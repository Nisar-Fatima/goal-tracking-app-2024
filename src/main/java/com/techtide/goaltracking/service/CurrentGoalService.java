package com.techtide.goaltracking.service;

import com.techtide.goaltracking.entity.CurrentGoalEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CurrentGoalService {
    CurrentGoalEntity save(CurrentGoalEntity entity);
    boolean existsEntry(String goal, LocalDate date);
    List<CurrentGoalEntity> findAllCurrentGoals();
    List<CurrentGoalEntity> getDatesForGoal(String savedCurrentGoals);
    Optional<CurrentGoalEntity> getCurrentGoalForDate(String selectedGoal, LocalDate selectedDate);
    void delete(CurrentGoalEntity currentGoalEntity);
}
