package com.mountyhub.app.repository;

import com.mountyhub.app.domain.Gear;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Gear entity.
 */
public interface GearRepository extends JpaRepository<Gear, Long> {
    void deleteByTrollNumber(Long number);
}
