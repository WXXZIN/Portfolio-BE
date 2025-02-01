package com.wxxzin.portfolio.server.domain.team.enums;

import lombok.Getter;

@Getter
public enum TaskPriority {
    LOW(3),
    MEDIUM(2),
    HIGH(1);

    private final int priority;

    TaskPriority(int priority) {
        this.priority = priority;
    }

    public static TaskPriority of(int priority) {
        for (TaskPriority taskPriority : values()) {
            if (taskPriority.priority == priority) {

                System.out.println("TaskPriority: " + taskPriority);

                return taskPriority;
            }
        }
        throw new IllegalArgumentException("Priority not found. priority: " + priority);
    }
}
