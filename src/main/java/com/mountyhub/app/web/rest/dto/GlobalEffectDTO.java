package com.mountyhub.app.web.rest.dto;

import com.mountyhub.app.domain.enumeration.FlyType;

import java.util.Map;

/**
 * Created by Theo on 3/11/16.
 */
public class GlobalEffectDTO {
    private Long attack;
    private Long attackM;
    private Long dodge;
    private Long dodgeM;
    private Long damage;
    private Long damageM;
    private Long regeneration;
    private Long hitPoint;
    private Long view;
    private Long rm;
    private Long mm;
    private Long armor;
    private Long armorM;
    private Long turn;

    public GlobalEffectDTO(Long attack, Long attackM, Long dodge, Long dodgeM, Long damage, Long damageM, Long regeneration, Long hitPoint, Long view, Long rm, Long mm, Long armor, Long armorM, Long turn) {
        this.attack = attack;
        this.attackM = attackM;
        this.dodge = dodge;
        this.dodgeM = dodgeM;
        this.damage = damage;
        this.damageM = damageM;
        this.regeneration = regeneration;
        this.hitPoint = hitPoint;
        this.view = view;
        this.rm = rm;
        this.mm = mm;
        this.armor = armor;
        this.armorM = armorM;
        this.turn = turn;
    }

    public GlobalEffectDTO(Map<FlyType, Long> fliesByType) {
        this.attack = fliesByType.getOrDefault(FlyType.CROBATE, 0L);
        this.attackM = 0L;
        this.dodge = fliesByType.getOrDefault(FlyType.VERTIE, 0L);
        this.dodgeM = 0L;
        this.damage = fliesByType.getOrDefault(FlyType.NABOLISANT, 0L);
        this.damageM = 0L;
        this.regeneration = fliesByType.getOrDefault(FlyType.MIEL, 0L);
        this.hitPoint = fliesByType.getOrDefault(FlyType.TELAITE, 0L) * 5;
        this.view = fliesByType.getOrDefault(FlyType.LUNETTES, 0L);
        this.rm = 0L;
        this.mm = 0L;
        this.armor = fliesByType.getOrDefault(FlyType.XIDANT, 0L);
        this.armorM = 0L;
        this.turn = fliesByType.getOrDefault(FlyType.RIVATANT, 0L) * -20;
    }

    public Long getAttack() {
        return attack;
    }

    public void setAttack(Long attack) {
        this.attack = attack;
    }

    public Long getAttackM() {
        return attackM;
    }

    public void setAttackM(Long attackM) {
        this.attackM = attackM;
    }

    public Long getDodge() {
        return dodge;
    }

    public void setDodge(Long dodge) {
        this.dodge = dodge;
    }

    public Long getDodgeM() {
        return dodgeM;
    }

    public void setDodgeM(Long dodgeM) {
        this.dodgeM = dodgeM;
    }

    public Long getDamage() {
        return damage;
    }

    public void setDamage(Long damage) {
        this.damage = damage;
    }

    public Long getDamageM() {
        return damageM;
    }

    public void setDamageM(Long damageM) {
        this.damageM = damageM;
    }

    public Long getRegeneration() {
        return regeneration;
    }

    public void setRegeneration(Long regeneration) {
        this.regeneration = regeneration;
    }

    public Long getHitPoint() {
        return hitPoint;
    }

    public void setHitPoint(Long hitPoint) {
        this.hitPoint = hitPoint;
    }

    public Long getView() {
        return view;
    }

    public void setView(Long view) {
        this.view = view;
    }

    public Long getRm() {
        return rm;
    }

    public void setRm(Long rm) {
        this.rm = rm;
    }

    public Long getMm() {
        return mm;
    }

    public void setMm(Long mm) {
        this.mm = mm;
    }

    public Long getArmor() {
        return armor;
    }

    public void setArmor(Long armor) {
        this.armor = armor;
    }

    public Long getArmorM() {
        return armorM;
    }

    public void setArmorM(Long armorM) {
        this.armorM = armorM;
    }

    public Long getTurn() {
        return turn;
    }

    public void setTurn(Long turn) {
        this.turn = turn;
    }
}
