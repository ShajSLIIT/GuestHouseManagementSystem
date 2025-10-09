package com.naveen.guesthousemanagementsystem.controller;

import com.naveen.guesthousemanagementsystem.dto.addon.AddonResponseDTO;
import com.naveen.guesthousemanagementsystem.dto.addon.AddonRequestDTO;
import com.naveen.guesthousemanagementsystem.service.AddonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/addons")
@RequiredArgsConstructor
@CrossOrigin(origins = "${app.cors.allowed-origins:http://localhost:3000}")
public class AddonController {

    private final AddonService addonService;

    /** Get all addons */
    @GetMapping
    public ResponseEntity<List<AddonResponseDTO>> getAllAddons() {
        return ResponseEntity.ok(addonService.getAllAddons());
    }

    /** Get addon by ID */
    @GetMapping("/{id}")
    public ResponseEntity<AddonResponseDTO> getAddonById(@PathVariable UUID id) {
        return ResponseEntity.ok(addonService.getAddonById(id));
    }

    /** Get addon by name */
    @GetMapping("/name/{name}")
    public ResponseEntity<AddonResponseDTO> getAddonByName(@PathVariable String name) {
        AddonResponseDTO addon = addonService.getAddonByName(name);
        return addon != null ? ResponseEntity.ok(addon) : ResponseEntity.notFound().build();
    }

    /** Get all active addons */
    @GetMapping("/active")
    public ResponseEntity<List<AddonResponseDTO>> getActiveAddons() {
        List<AddonResponseDTO> addons = addonService.getActiveAddons();
        return addons.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(addons);
    }

    /** Search addons by keyword */
    @GetMapping("/search")
    public ResponseEntity<List<AddonResponseDTO>> searchAddons(@RequestParam String keyword) {
        List<AddonResponseDTO> addons = addonService.searchAddons(keyword);
        return ResponseEntity.ok(addons);
    }

    /** Search active addons by keyword */
    @GetMapping("/search/active")
    public ResponseEntity<List<AddonResponseDTO>> searchActiveAddons(@RequestParam String keyword) {
        List<AddonResponseDTO> addons = addonService.searchActiveAddons(keyword);
        return ResponseEntity.ok(addons);
    }

    /** Create new addon */
    @PostMapping
    public ResponseEntity<AddonResponseDTO> createAddon(@Valid @RequestBody AddonRequestDTO addonRequestDTO) {
        AddonResponseDTO newAddon = addonService.createAddon(addonRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAddon);
    }

    /** Update existing addon */
    @PutMapping("/{id}")
    public ResponseEntity<AddonResponseDTO> updateAddon(
            @PathVariable UUID id,
            @Valid @RequestBody AddonRequestDTO addonRequestDTO) {
        AddonResponseDTO updatedAddon = addonService.updateAddon(id, addonRequestDTO);
        return ResponseEntity.ok(updatedAddon);
    }

    /** Activate addon */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<AddonResponseDTO> activateAddon(@PathVariable UUID id) {
        AddonResponseDTO activatedAddon = addonService.activateAddon(id);
        return ResponseEntity.ok(activatedAddon);
    }

    /** Deactivate addon */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<AddonResponseDTO> deactivateAddon(@PathVariable UUID id) {
        AddonResponseDTO deactivatedAddon = addonService.deactivateAddon(id);
        return ResponseEntity.ok(deactivatedAddon);
    }

    /** Hard delete addon */
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteAddon(@PathVariable UUID id) {
        addonService.hardDeleteAddon(id);
        return ResponseEntity.ok().build();
    }
}