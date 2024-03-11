package com.techtide.goaltracking.repository;

import com.techtide.goaltracking.entity.CurrentGoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CurrentGoalRepo extends JpaRepository<CurrentGoalEntity, Long>  {
    boolean existsByCurrentGoalAndDate(String currentgoal, LocalDate date);
    List<CurrentGoalEntity> findAllByCurrentGoal(String goalNames);

    @Query("SELECT c.timeSpent FROM CurrentGoalEntity c WHERE c.newGoal.goal = :goalName")
    List<Duration> findTimeSpentForGoal(String goalName);

    @Query("SELECT COUNT(DISTINCT c.date) FROM CurrentGoalEntity c WHERE c.newGoal.goal = :goalName")
    Long findTotalDaysForGoal(@Param("goalName") String goalName);


}
