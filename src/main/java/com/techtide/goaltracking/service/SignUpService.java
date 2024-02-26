package com.techtide.goaltracking.service;
import com.techtide.goaltracking.entity.SignUpEntity;
public interface SignUpService {
    SignUpEntity save(SignUpEntity entity);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}