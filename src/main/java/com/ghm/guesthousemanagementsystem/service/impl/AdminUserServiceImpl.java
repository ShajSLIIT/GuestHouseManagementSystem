package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.dto.AdminUserReqDto;
import com.ghm.guesthousemanagementsystem.dto.AdminUserResponseDto;
import com.ghm.guesthousemanagementsystem.entity.AdminUser;
import com.ghm.guesthousemanagementsystem.exception.ResourceNotFoundException;
import com.ghm.guesthousemanagementsystem.mapper.AdminUserMapper;
import com.ghm.guesthousemanagementsystem.repository.AdminUserRepository;
import com.ghm.guesthousemanagementsystem.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserRepository adminRepo;
    @Override
    public AdminUserResponseDto createAdminUser(AdminUserReqDto adminReqDto) {
        if (adminRepo.findByEmail(adminReqDto.getEmail()).isPresent()) {
           throw new IllegalArgumentException("Email already in use: " + adminReqDto.getEmail());
        }
        AdminUser adminUser = AdminUserMapper.toEntity(adminReqDto);
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

        existingAdminUser.setName(adminUserReqDto.getName());
        existingAdminUser.setEmail(adminUserReqDto.getEmail());
        existingAdminUser.setPasswordHash(adminUserReqDto.getPassword());

        return AdminUserMapper.toResponseDto(adminRepo.save(existingAdminUser));
    }

    @Override
    public void deleteAdminUser(UUID id) {
        AdminUser existingAdminUser = adminRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Admin User with id " + id + " not found")
        );
        adminRepo.delete(existingAdminUser);
    }

}
