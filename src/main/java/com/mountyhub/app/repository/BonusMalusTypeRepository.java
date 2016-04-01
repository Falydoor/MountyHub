package com.mountyhub.app.repository;

import com.mountyhub.app.domain.BonusMalusType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BonusMalusType entity.
 */
public interface BonusMalusTypeRepository extends JpaRepository<BonusMalusType,Long> {

}
