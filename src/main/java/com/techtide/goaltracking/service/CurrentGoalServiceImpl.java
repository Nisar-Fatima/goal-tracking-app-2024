package com.techtide.goaltracking.service;

import com.techtide.goaltracking.entity.CurrentGoalEntity;
import com.techtide.goaltracking.repository.CurrentGoalRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Override
    public List<CurrentGoalEntity> findAllCurrentGoals() {return currentGoalRepo.findAll();
    }
    @Override
    public List<CurrentGoalEntity> getDatesForGoal(String currentSavedGoals) {
        List<CurrentGoalEntity> all = currentGoalRepo.findAll();
        // Filter out entries only for the selected goal
        List<CurrentGoalEntity> filteredList = all.stream()
                .filter(entity -> entity.getCurrentGoal().equals(currentSavedGoals))
                .collect(Collectors.toList());
        return filteredList;
    }
    @Override
    public Optional<CurrentGoalEntity> getCurrentGoalForDate(String selectedGoal, LocalDate selectedDate) {
        return currentGoalRepo.findByCurrentGoalAndDate(selectedGoal, selectedDate);
    }
    @Override
    public void delete(CurrentGoalEntity currentGoalEntity) {
        currentGoalRepo.delete(currentGoalEntity);
    }
}
