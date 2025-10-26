package com.ghm.guesthousemanagementsystem.repository;



import com.ghm.guesthousemanagementsystem.entity.Addon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddonRepository extends JpaRepository<Addon, UUID> {
    List<Addon> findByActiveTrue();

    boolean existsByName(String name);

    Optional<Addon> findByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);

    @Query("SELECT a FROM Addon a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Addon> searchByName(@Param("keyword") String keyword);

    @Query("SELECT a FROM Addon a WHERE a.active = true AND LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Addon> searchActiveByName(@Param("keyword") String keyword);
}