package com.mountyhub.app.repository;

import com.mountyhub.app.domain.BonusMalusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the BonusMalusType entity.
 */
public interface BonusMalusTypeRepository extends JpaRepository<BonusMalusType, Long> {
    Optional<BonusMalusType> findByName(String name);
}
