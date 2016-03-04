package com.mountyhub.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mountyhub.app.domain.enumeration.GearType;

/**
 * A Gear.
 */
@Entity
@Table(name = "gear")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Gear implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "number", nullable = false)
    private Long number;
    
    @NotNull
    @Column(name = "weared", nullable = false)
    private Boolean weared;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private GearType type;
    
    @NotNull
    @Column(name = "identified", nullable = false)
    private Boolean identified;
    
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotNull
    @Column(name = "template", nullable = false)
    private String template;
    
    @NotNull
    @Column(name = "description", nullable = false)
    private String description;
    
    @NotNull
    @Column(name = "weight", nullable = false)
    private Float weight;
    
    @NotNull
    @Column(name = "attack", nullable = false)
    private Integer attack;
    
    @NotNull
    @Column(name = "attack_m", nullable = false)
    private Integer attackM;
    
    @NotNull
    @Column(name = "dodge", nullable = false)
    private Integer dodge;
    
    @NotNull
    @Column(name = "dodge_m", nullable = false)
    private Integer dodgeM;
    
    @NotNull
    @Column(name = "damage", nullable = false)
    private Integer damage;
    
    @NotNull
    @Column(name = "damage_m", nullable = false)
    private Integer damageM;
    
    @NotNull
    @Column(name = "regeneration", nullable = false)
    private Integer regeneration;
    
    @NotNull
    @Column(name = "hit_point", nullable = false)
    private Integer hitPoint;
    
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
    @Column(name = "armor_m", nullable = false)
    private Integer armorM;
    
    @NotNull
    @Column(name = "turn", nullable = false)
    private Integer turn;
    
    @NotNull
    @Column(name = "protection", nullable = false)
    private String protection;
    
    @ManyToOne
    @JoinColumn(name = "troll_id")
    private Troll troll;

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

    public Boolean getWeared() {
        return weared;
    }
    
    public void setWeared(Boolean weared) {
        this.weared = weared;
    }

    public GearType getType() {
        return type;
    }
    
    public void setType(GearType type) {
        this.type = type;
    }

    public Boolean getIdentified() {
        return identified;
    }
    
    public void setIdentified(Boolean identified) {
        this.identified = identified;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return template;
    }
    
    public void setTemplate(String template) {
        this.template = template;
    }

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public Float getWeight() {
        return weight;
    }
    
    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Integer getAttack() {
        return attack;
    }
    
    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getAttackM() {
        return attackM;
    }
    
    public void setAttackM(Integer attackM) {
        this.attackM = attackM;
    }

    public Integer getDodge() {
        return dodge;
    }
    
    public void setDodge(Integer dodge) {
        this.dodge = dodge;
    }

    public Integer getDodgeM() {
        return dodgeM;
    }
    
    public void setDodgeM(Integer dodgeM) {
        this.dodgeM = dodgeM;
    }

    public Integer getDamage() {
        return damage;
    }
    
    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public Integer getDamageM() {
        return damageM;
    }
    
    public void setDamageM(Integer damageM) {
        this.damageM = damageM;
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

    public Integer getArmorM() {
        return armorM;
    }
    
    public void setArmorM(Integer armorM) {
        this.armorM = armorM;
    }

    public Integer getTurn() {
        return turn;
    }
    
    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public String getProtection() {
        return protection;
    }
    
    public void setProtection(String protection) {
        this.protection = protection;
    }

    public Troll getTroll() {
        return troll;
    }

    public void setTroll(Troll troll) {
        this.troll = troll;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Gear gear = (Gear) o;
        if(gear.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, gear.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Gear{" +
            "id=" + id +
            ", number='" + number + "'" +
            ", weared='" + weared + "'" +
            ", type='" + type + "'" +
            ", identified='" + identified + "'" +
            ", name='" + name + "'" +
            ", template='" + template + "'" +
            ", description='" + description + "'" +
            ", weight='" + weight + "'" +
            ", attack='" + attack + "'" +
            ", attackM='" + attackM + "'" +
            ", dodge='" + dodge + "'" +
            ", dodgeM='" + dodgeM + "'" +
            ", damage='" + damage + "'" +
            ", damageM='" + damageM + "'" +
            ", regeneration='" + regeneration + "'" +
            ", hitPoint='" + hitPoint + "'" +
            ", view='" + view + "'" +
            ", rm='" + rm + "'" +
            ", mm='" + mm + "'" +
            ", armor='" + armor + "'" +
            ", armorM='" + armorM + "'" +
            ", turn='" + turn + "'" +
            ", protection='" + protection + "'" +
            '}';
    }
}
