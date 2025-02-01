package com.wxxzin.portfolio.server.common.email.dto.request;

public record EmailCertificationRequestDTO(
    String email,
    String certificationNumber
) {
}
