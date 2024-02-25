package com.techtide.goaltracking.service;
import com.techtide.goaltracking.entity.SignUpEntity;
import com.techtide.goaltracking.repository.SignUpRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {
    private final SignUpRepo signUpRepo;

    @Override
    public SignUpEntity save(SignUpEntity entity) {
        return signUpRepo.saveAndFlush(entity);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return signUpRepo.existsByUsername(username);
    }

    public Boolean existsByEmail(String email) {
        return signUpRepo.existsByEmail(email);
    }

    @Override
    public void validateSignUpData(String username, String email, String password) throws IllegalArgumentException {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("Must enter all information.");
        }
        if (existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists.");
        }
        if (!isValidEmail(email) || existsByEmail(email)) {
            throw new IllegalArgumentException("Invalid email.");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._%+-]+@gmail\\.com");
    }
}
