package com.techtide.goaltracking.service;

import com.techtide.goaltracking.entity.CurrentGoalEntity;

import java.time.LocalDate;

public interface CurrentGoalService {
    CurrentGoalEntity save(CurrentGoalEntity entity);
    boolean existsEntry(String goal, LocalDate date);
}
