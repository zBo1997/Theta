package com.momo.theta.controller;

import com.momo.theta.Result;
import com.momo.theta.dto.Oauth2TokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;


/**
 * @Author
 * @Description
 * @Date
 */
@Controller
@RequestMapping("/view")
public class AuthController {

    @RequestMapping("/login")
    public String loginView(Model model) {
        return "oauth-login";
    }
}

