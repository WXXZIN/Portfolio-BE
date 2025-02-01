package com.wxxzin.portfolio.server.domain.team.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

import lombok.*;

import com.wxxzin.portfolio.server.common.entity.BaseTimeEntity;
import com.wxxzin.portfolio.server.domain.team.enums.TaskPriority;
import com.wxxzin.portfolio.server.domain.team.enums.TaskStatus;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "team_task")
public class TeamTaskEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus = TaskStatus.TODO;

    @Enumerated(EnumType.STRING)
    private TaskPriority taskPriority;

    @Column
    private String assigneeMemberName;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity teamEntity;

    @Builder
    public TeamTaskEntity(
        String title,
        String description,
        LocalDate deadline,
        TaskStatus taskStatus,
        TaskPriority taskPriority,
        String assigneeMemberName,
        TeamEntity teamEntity
    ) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.taskStatus = taskStatus != null ? taskStatus : TaskStatus.TODO;
        this.taskPriority = taskPriority;
        this.assigneeMemberName = assigneeMemberName;
        this.teamEntity = teamEntity;
    }
}
