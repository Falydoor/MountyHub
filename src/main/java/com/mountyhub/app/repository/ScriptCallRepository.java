package com.mountyhub.app.repository;

import com.mountyhub.app.domain.ScriptCall;
import com.mountyhub.app.domain.enumeration.ScriptType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;

/**
 * Spring Data JPA repository for the ScriptCall entity.
 */
public interface ScriptCallRepository extends JpaRepository<ScriptCall, Long> {
    Long countByTrollNumberAndTypeAndSuccessfulTrueAndDateCalledAfter(Long number, ScriptType type, ZonedDateTime dateCalled);
}
