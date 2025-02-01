package com.wxxzin.portfolio.server.domain.team.enums;

import lombok.Getter;

@Getter
public enum TaskStatus {
    TODO("할 일"),
    DONE("완료");

    private final String status;

    TaskStatus(String status) {
        this.status = status;
    }

    public static TaskStatus of(String status) {
        for (TaskStatus taskStatus : TaskStatus.values()) {
            if (taskStatus.getStatus().equals(status)) {
                return taskStatus;
            }
        }
        throw new IllegalArgumentException("TaskStatus에 해당하는 상태가 없습니다.");
    }
}
