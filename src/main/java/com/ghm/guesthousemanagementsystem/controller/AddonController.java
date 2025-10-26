package com.ghm.guesthousemanagementsystem.controller;


import com.ghm.guesthousemanagementsystem.dto.addon.AddonResponseDTO;
import com.ghm.guesthousemanagementsystem.service.impl.AddonServiceImpl;
import com.ghm.guesthousemanagementsystem.dto.addon.AddonRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/addons")
@RequiredArgsConstructor
public class AddonController {

    private final AddonServiceImpl addonServiceImpl;

    @GetMapping
    public ResponseEntity<List<AddonResponseDTO>> getAllAddons() {
        return ResponseEntity.ok(addonServiceImpl.getAllAddons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddonResponseDTO> getAddonById(@PathVariable UUID id) {
        return ResponseEntity.ok(addonServiceImpl.getAddonById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<AddonResponseDTO> getAddonByName(@PathVariable String name) {
        AddonResponseDTO addon = addonServiceImpl.getAddonByName(name);
        return addon != null ? ResponseEntity.ok(addon) : ResponseEntity.notFound().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<AddonResponseDTO>> getActiveAddons() {
        List<AddonResponseDTO> addons = addonServiceImpl.getActiveAddons();
        return addons.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(addons);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AddonResponseDTO>> searchAddons(@RequestParam String keyword) {
        List<AddonResponseDTO> addons = addonServiceImpl.searchAddons(keyword);
        return ResponseEntity.ok(addons);
    }

    @GetMapping("/search/active")
    public ResponseEntity<List<AddonResponseDTO>> searchActiveAddons(@RequestParam String keyword) {
        List<AddonResponseDTO> addons = addonServiceImpl.searchActiveAddons(keyword);
        return ResponseEntity.ok(addons);
    }

    @PostMapping
    public ResponseEntity<AddonResponseDTO> createAddon(@Valid @RequestBody AddonRequestDTO addonRequestDTO) {
        AddonResponseDTO newAddon = addonServiceImpl.createAddon(addonRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAddon);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddonResponseDTO> updateAddon(
            @PathVariable UUID id,
            @Valid @RequestBody AddonRequestDTO addonRequestDTO) {
        AddonResponseDTO updatedAddon = addonServiceImpl.updateAddon(id, addonRequestDTO);
        return ResponseEntity.ok(updatedAddon);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<AddonResponseDTO> activateAddon(@PathVariable UUID id) {
        AddonResponseDTO activatedAddon = addonServiceImpl.activateAddon(id);
        return ResponseEntity.ok(activatedAddon);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<AddonResponseDTO> deactivateAddon(@PathVariable UUID id) {
        AddonResponseDTO deactivatedAddon = addonServiceImpl.deactivateAddon(id);
        return ResponseEntity.ok(deactivatedAddon);
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteAddon(@PathVariable UUID id) {
        addonServiceImpl.hardDeleteAddon(id);
        return ResponseEntity.ok().build();
    }
}