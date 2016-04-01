package com.mountyhub.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mountyhub.app.domain.enumeration.EffectType;

/**
 * A BonusMalusType.
 */
@Entity
@Table(name = "bonus_malus_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BonusMalusType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private EffectType type;
    
    @OneToMany(mappedBy = "type")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BonusMalus> bonusMaluss = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public EffectType getType() {
        return type;
    }
    
    public void setType(EffectType type) {
        this.type = type;
    }

    public Set<BonusMalus> getBonusMaluss() {
        return bonusMaluss;
    }

    public void setBonusMaluss(Set<BonusMalus> bonusMaluss) {
        this.bonusMaluss = bonusMaluss;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BonusMalusType bonusMalusType = (BonusMalusType) o;
        if(bonusMalusType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bonusMalusType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BonusMalusType{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
