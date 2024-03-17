package com.techtide.goaltracking.service;
import com.techtide.goaltracking.entity.SignUpEntity;
import com.techtide.goaltracking.repository.SignUpRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

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
    public boolean validateLogin(String username, String password) {
        Optional<SignUpEntity> userOpt = signUpRepo.findByUsername(username);
        if (userOpt.isPresent()) {
            SignUpEntity user = userOpt.get();
            return user.getPassword().equals(password);
        }
            return false;
    }

    @Override
    public Boolean updatePassword(String username, String newPassword) {
            Optional<SignUpEntity> userOpt = signUpRepo.findByUsername(username);
            if (userOpt.isPresent()) {
                SignUpEntity user = userOpt.get();
                user.setPassword(newPassword);
                signUpRepo.save(user);
                return true;
            }
            return false;
        }
    }
