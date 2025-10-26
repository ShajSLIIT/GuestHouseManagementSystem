package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.dto.AdminUserReqDto;
import com.ghm.guesthousemanagementsystem.dto.AdminUserResponseDto;
import com.ghm.guesthousemanagementsystem.entity.AdminUser;
import com.ghm.guesthousemanagementsystem.exception.ResourceNotFoundException;
import com.ghm.guesthousemanagementsystem.mapper.AdminUserMapper;
import com.ghm.guesthousemanagementsystem.repository.AdminUserRepository;
import com.ghm.guesthousemanagementsystem.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserRepository adminRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AdminUserResponseDto createAdminUser(AdminUserReqDto adminReqDto) {
        if (adminRepo.findByEmail(adminReqDto.getEmail()).isPresent()) {
           throw new IllegalArgumentException("Email already in use: " + adminReqDto.getEmail());
        }
        AdminUser adminUser = AdminUserMapper.toEntity(adminReqDto);
        adminUser.setPasswordHash(passwordEncoder.encode(adminReqDto.getPassword()));
        return AdminUserMapper.toResponseDto(adminRepo.save(adminUser));
    }

    @Override
    public List<AdminUserResponseDto> getAllAdminUsers() {
        return adminRepo.findAll().stream()
                .map(AdminUserMapper::toResponseDto).toList();
    }

    @Override
    public AdminUserResponseDto getAdminUserById(UUID id) {
        return adminRepo.findById(id).map(AdminUserMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found with id: " + id));
    }

    @Override
    public AdminUserResponseDto updateAdminUser(UUID id, AdminUserReqDto adminUserReqDto) {
        AdminUser existingAdminUser = adminRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin User with id " + id + " not found")
        );
        adminRepo.findByEmail(adminUserReqDto.getEmail())
                .filter(admin -> !admin.getId().equals(id))
                .ifPresent(admin ->{
                    throw new IllegalArgumentException("Email already in use: " + adminUserReqDto.getEmail());
                });

        existingAdminUser.setEmail(adminUserReqDto.getEmail());
        existingAdminUser.setPasswordHash(passwordEncoder.encode(adminUserReqDto.getPassword()));

        return AdminUserMapper.toResponseDto(adminRepo.save(existingAdminUser));
    }

    @Override
    public AdminUserResponseDto patchAdminUser(UUID id, Map<String, Object> newDetails) {
        AdminUser existingUser = adminRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user with id " + id + " not found"));

        if (newDetails.containsKey("name")) {
            String name = newDetails.get("name").toString();
            existingUser.setName(name);
        }

        if (newDetails.containsKey("email")) {
            String email = newDetails.get("email").toString();
            adminRepo.findByEmail(email)
                    .filter(admin -> !admin.getId().equals(id))
                    .ifPresent(admin ->{
                        throw new IllegalArgumentException("Email already in use: " + admin.getEmail());
                    });
            existingUser.setEmail(email);
        }

        if (newDetails.containsKey("password")) {
            String password = newDetails.get("password").toString();
            if (password != null && !password.isBlank()) {
                existingUser.setPasswordHash(passwordEncoder.encode(password));
            }
        }

        return AdminUserMapper.toResponseDto(adminRepo.save(existingUser));
    }

    @Override
    public void deleteAdminUser(UUID id) {
        AdminUser existingAdminUser = adminRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Admin User with id " + id + " not found")
        );
        adminRepo.delete(existingAdminUser);
    }

}
