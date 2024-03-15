package com.techtide.goaltracking.service;

import com.techtide.goaltracking.entity.CurrentGoalEntity;
import com.techtide.goaltracking.repository.CurrentGoalRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {
    private final CurrentGoalRepo currentGoalRepo;
    @Override
    public List<CurrentGoalEntity> findAllDataForGoal(String goalName) {
        return currentGoalRepo.findAllByCurrentGoal(goalName);
    }
}
