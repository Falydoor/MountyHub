package com.mountyhub.app.repository;

import com.mountyhub.app.domain.CompetenceMH;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the CompetenceMH entity.
 */
public interface CompetenceMHRepository extends JpaRepository<CompetenceMH, Long> {
    Optional<CompetenceMH> findByNumber(Long number);
}
