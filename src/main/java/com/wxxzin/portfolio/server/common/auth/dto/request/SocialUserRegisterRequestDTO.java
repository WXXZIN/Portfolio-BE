package com.wxxzin.portfolio.server.common.auth.dto.request;

import com.wxxzin.portfolio.server.common.auth.security.oauth2.userinfo.OAuth2UserInfo;

public record SocialUserRegisterRequestDTO(
    String email,
    String socialId    
) implements OAuth2UserInfo {

    @Override
    public String getSocialId() {
        return socialId;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
