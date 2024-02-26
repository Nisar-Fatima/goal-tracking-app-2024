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


}
