package com.mountyhub.app.repository;

import com.mountyhub.app.domain.Spell;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Spell entity.
 */
public interface SpellRepository extends JpaRepository<Spell,Long> {

}
