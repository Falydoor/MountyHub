package com.mountyhub.app.repository;

import com.mountyhub.app.domain.SpellMH;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the SpellMH entity.
 */
public interface SpellMHRepository extends JpaRepository<SpellMH, Long> {
    Optional<SpellMH> findByNumber(Long number);
}
