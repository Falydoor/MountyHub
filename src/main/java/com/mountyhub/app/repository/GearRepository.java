package com.mountyhub.app.repository;

import com.mountyhub.app.domain.Gear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Gear entity.
 */
public interface GearRepository extends JpaRepository<Gear, Long> {
    void deleteByNumber(Long number);

    Optional<Gear> findByNumber(Long number);
}
