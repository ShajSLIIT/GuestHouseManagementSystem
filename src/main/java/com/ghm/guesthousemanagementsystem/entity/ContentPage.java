package com.ghm.guesthousemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "content_page")
public class ContentPage {

    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, length = 100, unique = true)
    private String slug;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}
