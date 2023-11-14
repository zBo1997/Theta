package com.momo.theta.service.impl;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClientDetailsServiceImpl implements ClientDetailsService {

    //private OAuthClientFeignClient oAuthClientFeignClient;

    @Override
    @SneakyThrows
    public ClientDetails loadClientByClientId(String clientId) {
//        try {
//            Result<SysOauthClient> result = oAuthClientFeignClient.getOAuthClientById(clientId);
//            if (Result.success().getCode().equals(result.getCode())) {
//                SysOauthClient client = result.getData();
//                BaseClientDetails clientDetails = new BaseClientDetails(
//                        client.getClientId(),
//                        client.getResourceIds(),
//                        client.getScope(),
//                        client.getAuthorizedGrantTypes(),
//                        client.getAuthorities(),
//                        client.getWebServerRedirectUri());
//                clientDetails.setClientSecret(PasswordEncoderTypeEnum.NOOP.getPrefix() + client.getClientSecret());
//                return clientDetails;
//            } else {
//                throw new NoSuchClientException("No client with requested id: " + clientId);
//            }
//        } catch (EmptyResultDataAccessException var4) {
//            throw new NoSuchClientException("No client with requested id: " + clientId);
//        }
        return null;
    }
}