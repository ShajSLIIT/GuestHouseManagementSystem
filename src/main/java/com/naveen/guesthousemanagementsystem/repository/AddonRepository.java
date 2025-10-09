package com.naveen.guesthousemanagementsystem.repository;

import com.naveen.guesthousemanagementsystem.entity.Addon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddonRepository extends JpaRepository<Addon, UUID> {

    /** Find addon by name */
    Optional<Addon> findByName(String name);

    /** Find all active addons */
    List<Addon> findByActiveTrue();

    /** Check if addon with given name exists */
    boolean existsByName(String name);

    /** Check if addon with given name exists excluding specific ID */
    boolean existsByNameAndIdNot(String name, UUID id);

    /** Search addons by name keyword */
    @Query("SELECT a FROM Addon a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Addon> searchByName(@Param("keyword") String keyword);

    /** Search active addons by name keyword */
    @Query("SELECT a FROM Addon a WHERE a.active = true AND LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Addon> searchActiveByName(@Param("keyword") String keyword);
}