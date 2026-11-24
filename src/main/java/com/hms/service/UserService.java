package com.hms.service;

import com.hms.entity.AppUser;
import com.hms.payload.LoginDto;
import com.hms.repository.AppUserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final AppUserRepository appUserRepository;
    private final JWTService jwtService;

    public UserService(AppUserRepository appUserRepository, JWTService jwtService) {
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
    }

    public String verifyLogin(LoginDto dto) {
        if (dto == null || dto.getUsername() == null || dto.getPassword() == null) {
            return null;
        }

        Optional<AppUser> opUser = appUserRepository.findByUsername(dto.getUsername());
        if (opUser.isEmpty()) {
            return null;
        }

        AppUser appUser = opUser.get();
        if (!BCrypt.checkpw(dto.getPassword(), appUser.getPassword())) {
            return null;
        }

        return jwtService.generateToken(appUser.getUsername());
    }
}
