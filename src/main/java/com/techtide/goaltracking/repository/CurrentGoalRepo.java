package com.techtide.goaltracking.repository;

import com.techtide.goaltracking.entity.CurrentGoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentGoalRepo extends JpaRepository<CurrentGoalEntity, Long>  {

}
