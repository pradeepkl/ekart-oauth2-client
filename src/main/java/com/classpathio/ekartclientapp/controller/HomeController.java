package com.classpathio.ekartclientapp.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class HomeController {

    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;


    public HomeController(OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
    }

    @GetMapping("/user-info")
    public Map<String, String> userInfo(OAuth2AuthenticationToken token) {
        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(
            token.getAuthorizedClientRegistrationId(),
            token.getName()
        );
        if(client != null) {
            OAuth2AccessToken clientAccessToken = client.getAccessToken();
            String jwtToken = clientAccessToken.getTokenValue();
            Set<String> scopes = clientAccessToken.getScopes();
            Instant expiresAt = clientAccessToken.getExpiresAt();
            Instant issuedAt = clientAccessToken.getIssuedAt();
            String tokenType = clientAccessToken.getTokenType().getValue();

            HashMap<String, String> userInfo = new LinkedHashMap<>();
            userInfo.put("jwtToken", jwtToken);
            userInfo.put("scopes", StringUtils.collectionToCommaDelimitedString(scopes));
            userInfo.put("expiresAt", expiresAt.atZone(ZoneId.systemDefault()).format(java.time.format.DateTimeFormatter.ISO_DATE_TIME).toString());
            userInfo.put("issuedAt", issuedAt.atZone(ZoneId.systemDefault()).format(java.time.format.DateTimeFormatter.ISO_DATE_TIME).toString());
            return userInfo;
        }
        return null;
    }
}
