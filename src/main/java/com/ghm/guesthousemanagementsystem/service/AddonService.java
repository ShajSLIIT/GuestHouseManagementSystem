package com.ghm.guesthousemanagementsystem.service;



import com.ghm.guesthousemanagementsystem.dto.addon.AddonRequestDTO;
import com.ghm.guesthousemanagementsystem.dto.addon.AddonResponseDTO;
import com.ghm.guesthousemanagementsystem.entity.Addon;

import java.util.List;
import java.util.UUID;

public interface AddonService {

    Addon findAddonEntityById(UUID id);

    List<AddonResponseDTO> getAllAddons();

    List<AddonResponseDTO> getActiveAddons();

    AddonResponseDTO getAddonById(UUID id);

    AddonResponseDTO getAddonByName(String name);

    List<AddonResponseDTO> searchAddons(String keyword);

    List<AddonResponseDTO> searchActiveAddons(String keyword);

    AddonResponseDTO createAddon(AddonRequestDTO addonRequestDTO);

    AddonResponseDTO updateAddon(UUID id, AddonRequestDTO addonRequestDTO);

    AddonResponseDTO activateAddon(UUID id);

    AddonResponseDTO deactivateAddon(UUID id);

    void hardDeleteAddon(UUID id);
}
