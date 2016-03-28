package com.mountyhub.app.repository;

import com.mountyhub.app.domain.BonusMalus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BonusMalus entity.
 */
public interface BonusMalusRepository extends JpaRepository<BonusMalus,Long> {

}
