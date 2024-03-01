package com.techtide.goaltracking.repository;

import com.techtide.goaltracking.entity.NewGoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewGoalRepo extends JpaRepository<NewGoalEntity, Long> {

    void deleteByGoal(String goal);

    NewGoalEntity findByGoal(String selectedGoal);
}

