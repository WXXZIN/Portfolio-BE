package com.wxxzin.portfolio.server.common.auth.security.oauth2.userinfo.implement;

import java.util.Map;

import com.wxxzin.portfolio.server.common.auth.security.oauth2.userinfo.OAuth2UserInfo;

public class GoogleOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getSocialId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }
}
