package com.mountyhub.app.repository;

import com.mountyhub.app.domain.Competence;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Competence entity.
 */
public interface CompetenceRepository extends JpaRepository<Competence,Long> {

}
