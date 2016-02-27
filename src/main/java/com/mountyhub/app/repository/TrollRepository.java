package com.mountyhub.app.repository;

import com.mountyhub.app.domain.Troll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Troll entity.
 */
public interface TrollRepository extends JpaRepository<Troll, Long> {
    Optional<Troll> findOneByNumber(Long number);
}
