package com.techtide.goaltracking.service;

import com.techtide.goaltracking.entity.NewGoalEntity;

import java.util.List;

public interface NewGoalService {
    NewGoalEntity save(NewGoalEntity entity);
    List<NewGoalEntity> getAllGoals();
    void delete(String goal);


    NewGoalEntity getGoalByGoalName(String selectedGoal);

    void updateGoal(NewGoalEntity goalEntity);
}
