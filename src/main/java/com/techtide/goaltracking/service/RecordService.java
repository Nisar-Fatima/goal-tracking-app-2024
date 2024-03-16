package com.techtide.goaltracking.service;

import com.techtide.goaltracking.entity.CurrentGoalEntity;

import java.util.List;

public interface RecordService {
    List<CurrentGoalEntity> findAllDataForGoal(String goalName);



}
