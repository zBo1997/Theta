package com.momo.theta.config;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@AllArgsConstructor
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 配置authenticationManager用于认证的过程
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
            .authenticationManager(authenticationManager);
    }

    /**
     * 重写此方法用于声明认证服务器能认证的客户端信息
     * 相当于在认证服务器中注册哪些客户端（包括资源服务器）能访问
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient("orderApp")  //声明访问认证服务器的客户端
            .secret(passwordEncoder.encode("123456"))  //客户端访问认证服务器需要带上的密码
            .scopes("read","write")  //获取token包含的哪些权限
            .accessTokenValiditySeconds(3600)  //token过期时间
            .resourceIds("order-service")  //指明请求的资源服务器
            .authorizedGrantTypes("password")  //密码模式
            .and()
            //资源服务器拿到了客户端请求过来的token之后会请求认证服务器去判断此token是否正确或者过期
            //所以此时的资源服务器对于认证服务器来说也充当了客户端的角色
            .withClient("order-service")
            .secret(passwordEncoder.encode("123456"))
            .scopes("read")
            .accessTokenValiditySeconds(3600)
            .resourceIds("order-service")
            .authorizedGrantTypes("password");
    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("isAuthenticated()");
    }
}
