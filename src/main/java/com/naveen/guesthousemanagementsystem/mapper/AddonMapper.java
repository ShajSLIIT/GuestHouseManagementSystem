package com.naveen.guesthousemanagementsystem.mapper;

import com.naveen.guesthousemanagementsystem.dto.addon.AddonRequestDTO;
import com.naveen.guesthousemanagementsystem.dto.addon.AddonResponseDTO;
import com.naveen.guesthousemanagementsystem.entity.Addon;
import org.springframework.stereotype.*;

@Component
public class AddonMapper {

    /** Convert AddonRequestDTO to Addon entity */
    public Addon toEntity(AddonRequestDTO requestDTO) {
        if (requestDTO == null) return null;

        return Addon.builder()
                .name(requestDTO.getName().trim())
                .description(requestDTO.getDescription() != null ? requestDTO.getDescription().trim() : null)
                .price(requestDTO.getPrice())
                .active(requestDTO.getActive() != null ? requestDTO.getActive() : true)
                .build();
    }

    /** Convert Addon entity to AddonResponseDTO */
    public AddonResponseDTO toResponseDTO(Addon addon) {
        if (addon == null) return null;

        AddonResponseDTO dto = new AddonResponseDTO();
        dto.setId(addon.getId());
        dto.setName(addon.getName());
        dto.setDescription(addon.getDescription());
        dto.setPrice(addon.getPrice());
        dto.setActive(addon.getActive());
        dto.setCreatedAt(addon.getCreatedAt());
        dto.setUpdatedAt(addon.getUpdatedAt());
        return dto;
    }

    /** Update Addon entity from AddonRequestDTO */
    public void updateEntityFromDTO(AddonRequestDTO requestDTO, Addon addon) {
        if (requestDTO == null || addon == null) return;

        addon.setName(requestDTO.getName().trim());
        addon.setDescription(requestDTO.getDescription() != null ? requestDTO.getDescription().trim() : null);
        addon.setPrice(requestDTO.getPrice());
        if (requestDTO.getActive() != null) {
            addon.setActive(requestDTO.getActive());
        }
    }
}