package com.mountyhub.app.repository;

import com.mountyhub.app.domain.BonusMalus;

import com.mountyhub.app.web.rest.dto.GlobalEffectDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the BonusMalus entity.
 */
public interface BonusMalusRepository extends JpaRepository<BonusMalus,Long> {

    @Query("SELECT new com.mountyhub.app.web.rest.dto.GlobalEffectDTO(SUM(attack), SUM(attackM), SUM(dodge), SUM(dodgeM), " +
        "SUM(damage), SUM(damageM), SUM(regeneration), SUM(hitPoint), SUM(view), SUM(rm), SUM(mm), SUM(armor), SUM(armorM), SUM(turn)) " +
        "FROM BonusMalus bm WHERE troll_id = :trollId")
    GlobalEffectDTO getGlobalEffect(@Param("trollId") Long trollId);
}
