package com.wxxzin.portfolio.server.domain.user.service;

import com.wxxzin.portfolio.server.domain.user.dto.request.LocalUserRegisterRequestDTO;

public interface LocalUserService {
    boolean isUsernameTaken(String username);
    void registerUser(LocalUserRegisterRequestDTO localUserRegisterRequestDTO);
}
