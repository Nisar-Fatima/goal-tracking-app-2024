package com.techtide.goaltracking.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
    @Entity
    @Table(schema = "goal", name="newgoal", uniqueConstraints = {
            @UniqueConstraint(name ="Newgoal_uk1", columnNames = "goal")
    })
    public class NewGoalEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "name")
        private String name;

        @Column(name = "goal")
        private String goal;

        @Column(name = "start_date")
        private LocalDate startDate;

        @Column(name = "end_date")
        private LocalDate endDate;

    @OneToMany(mappedBy = "newGoal", cascade = CascadeType.ALL)
    private List<CurrentGoalEntity> currentGoals;


}

