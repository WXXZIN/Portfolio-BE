package com.wxxzin.portfolio.server.common.auth.service.implement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.auth.service.RevokeService;
import com.wxxzin.portfolio.server.common.exception.RemovalException;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;
import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;

@Service
@Slf4j
public class RevokeServiceImpl implements RevokeService {

    @Value("google.revoke.url")
    private String GOOGLE_REVOKE_URL;

    @Value("kakao.revoke.url")
    private String KAKAO_REVOKE_URL;

    @Value("naver.revoke.url")
    private String NAVER_REVOKE_URL;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;

    @Value("spring.security.oauth2.client.registration.naver.client-secret")
    private String NAVER_CLIENT_SECRET;

    @Override
    public void revokeAccess(BaseUserEntity baseUserEntity) {
        Provider provider = baseUserEntity.getProvider();

        if (provider == Provider.LOCAL) return;

        String accessToken = baseUserEntity.getSocialUserEntity().getAccessToken();

        switch (provider) {
            case GOOGLE -> sendRevokeRequest(GOOGLE_REVOKE_URL, "token=" + accessToken, null);
            case KAKAO -> sendRevokeRequest(KAKAO_REVOKE_URL, null, accessToken);
            case NAVER -> sendRevokeRequest(NAVER_REVOKE_URL, getNaverData(accessToken), null);
            default -> throw new RemovalException(ErrorMessage.NOT_SUPPORTED_PROVIDER);
        }
    }

    private String getNaverData(String accessToken) {
        return "client_id= " + NAVER_CLIENT_ID
                + "&client_secret=" + NAVER_CLIENT_SECRET
                + "&grant_type=delete"
                + "&access_token=" + accessToken
                + "&grant_type=delete";
    }

    private void sendRevokeRequest(String revokeUrl, String data, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        if (accessToken != null) {
            headers.setBearerAuth(accessToken);
        }

        HttpEntity<String> entity = new HttpEntity<>(data, headers);
        restTemplate.exchange(revokeUrl, HttpMethod.POST, entity, String.class);
    }
}
