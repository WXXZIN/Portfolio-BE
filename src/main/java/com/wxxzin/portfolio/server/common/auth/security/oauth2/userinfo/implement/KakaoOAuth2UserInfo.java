package com.wxxzin.portfolio.server.common.auth.security.oauth2.userinfo.implement;

import java.util.Map;

import com.wxxzin.portfolio.server.common.auth.security.oauth2.userinfo.OAuth2UserInfo;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getSocialId() {
        return attributes.get("id").toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getEmail() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("kakao_account");

        if (properties == null) {
            return null;
        }

        return properties.get("email").toString();
    }
}
