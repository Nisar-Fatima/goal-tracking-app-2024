package com.techtide.goaltracking.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.Duration;
import java.time.LocalDate;
@Data
@Entity
@Table(schema = "goal", name="current_goals")
public class CurrentGoalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "current_goal")
    private String currentGoal;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time_spent")
    private Duration timeSpent;

    @Column(name = "current_task")
    private String currentTask;

    @ManyToOne
    private NewGoalEntity newGoal;

}
