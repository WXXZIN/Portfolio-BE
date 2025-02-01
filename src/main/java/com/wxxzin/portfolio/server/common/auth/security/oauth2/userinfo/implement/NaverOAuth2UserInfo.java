package com.wxxzin.portfolio.server.common.auth.security.oauth2.userinfo.implement;

import java.util.Map;

import com.wxxzin.portfolio.server.common.auth.security.oauth2.userinfo.OAuth2UserInfo;

public class NaverOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    @SuppressWarnings("unchecked")
    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getSocialId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }
}
