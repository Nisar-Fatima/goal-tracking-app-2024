package com.techtide.goaltracking.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.Duration;
import java.time.LocalDate;
@Data
@Entity
@Table(schema = "goal", name="currentgoal")
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

    @ManyToOne
    private NewGoalEntity newgoal;

}
