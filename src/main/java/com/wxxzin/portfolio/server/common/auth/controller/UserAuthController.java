package com.wxxzin.portfolio.server.common.auth.controller;

import java.io.IOException;

import com.wxxzin.portfolio.server.common.auth.dto.request.ChangePasswordRequestDTO;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.auth.dto.request.SocialUserRegisterRequestDTO;
import com.wxxzin.portfolio.server.common.auth.dto.response.UserAuthResponseDTO;
import com.wxxzin.portfolio.server.common.auth.security.model.PrincipalUser;
import com.wxxzin.portfolio.server.common.auth.service.BaseUserAuthService;
import com.wxxzin.portfolio.server.common.auth.service.LocalUserAuthService;
import com.wxxzin.portfolio.server.common.auth.service.SocialUserAuthService;
import com.wxxzin.portfolio.server.common.response.success.ApiSuccessResponse;
import com.wxxzin.portfolio.server.common.response.success.SuccessMessage;
import com.wxxzin.portfolio.server.domain.user.dto.response.UserResponseDTO;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;

@RestController
@Slf4j
@Tag(name = "User - Auth", description = "유저 인증 관련 API")
@RequestMapping("/api/v1/user/auth")
public class UserAuthController {
    
    private final BaseUserAuthService baseUserAuthService;
    private final LocalUserAuthService localUserAuthService;
    private final SocialUserAuthService socialUserAuthService;
    
    public UserAuthController(
        BaseUserAuthService baseUserAuthService, 
        LocalUserAuthService localUserAuthService,
        SocialUserAuthService socialUserAuthService
    ) {
        this.baseUserAuthService = baseUserAuthService;
        this.localUserAuthService = localUserAuthService;
        this.socialUserAuthService = socialUserAuthService;
    }

    @PostMapping("/sdk/oauth2/{provider}")
    @Operation(summary = "소셜 로그인", description = "소셜 로그인을 진행합니다.")
    public ResponseEntity<ApiSuccessResponse<UserResponseDTO>> socialLogin(
        @PathVariable(name = "provider") String provider,
        @RequestBody SocialUserRegisterRequestDTO requestDTO,
        HttpServletRequest request
    ) throws IOException {

        Provider providerValue = Provider.valueOf(provider.toUpperCase());

        UserAuthResponseDTO userAuthResponseDTO = socialUserAuthService.updateOrRegisterUserForApp(
            providerValue, 
            requestDTO,
            request
        );

        return ResponseEntity
            .status(200)
            .header("Authorization", "Bearer " + userAuthResponseDTO.accessToken())
            .header("Set-Cookie", userAuthResponseDTO.refreshToken())
            .header("Set-Cookie", userAuthResponseDTO.deviceId())
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.AUTH_LOGIN_SUCCESS.getMessage(),
                    userAuthResponseDTO.userResponseDTO()
                )
            );
    }

    @PostMapping("/find-username")
    @Operation(summary = "아이디 찾기", description = "이메일로 아이디를 찾습니다.")
    public ResponseEntity<ApiSuccessResponse<String>> findUsernameByEmail(
        @RequestBody String email
    ) {

        String username = localUserAuthService.findUsernameByEmail(email);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.AUTH_USERNAME_FIND_SUCCESS.getMessage(),
                    username
                )
            );
    }

    @PutMapping("/change-password")
    @Operation(summary = "비밀번호 변경", description = "비밀번호를 변경합니다.")
    public ResponseEntity<ApiSuccessResponse<Void>> changePassword(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO
    ) {

        Long userId = principalUser.getUserId();

        localUserAuthService.changePassword(userId, changePasswordRequestDTO);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.AUTH_PASSWORD_CHANGE_SUCCESS.getMessage()
                )
            );
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃을 진행합니다.")
    public ResponseEntity<ApiSuccessResponse<Void>> logout(
        @CookieValue(name = "refreshToken") String refreshToken,
        @CookieValue(name = "deviceId") String deviceId
    ) {

        baseUserAuthService.logoutCurrentDevice(refreshToken, deviceId);

        return ResponseEntity
            .status(200)
            .header("Authorization", "Bearer ")
            .header("Set-Cookie", "refreshToken=; httpOnly=true; path=/; sameSite=Lax; Max-Age=0")
            .header("Set-Cookie", "deviceId=; httpOnly=true; path=/; sameSite=Lax; Max-Age=0")
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.AUTH_LOGOUT_SUCCESS.getMessage()
                )
            );
    }

    @PostMapping("/logout/{targetDeviceId}")
    @Operation(summary = "특정 디바이스 로그아웃", description = "특정 디바이스를 로그아웃합니다.")
    public ResponseEntity<ApiSuccessResponse<Void>> logoutTargetDevice(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @PathVariable(name = "targetDeviceId") String targetDeviceId
    ) {

        Long userId = principalUser.getUserId();

        baseUserAuthService.logoutTargetDevice(userId, targetDeviceId);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.AUTH_LOGOUT_SUCCESS.getMessage()
                )
            );
    }

    @DeleteMapping("/delete")
    @Operation(summary ="회원 탈퇴", description = "회원 탈퇴를 진행합니다.")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteUser(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @RequestHeader(name = "X-Client-Type") String clientType
    ) {

        Long userId = principalUser.getUserId();

        baseUserAuthService.deleteUser(userId, clientType);

        return ResponseEntity.
            status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.USER_DELETE_SUCCESS.getMessage()
                )
            );
    }
    
    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급", description = "토큰을 재발급합니다.")
    public ResponseEntity<ApiSuccessResponse<Void>> reissueAccessToken(
        @CookieValue(name = "refreshToken") String refreshToken,
        @CookieValue(name = "deviceId") String deviceId
    ) { 

        String newAccessToken = baseUserAuthService.reissueAccessToken(refreshToken, deviceId);

        return ResponseEntity
            .status(200)
            .header("Authorization", "Bearer " + newAccessToken)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.AUTH_TOKEN_REISSUE_SUCCESS.getMessage()
                )
            );
    }

    @PostMapping("/reissue/social")
    @Operation(summary = "토큰 재발급", description = "토큰을 재발급합니다.")
    public ResponseEntity<ApiSuccessResponse<UserResponseDTO>> reissueAccessTokenForSocialLogin(
        @CookieValue(name = "refreshToken") String refreshToken,
        @CookieValue(name = "deviceId") String deviceId
    ) {

        UserAuthResponseDTO userAuthResponseDTO = baseUserAuthService.reissueAccessTokenForSocialLogin(refreshToken, deviceId);
        
        return ResponseEntity
            .status(200)
            .header("Authorization", "Bearer " + userAuthResponseDTO.accessToken())
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.AUTH_TOKEN_REISSUE_SUCCESS.getMessage(),
                    userAuthResponseDTO.userResponseDTO()
                )
            );
    }
}
