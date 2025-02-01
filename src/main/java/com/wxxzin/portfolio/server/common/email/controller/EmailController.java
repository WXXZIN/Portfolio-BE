package com.wxxzin.portfolio.server.common.email.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.email.dto.request.EmailCertificationRequestDTO;
import com.wxxzin.portfolio.server.common.email.dto.request.TemporaryPasswordRequestDTO;
import com.wxxzin.portfolio.server.common.email.service.EmailAuthService;
import com.wxxzin.portfolio.server.common.response.success.ApiSuccessResponse;
import com.wxxzin.portfolio.server.common.response.success.SuccessMessage;

@RestController
@Slf4j
@Tag(name = "Email", description = "이메일 관련 API")
@RequestMapping("/api/v1/email")
public class EmailController {
    
    private final EmailAuthService emailAuthService;

    public EmailController(EmailAuthService emailAuthService) {
        this.emailAuthService = emailAuthService;
    }

    @PostMapping("/certification")
    @Operation(summary = "인증 메일 전송", description = "인증 메일을 전송합니다.")
    public ResponseEntity<ApiSuccessResponse<?>> sendCertificationEmail(
        @RequestBody String email
    ) {

        emailAuthService.sendCertificationEmail(email);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.AUTH_VERIFICATION_EMAIL_SEND_SUCCESS.getMessage()
                )
            );
    }

    @PostMapping("/temporary-password")
    @Operation(summary = "임시 비밀번호 전송", description = "임시 비밀번호를 전송합니다.")
    public ResponseEntity<ApiSuccessResponse<Void>> sendTemporaryPassword(
        @RequestBody TemporaryPasswordRequestDTO request
    ) {

        String username = request.username();
        String email = request.email();

        emailAuthService.sendTemporaryPasswordEmail(username, email);

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.AUTH_TEMP_PASSWORD_EMAIL_SEND_SUCCESS.getMessage()
                )
            );
    }

    @PostMapping("/check-certification")
    @Operation(summary = "이메일 인증 확인", description = "이메일 인증 번호를 확인합니다.")
    public ResponseEntity<ApiSuccessResponse<?>> checkCertificationNumber(
        @RequestBody EmailCertificationRequestDTO requestDTO
    ) {
        
        emailAuthService.checkCertificationNumber(
            requestDTO.email(), 
            requestDTO.certificationNumber()
        );
        
        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.AUTH_EMAIL_VERIFICATION_SUCCESS.getMessage()
                )
            );
    }
}
