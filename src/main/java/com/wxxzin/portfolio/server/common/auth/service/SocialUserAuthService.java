package com.wxxzin.portfolio.server.common.auth.service;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;

import com.wxxzin.portfolio.server.common.auth.dto.request.SocialUserRegisterRequestDTO;
import com.wxxzin.portfolio.server.common.auth.dto.response.UserAuthResponseDTO;
import com.wxxzin.portfolio.server.common.auth.security.oauth2.userinfo.OAuth2UserInfo;
import com.wxxzin.portfolio.server.domain.user.dto.response.UserResponseDTO;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;

public interface SocialUserAuthService {
    UserResponseDTO updateOrRegisterUserForWeb(Provider provider, OAuth2UserInfo oAuth2UserInfo, String accessToken);   
    UserAuthResponseDTO updateOrRegisterUserForApp(
        Provider provider,
        SocialUserRegisterRequestDTO socialUserRegisterRequestDTO,
        HttpServletRequest request
    ) throws IOException;
}
