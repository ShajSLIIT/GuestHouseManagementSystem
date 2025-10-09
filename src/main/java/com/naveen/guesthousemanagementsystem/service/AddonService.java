package com.naveen.guesthousemanagementsystem.service;

import com.naveen.guesthousemanagementsystem.dto.addon.AddonRequestDTO;
import com.naveen.guesthousemanagementsystem.dto.addon.AddonResponseDTO;
import com.naveen.guesthousemanagementsystem.entity.Addon;
import com.naveen.guesthousemanagementsystem.mapper.AddonMapper;
import com.naveen.guesthousemanagementsystem.repository.AddonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AddonService {

    private final AddonRepository addonRepository;
    private final AddonMapper addonMapper;

    /** Get all addons */
    @Transactional(readOnly = true)
    public List<AddonResponseDTO> getAllAddons() {
        return addonRepository.findAll().stream()
                .map(addonMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /** Get all active addons */
    @Transactional(readOnly = true)
    public List<AddonResponseDTO> getActiveAddons() {
        return addonRepository.findByActiveTrue().stream()
                .map(addonMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /** Get addon by ID */
    @Transactional(readOnly = true)
    public AddonResponseDTO getAddonById(UUID id) {
        Addon addon = addonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Addon not found with id: " + id));
        return addonMapper.toResponseDTO(addon);
    }

    /** Get addon by name */
    @Transactional(readOnly = true)
    public AddonResponseDTO getAddonByName(String name) {
        return addonRepository.findByName(name.trim())
                .map(addonMapper::toResponseDTO)
                .orElse(null);
    }

    /** Search addons by keyword */
    @Transactional(readOnly = true)
    public List<AddonResponseDTO> searchAddons(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be empty");
        }
        return addonRepository.searchByName(keyword.trim()).stream()
                .map(addonMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /** Search active addons by keyword */
    @Transactional(readOnly = true)
    public List<AddonResponseDTO> searchActiveAddons(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be empty");
        }
        return addonRepository.searchActiveByName(keyword.trim()).stream()
                .map(addonMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /** Create new addon */
    public AddonResponseDTO createAddon(AddonRequestDTO addonRequestDTO) {
        if (addonRepository.existsByName(addonRequestDTO.getName().trim())) {
            throw new RuntimeException("Addon with name '" + addonRequestDTO.getName() + "' already exists");
        }

        Addon newAddon = addonMapper.toEntity(addonRequestDTO);
        Addon savedAddon = addonRepository.save(newAddon);
        return addonMapper.toResponseDTO(savedAddon);
    }

    /** Update existing addon */
    public AddonResponseDTO updateAddon(UUID id, AddonRequestDTO addonRequestDTO) {
        Addon existingAddon = addonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Addon not found with id: " + id));

        if (addonRepository.existsByNameAndIdNot(addonRequestDTO.getName().trim(), id)) {
            throw new RuntimeException("Addon with name '" + addonRequestDTO.getName() + "' already exists");
        }

        addonMapper.updateEntityFromDTO(addonRequestDTO, existingAddon);
        Addon updatedAddon = addonRepository.save(existingAddon);
        return addonMapper.toResponseDTO(updatedAddon);
    }

    /** Activate addon */
    public AddonResponseDTO activateAddon(UUID id) {
        Addon addon = addonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Addon not found with id: " + id));
        addon.setActive(true);
        Addon activatedAddon = addonRepository.save(addon);
        return addonMapper.toResponseDTO(activatedAddon);
    }

    /** Deactivate addon */
    public AddonResponseDTO deactivateAddon(UUID id) {
        Addon addon = addonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Addon not found with id: " + id));
        addon.setActive(false);
        Addon deactivatedAddon = addonRepository.save(addon);
        return addonMapper.toResponseDTO(deactivatedAddon);
    }

    /** Hard delete addon (permanent) */
    public void hardDeleteAddon(UUID id) {
        if (!addonRepository.existsById(id)) {
            throw new RuntimeException("Addon not found with id: " + id);
        }
        addonRepository.deleteById(id);
    }
}