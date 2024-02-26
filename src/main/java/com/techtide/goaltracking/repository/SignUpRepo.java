package com.techtide.goaltracking.repository;
import com.techtide.goaltracking.entity.SignUpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SignUpRepo extends JpaRepository<SignUpEntity, Long> {
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<SignUpEntity> findByUsername(String username);

}
