package com.momo.theta.controller;

import com.momo.theta.Result;
import com.momo.theta.dto.Oauth2TokenDTO;
import com.momo.theta.entity.Account;
import com.momo.theta.service.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;


@RestController
@RequestMapping("/api/user/")
@Slf4j
public class AuthController {
    public final static String AUTH_CODE_DEVICE_NO_TYPE = "authorizationCodeAndDeviceNo";

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private IAccountService accountService;

    /**
     * Oauth2登录认证
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result<Oauth2TokenDTO> postAccessToken(Principal principal, @RequestBody Map<String, String> parameters) {
        parameters.put("grant_type", "grant_type");
        String presentedPassword = parameters.get("password");

        String username = parameters.get("username");
        String clientId = parameters.get("client_id");
        String isAuthLoginDevice = parameters.get("authLoginDevice");

        OAuth2AccessToken oAuth2AccessToken = null;
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(
                ((ObjectUtils.isEmpty(username) ? presentedPassword : username) + clientId).intern());
        RLock rLock = readWriteLock.readLock();
        rLock.lock();
        try {
            oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        } catch (Exception ex) {
            // 为token状态异常msg特殊处理成多语言返回
            log.error("AuthController.postAccessToken UccAuthenticationException ", ex);
            return Result.fail("登录失败");
        } finally {
            rLock.unlock();
        }
        // 使用BCrypt算法刷新密码
        //accountService.updatePasswordByLoginName(username, presentedPassword, AbstractUserConstant.USER_ACCOUNT_TYPE.USER_ACCOUNT_TYPE_YES_ORG_VALUE);

        if (oAuth2AccessToken == null) {
            return Result.success(null);
        }
        Map<String, Object> additionalInfo = oAuth2AccessToken.getAdditionalInformation();

        String userCode = (String) additionalInfo.get("userCode");

        Account account = accountService.findByUserCode(userCode).get(0);

        username = ObjectUtils.isEmpty(username) ? account.getThetaId() : username;
        if (null != parameters.get("auth_type") && parameters.get("auth_type").equals(AUTH_CODE_DEVICE_NO_TYPE)) {
            username = account.getThetaId();
        }

        String token = oAuth2AccessToken.getValue();

        Oauth2TokenDTO oauth2TokenDto = Oauth2TokenDTO.builder()
                .token(token)
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .scope(oAuth2AccessToken.getScope())
                .userCode(userCode)
                .loginName(username)
                .tokenType("Bearer").build();
        return Result.success(oauth2TokenDto);
    }
}

