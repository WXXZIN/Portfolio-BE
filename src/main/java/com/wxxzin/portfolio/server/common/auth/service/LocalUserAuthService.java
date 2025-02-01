package com.wxxzin.portfolio.server.common.auth.service;

import com.wxxzin.portfolio.server.common.auth.dto.request.ChangePasswordRequestDTO;

public interface LocalUserAuthService {
   String findUsernameByEmail(String email);
   void changePassword(Long userId, ChangePasswordRequestDTO requestDTO);
}
