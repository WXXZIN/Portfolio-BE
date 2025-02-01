package com.wxxzin.portfolio.server.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.response.error.ApiErrorResponse;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAuthException.class)
    public ResponseEntity<ApiErrorResponse> handleUserAuthException(
        UserAuthException e
    ) {
        return ResponseEntity
                .status(401)
                .body (
                    ApiErrorResponse.of(
                        ErrorMessage.USER_AUTH_FAILED,
                        e.getErrorMessage()
                    )
                );
    }

    @ExceptionHandler(EmailSendException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailSendException(
        EmailSendException e
    ) {
        return ResponseEntity
                .status(500)
                .body (
                    ApiErrorResponse.of(
                        ErrorMessage.EMAIL_SEND_FAILED,
                        e.getErrorMessage()
                    )
                );
    }

    @ExceptionHandler(CertificationException.class)
    public ResponseEntity<ApiErrorResponse> handleCertificationException(
        CertificationException e
    ) {
        return ResponseEntity
                .status(400)
                .body (
                    ApiErrorResponse.of(
                        ErrorMessage.EMAIL_CERTIFICATION_FAILED,
                        e.getErrorMessage()
                    )
                );
    }

    @ExceptionHandler(CreationException.class)
    public ResponseEntity<ApiErrorResponse> handleCreationException(
        CreationException e
    ) {
        return ResponseEntity
                .status(400)
                .body (
                    ApiErrorResponse.of(
                        ErrorMessage.CREATION_FAILED,
                        e.getErrorMessage()
                    )
                );
    }

    @ExceptionHandler(RetrievalException.class)
    public ResponseEntity<ApiErrorResponse> handleRetrievalException(
        RetrievalException e
    ) {
        return ResponseEntity
                .status(400)
                .body (
                    ApiErrorResponse.of(
                        ErrorMessage.RETRIEVAL_FAILED,
                        e.getErrorMessage()
                    )
                );
    }

    @ExceptionHandler(ModificationException.class)
    public ResponseEntity<ApiErrorResponse> handleModificationException(
        ModificationException e
    ) {
        return ResponseEntity
                .status(400)
                .body (
                    ApiErrorResponse.of(
                        ErrorMessage.MODIFICATION_FAILED,
                        e.getErrorMessage()
                    )
                );
    }

    @ExceptionHandler(RemovalException.class)
    public ResponseEntity<ApiErrorResponse> handleRemovalException(
        RemovalException e
    ) {
        return ResponseEntity
                .status(400)
                .body (
                    ApiErrorResponse.of(
                        ErrorMessage.REMOVAL_FAILED,
                        e.getErrorMessage()
                    )
                );
    }
}
