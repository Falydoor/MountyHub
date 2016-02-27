package com.mountyhub.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mountyhub.app.domain.enumeration.TrollRace;

/**
 * A Troll.
 */
@Entity
@Table(name = "troll")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Troll implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "number", nullable = false)
    private Long number;
    
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "race", nullable = false)
    private TrollRace race;
    
    @NotNull
    @Column(name = "birth_date", nullable = false)
    private ZonedDateTime birthDate;
    
    @NotNull
    @Column(name = "x", nullable = false)
    private Integer x;
    
    @NotNull
    @Column(name = "y", nullable = false)
    private Integer y;
    
    @NotNull
    @Column(name = "z", nullable = false)
    private Integer z;
    
    @NotNull
    @Column(name = "attack", nullable = false)
    private Integer attack;
    
    @NotNull
    @Column(name = "dodge", nullable = false)
    private Integer dodge;
    
    @NotNull
    @Column(name = "damage", nullable = false)
    private Integer damage;
    
    @NotNull
    @Column(name = "regeneration", nullable = false)
    private Integer regeneration;
    
    @NotNull
    @Column(name = "hit_point", nullable = false)
    private Integer hitPoint;
    
    @NotNull
    @Column(name = "current_hit_point", nullable = false)
    private Integer currentHitPoint;
    
    @NotNull
    @Column(name = "view", nullable = false)
    private Integer view;
    
    @NotNull
    @Column(name = "rm", nullable = false)
    private Integer rm;
    
    @NotNull
    @Column(name = "mm", nullable = false)
    private Integer mm;
    
    @NotNull
    @Column(name = "armor", nullable = false)
    private Integer armor;
    
    @NotNull
    @Column(name = "turn", nullable = false)
    private Integer turn;
    
    @NotNull
    @Column(name = "weight", nullable = false)
    private Integer weight;
    
    @NotNull
    @Column(name = "focus", nullable = false)
    private Integer focus;
    
    @NotNull
    @Column(name = "attack_p", nullable = false)
    private Integer attackP;
    
    @NotNull
    @Column(name = "dodge_p", nullable = false)
    private Integer dodgeP;
    
    @NotNull
    @Column(name = "damage_p", nullable = false)
    private Integer damageP;
    
    @NotNull
    @Column(name = "regeneration_p", nullable = false)
    private Integer regenerationP;
    
    @NotNull
    @Column(name = "hit_point_p", nullable = false)
    private Integer hitPointP;
    
    @NotNull
    @Column(name = "attack_m", nullable = false)
    private Integer attackM;
    
    @NotNull
    @Column(name = "dodge_m", nullable = false)
    private Integer dodgeM;
    
    @NotNull
    @Column(name = "damage_m", nullable = false)
    private Integer damageM;
    
    @NotNull
    @Column(name = "regeneration_m", nullable = false)
    private Integer regenerationM;
    
    @NotNull
    @Column(name = "hit_point_m", nullable = false)
    private Integer hitPointM;
    
    @NotNull
    @Column(name = "view_p", nullable = false)
    private Integer viewP;
    
    @NotNull
    @Column(name = "rm_p", nullable = false)
    private Integer rmP;
    
    @NotNull
    @Column(name = "mm_p", nullable = false)
    private Integer mmP;
    
    @NotNull
    @Column(name = "armor_p", nullable = false)
    private Integer armorP;
    
    @NotNull
    @Column(name = "weight_p", nullable = false)
    private Integer weightP;
    
    @NotNull
    @Column(name = "view_m", nullable = false)
    private Integer viewM;
    
    @NotNull
    @Column(name = "rm_m", nullable = false)
    private Integer rmM;
    
    @NotNull
    @Column(name = "mm_m", nullable = false)
    private Integer mmM;
    
    @NotNull
    @Column(name = "armor_m", nullable = false)
    private Integer armorM;
    
    @NotNull
    @Column(name = "weight_m", nullable = false)
    private Integer weightM;
    
    @OneToOne(mappedBy = "troll")
    @JsonIgnore
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }
    
    public void setNumber(Long number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public TrollRace getRace() {
        return race;
    }
    
    public void setRace(TrollRace race) {
        this.race = race;
    }

    public ZonedDateTime getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(ZonedDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getX() {
        return x;
    }
    
    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }
    
    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getZ() {
        return z;
    }
    
    public void setZ(Integer z) {
        this.z = z;
    }

    public Integer getAttack() {
        return attack;
    }
    
    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getDodge() {
        return dodge;
    }
    
    public void setDodge(Integer dodge) {
        this.dodge = dodge;
    }

    public Integer getDamage() {
        return damage;
    }
    
    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public Integer getRegeneration() {
        return regeneration;
    }
    
    public void setRegeneration(Integer regeneration) {
        this.regeneration = regeneration;
    }

    public Integer getHitPoint() {
        return hitPoint;
    }
    
    public void setHitPoint(Integer hitPoint) {
        this.hitPoint = hitPoint;
    }

    public Integer getCurrentHitPoint() {
        return currentHitPoint;
    }
    
    public void setCurrentHitPoint(Integer currentHitPoint) {
        this.currentHitPoint = currentHitPoint;
    }

    public Integer getView() {
        return view;
    }
    
    public void setView(Integer view) {
        this.view = view;
    }

    public Integer getRm() {
        return rm;
    }
    
    public void setRm(Integer rm) {
        this.rm = rm;
    }

    public Integer getMm() {
        return mm;
    }
    
    public void setMm(Integer mm) {
        this.mm = mm;
    }

    public Integer getArmor() {
        return armor;
    }
    
    public void setArmor(Integer armor) {
        this.armor = armor;
    }

    public Integer getTurn() {
        return turn;
    }
    
    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public Integer getWeight() {
        return weight;
    }
    
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getFocus() {
        return focus;
    }
    
    public void setFocus(Integer focus) {
        this.focus = focus;
    }

    public Integer getAttackP() {
        return attackP;
    }
    
    public void setAttackP(Integer attackP) {
        this.attackP = attackP;
    }

    public Integer getDodgeP() {
        return dodgeP;
    }
    
    public void setDodgeP(Integer dodgeP) {
        this.dodgeP = dodgeP;
    }

    public Integer getDamageP() {
        return damageP;
    }
    
    public void setDamageP(Integer damageP) {
        this.damageP = damageP;
    }

    public Integer getRegenerationP() {
        return regenerationP;
    }
    
    public void setRegenerationP(Integer regenerationP) {
        this.regenerationP = regenerationP;
    }

    public Integer getHitPointP() {
        return hitPointP;
    }
    
    public void setHitPointP(Integer hitPointP) {
        this.hitPointP = hitPointP;
    }

    public Integer getAttackM() {
        return attackM;
    }
    
    public void setAttackM(Integer attackM) {
        this.attackM = attackM;
    }

    public Integer getDodgeM() {
        return dodgeM;
    }
    
    public void setDodgeM(Integer dodgeM) {
        this.dodgeM = dodgeM;
    }

    public Integer getDamageM() {
        return damageM;
    }
    
    public void setDamageM(Integer damageM) {
        this.damageM = damageM;
    }

    public Integer getRegenerationM() {
        return regenerationM;
    }
    
    public void setRegenerationM(Integer regenerationM) {
        this.regenerationM = regenerationM;
    }

    public Integer getHitPointM() {
        return hitPointM;
    }
    
    public void setHitPointM(Integer hitPointM) {
        this.hitPointM = hitPointM;
    }

    public Integer getViewP() {
        return viewP;
    }
    
    public void setViewP(Integer viewP) {
        this.viewP = viewP;
    }

    public Integer getRmP() {
        return rmP;
    }
    
    public void setRmP(Integer rmP) {
        this.rmP = rmP;
    }

    public Integer getMmP() {
        return mmP;
    }
    
    public void setMmP(Integer mmP) {
        this.mmP = mmP;
    }

    public Integer getArmorP() {
        return armorP;
    }
    
    public void setArmorP(Integer armorP) {
        this.armorP = armorP;
    }

    public Integer getWeightP() {
        return weightP;
    }
    
    public void setWeightP(Integer weightP) {
        this.weightP = weightP;
    }

    public Integer getViewM() {
        return viewM;
    }
    
    public void setViewM(Integer viewM) {
        this.viewM = viewM;
    }

    public Integer getRmM() {
        return rmM;
    }
    
    public void setRmM(Integer rmM) {
        this.rmM = rmM;
    }

    public Integer getMmM() {
        return mmM;
    }
    
    public void setMmM(Integer mmM) {
        this.mmM = mmM;
    }

    public Integer getArmorM() {
        return armorM;
    }
    
    public void setArmorM(Integer armorM) {
        this.armorM = armorM;
    }

    public Integer getWeightM() {
        return weightM;
    }
    
    public void setWeightM(Integer weightM) {
        this.weightM = weightM;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Troll troll = (Troll) o;
        if(troll.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, troll.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Troll{" +
            "id=" + id +
            ", number='" + number + "'" +
            ", name='" + name + "'" +
            ", race='" + race + "'" +
            ", birthDate='" + birthDate + "'" +
            ", x='" + x + "'" +
            ", y='" + y + "'" +
            ", z='" + z + "'" +
            ", attack='" + attack + "'" +
            ", dodge='" + dodge + "'" +
            ", damage='" + damage + "'" +
            ", regeneration='" + regeneration + "'" +
            ", hitPoint='" + hitPoint + "'" +
            ", currentHitPoint='" + currentHitPoint + "'" +
            ", view='" + view + "'" +
            ", rm='" + rm + "'" +
            ", mm='" + mm + "'" +
            ", armor='" + armor + "'" +
            ", turn='" + turn + "'" +
            ", weight='" + weight + "'" +
            ", focus='" + focus + "'" +
            ", attackP='" + attackP + "'" +
            ", dodgeP='" + dodgeP + "'" +
            ", damageP='" + damageP + "'" +
            ", regenerationP='" + regenerationP + "'" +
            ", hitPointP='" + hitPointP + "'" +
            ", attackM='" + attackM + "'" +
            ", dodgeM='" + dodgeM + "'" +
            ", damageM='" + damageM + "'" +
            ", regenerationM='" + regenerationM + "'" +
            ", hitPointM='" + hitPointM + "'" +
            ", viewP='" + viewP + "'" +
            ", rmP='" + rmP + "'" +
            ", mmP='" + mmP + "'" +
            ", armorP='" + armorP + "'" +
            ", weightP='" + weightP + "'" +
            ", viewM='" + viewM + "'" +
            ", rmM='" + rmM + "'" +
            ", mmM='" + mmM + "'" +
            ", armorM='" + armorM + "'" +
            ", weightM='" + weightM + "'" +
            '}';
    }
}
