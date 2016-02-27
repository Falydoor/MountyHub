package com.mountyhub.app.repository;

import com.mountyhub.app.domain.Troll;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Troll entity.
 */
public interface TrollRepository extends JpaRepository<Troll,Long> {

}
