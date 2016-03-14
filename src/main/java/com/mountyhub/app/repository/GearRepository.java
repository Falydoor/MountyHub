package com.mountyhub.app.repository;

import com.mountyhub.app.domain.Gear;
import com.mountyhub.app.web.rest.dto.GlobalEffectDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Gear entity.
 */
public interface GearRepository extends JpaRepository<Gear, Long> {
    void deleteByNumber(Long number);

    Optional<Gear> findByNumber(Long number);

    @Query("SELECT new com.mountyhub.app.web.rest.dto.GlobalEffectDTO(SUM(attack), SUM(attackM), SUM(dodge), SUM(dodgeM), " +
        "SUM(damage), SUM(damageM), SUM(regeneration), SUM(hitPoint), SUM(view), SUM(rm), SUM(mm), SUM(armor), SUM(armorM), SUM(turn)) " +
        "FROM Gear g WHERE wore IS TRUE AND troll_id = :trollId")
    GlobalEffectDTO getWoreGlobalEffect(@Param("trollId") Long trollId);
}
