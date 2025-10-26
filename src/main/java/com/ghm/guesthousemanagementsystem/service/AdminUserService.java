package com.ghm.guesthousemanagementsystem.service;

import com.ghm.guesthousemanagementsystem.dto.AdminUserReqDto;
import com.ghm.guesthousemanagementsystem.dto.AdminUserResponseDto;
import com.ghm.guesthousemanagementsystem.entity.AdminUser;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AdminUserService {
    AdminUserResponseDto createAdminUser(AdminUserReqDto adminReqDto);
    List<AdminUserResponseDto> getAllAdminUsers();
    AdminUserResponseDto getAdminUserById(UUID id);
    AdminUserResponseDto updateAdminUser(UUID id, AdminUserReqDto adminUserReqDto);
    AdminUserResponseDto patchAdminUser(UUID id, Map<String, Object> newDetails);
    void deleteAdminUser(UUID id);
}
