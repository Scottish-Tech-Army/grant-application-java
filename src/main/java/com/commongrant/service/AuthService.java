package com.commongrant.service;

import com.commongrant.dto.*;
import com.commongrant.model.*;
import com.commongrant.repository.*;
import com.commongrant.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final OrganizationRepository orgRepo;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    @Transactional
    public LoginResponse register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        Organization org = Organization.builder()
                .name(req.getOrgName())
                .ein(req.getEin())
                .city(req.getCity())
                .state(req.getState())
                .taxStatus(Organization.TaxStatus.NONPROFIT_501C3)
                .build();
        org = orgRepo.save(org);

        AppUser user = AppUser.builder()
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .fullName(req.getFullName())
                .role(AppUser.UserRole.NONPROFIT)
                .organization(org)
                .build();
        user = userRepo.save(user);

        String token = tokenProvider.generateToken(user.getId(), org.getId(), user.getEmail(), user.getRole().name());
        return LoginResponse.builder()
                .token(token).email(user.getEmail())
                .fullName(user.getFullName())
                .orgId(org.getId()).orgName(org.getName())
                .role(user.getRole().name())
                .build();
    }

    public LoginResponse login(LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

        AppUser user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = tokenProvider.generateToken(
                user.getId(), user.getOrganization().getId(),
                user.getEmail(), user.getRole().name());

        return LoginResponse.builder()
                .token(token).email(user.getEmail())
                .fullName(user.getFullName())
                .orgId(user.getOrganization().getId())
                .orgName(user.getOrganization().getName())
                .role(user.getRole().name())
                .build();
    }
}

