package com.wxxzin.portfolio.server.domain.project.enums;

import lombok.Getter;

@Getter
public enum RecruitmentStatus {
    RECRUITING("모집 중"),
    COMPLETED("모집 완료"),
    CANCELED("모집 취소");

    private final String status;

    RecruitmentStatus(String status) {
        this.status = status;
    }
}
