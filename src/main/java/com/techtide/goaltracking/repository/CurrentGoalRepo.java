package com.techtide.goaltracking.repository;

import com.techtide.goaltracking.entity.CurrentGoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CurrentGoalRepo extends JpaRepository<CurrentGoalEntity, Long>  {
    boolean existsByCurrentGoalAndDate(String currentgoal, LocalDate date);
    Optional<CurrentGoalEntity> findByCurrentGoalAndDate(String selectedGoal, LocalDate selectedDate);

}
