package com.techtide.goaltracking.service;

import com.techtide.goaltracking.entity.NewGoalEntity;
import com.techtide.goaltracking.repository.NewGoalRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewGoalServiceImpl implements NewGoalService {

    private final NewGoalRepo newGoalRepository;
    @Override
    public NewGoalEntity saveGoal(NewGoalEntity entity) {
        return newGoalRepository.save(entity);
    }
    @Override
    public List<NewGoalEntity> getAllGoals() {
        return newGoalRepository.findAll();
    }
    @Transactional
    @Override
    public void delete(String goal) {
        newGoalRepository.deleteByGoal(goal);
    }
    @Override
    public NewGoalEntity getGoalByName(String selectedGoal) {
        return newGoalRepository.findByGoal(selectedGoal);
    }
    @Override
    public void updateGoal(NewGoalEntity goalEntity) {
            newGoalRepository.save(goalEntity);
        }

    }








