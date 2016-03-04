package com.mountyhub.app.repository;

import com.mountyhub.app.domain.Gear;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Gear entity.
 */
public interface GearRepository extends JpaRepository<Gear,Long> {

}
