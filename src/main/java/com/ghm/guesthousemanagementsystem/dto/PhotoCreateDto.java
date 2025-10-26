package com.ghm.guesthousemanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoCreateDto {

    private UUID propertyId;
    private UUID roomId;
    private String caption;
}
