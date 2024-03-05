package com.techtide.goaltracking.service;

import com.techtide.goaltracking.entity.CurrentGoalEntity;
import com.techtide.goaltracking.repository.CurrentGoalRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentGoalImpl implements CurrentGoalService {
    private final CurrentGoalRepo currentGoalRepo;

    @Override
    public CurrentGoalEntity save(CurrentGoalEntity entity) {
        return currentGoalRepo.saveAndFlush(entity);
    }
}
