package com.ghm.guesthousemanagementsystem.mapper;

import com.ghm.guesthousemanagementsystem.dto.AdminUserReqDto;
import com.ghm.guesthousemanagementsystem.dto.AdminUserResponseDto;
import com.ghm.guesthousemanagementsystem.entity.AdminUser;

public class AdminUserMapper {

    public static AdminUser toEntity(AdminUserReqDto userReqDto) {
        AdminUser adminUser = new AdminUser();
        adminUser.setName(userReqDto.getName());
        adminUser.setEmail(userReqDto.getEmail());
        adminUser.setPasswordHash(userReqDto.getPassword());
        return adminUser;
    }

    public static AdminUserResponseDto toResponseDto(AdminUser adminUser) {
        return new AdminUserResponseDto(
                adminUser.getId(),
                adminUser.getName(),
                adminUser.getEmail(),
                adminUser.getLastLogin()
        );
    }
}
