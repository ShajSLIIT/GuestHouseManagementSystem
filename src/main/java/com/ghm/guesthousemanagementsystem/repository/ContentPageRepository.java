package com.ghm.guesthousemanagementsystem.repository;

import com.ghm.guesthousemanagementsystem.entity.ContentPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContentPageRepository extends JpaRepository<ContentPage, UUID> {
}
