package com.mountyhub.app.repository;

import com.mountyhub.app.domain.CompetenceMH;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CompetenceMH entity.
 */
public interface CompetenceMHRepository extends JpaRepository<CompetenceMH,Long> {

}
