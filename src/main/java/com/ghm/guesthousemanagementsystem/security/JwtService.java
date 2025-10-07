package com.ghm.guesthousemanagementsystem.security;

import com.ghm.guesthousemanagementsystem.entity.AdminUser;
import com.ghm.guesthousemanagementsystem.exception.ResourceNotFoundException;
import com.ghm.guesthousemanagementsystem.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService implements UserDetailsService {
    private final AdminUserRepository adminUserRepo;

    @Override
    public UserDetails loadUserByUsername(String email) {
        AdminUser adminUser = adminUserRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user with email: " + email + " not found"));
        return User.builder()
                .username(adminUser.getEmail())
                .password(adminUser.getPasswordHash())
                .roles("ADMIN")
                .build();

    }

    public UserDetails loadUserByEmail(String email) {
        return loadUserByUsername(email);
    }
}
