package com.mountyhub.app.repository;

import com.mountyhub.app.domain.Fly;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Fly entity.
 */
public interface FlyRepository extends JpaRepository<Fly, Long> {
    Optional<Fly> findByNumber(Long number);

    void deleteByNumber(Long number);
}
