package com.wxxzin.portfolio.server.domain.team.enums;

import lombok.Getter;

@Getter
public enum ApplicationStatus {
    PENDING("대기 중"),
    APPROVED("승인됨"),
    REJECTED("거절됨");

    private final String status;

    ApplicationStatus(String status) {
        this.status = status;
    }
}
