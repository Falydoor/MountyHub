package com.mountyhub.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mountyhub.app.domain.enumeration.FlyType;

/**
 * A Fly.
 */
@Entity
@Table(name = "fly")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fly implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "number", nullable = false)
    private Long number;
    
    @NotNull
    @Column(name = "old", nullable = false)
    private Integer old;
    
    @NotNull
    @Column(name = "here", nullable = false)
    private Boolean here;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private FlyType type;
    
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;
    
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

    public Integer getOld() {
        return old;
    }
    
    public void setOld(Integer old) {
        this.old = old;
    }

    public Boolean getHere() {
        return here;
    }
    
    public void setHere(Boolean here) {
        this.here = here;
    }

    public FlyType getType() {
        return type;
    }
    
    public void setType(FlyType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
        Fly fly = (Fly) o;
        if(fly.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, fly.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Fly{" +
            "id=" + id +
            ", number='" + number + "'" +
            ", old='" + old + "'" +
            ", here='" + here + "'" +
            ", type='" + type + "'" +
            ", name='" + name + "'" +
            '}';
    }
}
