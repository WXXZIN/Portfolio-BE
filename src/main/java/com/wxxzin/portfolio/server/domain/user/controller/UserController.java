package com.wxxzin.portfolio.server.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.auth.security.model.PrincipalUser;
import com.wxxzin.portfolio.server.common.response.success.ApiSuccessResponse;
import com.wxxzin.portfolio.server.common.response.success.SuccessMessage;
import com.wxxzin.portfolio.server.domain.user.dto.request.LocalUserRegisterRequestDTO;
import com.wxxzin.portfolio.server.domain.user.dto.response.UserResponseDTO;
import com.wxxzin.portfolio.server.domain.user.service.BaseUserService;
import com.wxxzin.portfolio.server.domain.user.service.LocalUserService;

@RestController
@Slf4j
@Tag(name = "User", description = "유저 관련 API")
@RequestMapping("/api/v1/user")
public class UserController {

    private final BaseUserService baseUserService;
    private final LocalUserService localUserService;

    public UserController(
        BaseUserService baseUserService, 
        LocalUserService localUserService
    ) {
        this.baseUserService = baseUserService;
        this.localUserService = localUserService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    public ResponseEntity<ApiSuccessResponse<Void>> registerUser(   
        @RequestBody LocalUserRegisterRequestDTO requestDTO
    ) {
        
        localUserService.registerUser(requestDTO);

        return ResponseEntity
            .status(201)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.USER_REGISTER_SUCCESS.getMessage()
                )
            );
    }

    @GetMapping("/is-username-taken")
    @Operation(summary = "아이디 중복 확인", description = "아이디 중복을 확인합니다.")
    public ResponseEntity<ApiSuccessResponse<Boolean>> isUsernameTaken(
        @RequestParam(name = "username") String username
    ) {

        boolean isUsernameTaken = localUserService.isUsernameTaken(username);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.USER_USERNAME_CHECK_SUCCESS.getMessage(),
                    isUsernameTaken
                )
            );
    }

    @GetMapping("/is-nickname-taken")
    @Operation(summary = "닉네임 중복 확인", description = "닉네임 중복을 확인합니다.")
    public ResponseEntity<ApiSuccessResponse<Boolean>> isNicknameTaken(
        @RequestParam(name = "nickname") String nickname
    ) {

        boolean isNicknameTaken = baseUserService.isNicknameTaken(nickname);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.USER_NICKNAME_CHECK_SUCCESS.getMessage(),
                    isNicknameTaken
                )
            );
    }

    @GetMapping("/profile")
    @Operation(summary = "프로필 조회")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal PrincipalUser principalUser) {

        Long userId = principalUser.getUserId();

        UserResponseDTO userResponseDTO = baseUserService.getUserProfile(userId);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.USER_INFO_RETRIEVE_SUCCESS.getMessage(),
                    userResponseDTO
                )
            );
    }

    @PutMapping("/change-nickname")
    @Operation(summary = "닉네임 변경", description = "닉네임을 변경합니다.")
    public ResponseEntity<ApiSuccessResponse<Void>> changeNickname(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @RequestBody String newNickname
    ) {
        
        Long userId = principalUser.getUserId();
    
        baseUserService.changeNickname(userId, newNickname);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.USER_NICKNAME_CHANGE_SUCCESS.getMessage()
                )
            );
    }
}
