package com.mountyhub.app.repository;

import com.mountyhub.app.domain.ScriptCall;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ScriptCall entity.
 */
public interface ScriptCallRepository extends JpaRepository<ScriptCall,Long> {

}
