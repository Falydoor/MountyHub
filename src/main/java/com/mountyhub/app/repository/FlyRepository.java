package com.mountyhub.app.repository;

import com.mountyhub.app.domain.Fly;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Fly entity.
 */
public interface FlyRepository extends JpaRepository<Fly,Long> {

}
