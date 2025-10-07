package com.ghm.guesthousemanagementsystem.controller;

import com.ghm.guesthousemanagementsystem.dto.LoginRequestDto;
import com.ghm.guesthousemanagementsystem.dto.LoginResponseDto;
import com.ghm.guesthousemanagementsystem.entity.AdminUser;
import com.ghm.guesthousemanagementsystem.repository.AdminUserRepository;
import com.ghm.guesthousemanagementsystem.security.JwtService;
import com.ghm.guesthousemanagementsystem.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;
    private final AdminUserRepository adminUserRepo;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword()
                    )
            );

            UserDetails userDetails = jwtService.loadUserByEmail(requestDto.getEmail());
            String jwtToken = jwtUtil.generateToken(userDetails);

            AdminUser adminUser = adminUserRepo.findByEmail(requestDto.getEmail()).orElseThrow();
            adminUser.setLastLogin(LocalDateTime.now());
            adminUserRepo.save(adminUser);

            return ResponseEntity.ok(new LoginResponseDto(
                    jwtToken,
                    adminUser.getName(),
                    adminUser.getEmail(),
                    adminUser.getLastLogin()
            ));
        } catch (AuthenticationException except) {
            return ResponseEntity.status(401).build();
        }
    }
}
