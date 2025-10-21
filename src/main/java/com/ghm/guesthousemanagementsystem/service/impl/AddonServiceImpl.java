package com.naveen.guesthousemanagementsystem.service.impl;


import com.naveen.guesthousemanagementsystem.dto.addon.AddonRequestDTO;
import com.naveen.guesthousemanagementsystem.dto.addon.AddonResponseDTO;
import com.naveen.guesthousemanagementsystem.entity.Addon;
import com.naveen.guesthousemanagementsystem.exeption.DuplicateResourceException;
import com.naveen.guesthousemanagementsystem.exeption.ResourceNotFoundException;
import com.naveen.guesthousemanagementsystem.mapper.AddonMapper;
import com.naveen.guesthousemanagementsystem.repository.AddonRepository;
import com.naveen.guesthousemanagementsystem.service.AddonService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddonServiceImpl implements AddonService {
    private final AddonRepository addonRepository;
    private final AddonMapper addonMapper;

    @Override
    public Addon findAddonEntityById(UUID id) {  // Get Addon entity by ID (for internal use by BookingAddonService)
        return addonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Addon not found with id: " + id));
    }

    // Your existing methods remain unchanged below...
    @Transactional(readOnly = true)
    @Override
    public List<AddonResponseDTO> getAllAddons() {  // Get all addons from the system
        return addonRepository.findAll().stream()
                .map(addonMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<AddonResponseDTO> getActiveAddons() {  // Get all active addons only
        return addonRepository.findByActiveTrue().stream()
                .map(addonMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public AddonResponseDTO getAddonById(UUID id) {  // Get addon by ID
        Addon addon = addonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Addon not found with id: " + id));
        return addonMapper.toResponseDTO(addon);
    }

    @Transactional(readOnly = true)
    @Override
    public AddonResponseDTO getAddonByName(String name) {  // Get addon by name
        return addonRepository.findByName(name.trim())
                .map(addonMapper::toResponseDTO)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AddonResponseDTO> searchAddons(String keyword) {  // Search addons by keyword in name
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new ValidationException("Search keyword cannot be empty");
        }
        return addonRepository.searchByName(keyword.trim()).stream()
                .map(addonMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<AddonResponseDTO> searchActiveAddons(String keyword) {  // Search active addons by keyword in name
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new ValidationException("Search keyword cannot be empty");
        }
        return addonRepository.searchActiveByName(keyword.trim()).stream()
                .map(addonMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    @Override
    public AddonResponseDTO createAddon(AddonRequestDTO addonRequestDTO) {  // Create a new addon
        if (addonRepository.existsByName(addonRequestDTO.getName().trim())) {
            throw new DuplicateResourceException("Addon with name '" + addonRequestDTO.getName() + "' already exists");
        }
        Addon newAddon = AddonMapper.toEntity(addonRequestDTO);
        Addon savedAddon = addonRepository.save(newAddon);
        return addonMapper.toResponseDTO(savedAddon);
    }

    @Override
    public AddonResponseDTO updateAddon(UUID id, AddonRequestDTO addonRequestDTO) {  // Update an existing addon
        Addon existingAddon = addonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Addon not found with id: " + id));
        if (addonRepository.existsByNameAndIdNot(addonRequestDTO.getName().trim(), id)) {
            throw new DuplicateResourceException("Addon with name '" + addonRequestDTO.getName() + "' already exists");
        }
        AddonMapper.updateEntityFromDTO(addonRequestDTO, existingAddon);
        Addon updatedAddon = addonRepository.save(existingAddon);
        return addonMapper.toResponseDTO(updatedAddon);
    }

    @Override
    public AddonResponseDTO activateAddon(UUID id) {  // Activate an addon
        Addon addon = addonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Addon not found with id: " + id));
        addon.setActive(true);
        Addon activatedAddon = addonRepository.save(addon);
        return addonMapper.toResponseDTO(activatedAddon);
    }

    @Override
    public AddonResponseDTO deactivateAddon(UUID id) {  // Deactivate an addon
        Addon addon = addonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Addon not found with id: " + id));
        addon.setActive(false);
        Addon deactivatedAddon = addonRepository.save(addon);
        return addonMapper.toResponseDTO(deactivatedAddon);
    }

    @Override
    public void hardDeleteAddon(UUID id) {  // Permanently delete an addon from database
        if (!addonRepository.existsById(id)) {
            throw new ResourceNotFoundException("Addon not found with id: " + id);
        }
        addonRepository.deleteById(id);
    }
}