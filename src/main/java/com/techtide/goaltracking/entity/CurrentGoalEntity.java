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

    @Column(name = "goal")
    private String goal;

    @Column(name = "date")
    private LocalDate date;


    @Column(name = "time_spent")
    private Duration timeSpent;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    private NewGoalEntity newGoal;

}
