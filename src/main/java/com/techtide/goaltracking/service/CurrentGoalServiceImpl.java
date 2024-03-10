package com.techtide.goaltracking.service;

import com.techtide.goaltracking.entity.CurrentGoalEntity;
import com.techtide.goaltracking.repository.CurrentGoalRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CurrentGoalServiceImpl implements CurrentGoalService {
    private final CurrentGoalRepo currentGoalRepo;

    @Override
    public CurrentGoalEntity save(CurrentGoalEntity entity) {
        return currentGoalRepo.saveAndFlush(entity);
    }

    @Override
    public boolean existsEntry(String currentgoal, LocalDate date) {
        return currentGoalRepo.existsByCurrentGoalAndDate(currentgoal, date);
    }


}
