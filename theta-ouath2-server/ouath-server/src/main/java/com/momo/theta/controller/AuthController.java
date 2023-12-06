package com.momo.theta.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import com.momo.theta.Result;
import com.momo.theta.constants.SecurityConstants;
import com.momo.theta.util.RequestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping ("/oauth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final TokenEndpoint tokenEndpoint;
    private final RedisTemplate redisTemplate;

    @PostMapping ("/token")
    public Object postAccessToken(Principal principal, @RequestParam Map<String, String> parameters)
        throws HttpRequestMethodNotSupportedException {
        log.info("token请求");

        /**
         * 获取登录认证的客户端ID
         *
         * 兼容两种方式获取Oauth2客户端信息（client_id、client_secret）
         * 方式一：client_id、client_secret放在请求路径中(注：当前版本已废弃)
         * 方式二：放在请求头（Request Headers）中的Authorization字段，且经过加密，例如 Basic Y2xpZW50OnNlY3JldA== 明文等于 client:secret
         */
        String clientId = RequestUtils.getClientId();
        log.info("OAuth认证授权 客户端ID:{}，请求参数：{}", clientId, JSONUtil.toJsonStr(parameters));

        /**
         * knife4j接口文档测试使用
         *
         * 请求头自动填充，token必须原生返回，不能有任何包装，否则显示 undefined undefined
         * 账号/密码:  client_id/client_secret : client/123456
         */
        if (SecurityConstants.TEST_CLIENT_ID.equals(clientId)) {
            return tokenEndpoint.postAccessToken(principal, parameters).getBody();
        }

        OAuth2AccessToken accessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        return Result.success(accessToken);
    }

    /**
     * 注销
     * @return
     */
    @DeleteMapping ("/logout")
    public Result logout() {
        String payload = RequestUtils.getJwtPayload();

        if (StrUtil.isNotBlank(payload)) {
            JSONObject entries = JSONUtil.parseObj(payload);
            if (entries != null) {
                String jti = entries.getStr("jti"); // JWT唯一标识
                Long expireTime = entries.getLong("exp"); // JWT过期时间戳(单位：秒)
                if (expireTime != null) {
                    long currentTime = System.currentTimeMillis() / 1000;// 当前时间（单位：秒）
                    if (expireTime > currentTime) { // token未过期，添加至缓存作为黑名单限制访问，缓存时间为token过期剩余时间
                        redisTemplate.opsForValue()
                            .set(SecurityConstants.BLACKLIST_TOKEN_PREFIX + jti, null, (expireTime - currentTime),
                                TimeUnit.SECONDS);
                    }
                } else { // token 永不过期则永久加入黑名单
                    redisTemplate.opsForValue().set(SecurityConstants.BLACKLIST_TOKEN_PREFIX + jti, null);
                }
            }
        }
        return Result.success("注销成功");
    }
}