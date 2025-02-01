package com.wxxzin.portfolio.server.common.auth.service;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;

import com.wxxzin.portfolio.server.common.auth.dto.request.SocialUserRegisterRequestDTO;
import com.wxxzin.portfolio.server.common.auth.dto.response.UserAuthResponseDTO;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;

public interface SocialUserAuthService {
    UserAuthResponseDTO updateOrRegisterUserForApp(
        Provider provider,
        SocialUserRegisterRequestDTO socialUserRegisterRequestDTO,
        HttpServletRequest request
    ) throws IOException;
}
