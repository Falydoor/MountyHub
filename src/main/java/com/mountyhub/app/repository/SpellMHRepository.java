package com.mountyhub.app.repository;

import com.mountyhub.app.domain.SpellMH;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SpellMH entity.
 */
public interface SpellMHRepository extends JpaRepository<SpellMH,Long> {

}
