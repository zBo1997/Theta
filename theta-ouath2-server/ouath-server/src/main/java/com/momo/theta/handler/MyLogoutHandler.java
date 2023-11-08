package com.momo.theta.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class MyLogoutHandler implements LogoutHandler {

    @Autowired
    private TokenStore tokenStore;

    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Assert.notNull(this.tokenStore, "tokenStore must be set");
        String token = request.getHeader("Authorization");
        Assert.hasText(token, "token must be set");
        OAuth2AccessToken existingAccessToken = this.tokenStore.readAccessToken(token);
        if (existingAccessToken != null) {
            if (existingAccessToken.getRefreshToken() != null) {
                log.info("remove refreshToken!", existingAccessToken.getRefreshToken());
                OAuth2RefreshToken refreshToken = existingAccessToken.getRefreshToken();
                this.tokenStore.removeRefreshToken(refreshToken);
            }

            log.info("remove existingAccessToken!", existingAccessToken);
            this.tokenStore.removeAccessToken(existingAccessToken);
        }

    }
}
