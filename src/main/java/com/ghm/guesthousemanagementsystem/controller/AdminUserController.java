package com.ghm.guesthousemanagementsystem.controller;

import com.ghm.guesthousemanagementsystem.dto.AdminUserReqDto;
import com.ghm.guesthousemanagementsystem.dto.AdminUserResponseDto;
import com.ghm.guesthousemanagementsystem.entity.AdminUser;
import com.ghm.guesthousemanagementsystem.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin-users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping
    public ResponseEntity<AdminUserResponseDto> createAdminUser(@RequestBody AdminUserReqDto adminUserReqDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminUserService.createAdminUser(adminUserReqDto));
    }

    @GetMapping
    public ResponseEntity<List<AdminUserResponseDto>> getAllAdminUsers() {
        return ResponseEntity.ok(adminUserService.getAllAdminUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUserResponseDto> getAdminUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminUserService.getAdminUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminUserResponseDto> updateAdminUser(@PathVariable UUID id, @RequestBody AdminUserReqDto adminUserReqDto) {

        return ResponseEntity.ok(adminUserService.updateAdminUser(id, adminUserReqDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminUserById(@PathVariable UUID id) {
        adminUserService.deleteAdminUser(id);
        return ResponseEntity.noContent().build();
    }
}
