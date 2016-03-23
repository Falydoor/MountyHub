package com.mountyhub.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Spell.
 */
@Entity
@Table(name = "spell")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Spell implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "percent", nullable = false)
    private Integer percent;

    @NotNull
    @Column(name = "percent_bonus", nullable = false)
    private Integer percentBonus;

    @NotNull
    @Column(name = "level", nullable = false)
    private Integer level;

    @OneToOne
    @JoinColumn(name = "spell_mh_id")
    private SpellMH spellMH;

    @ManyToOne
    @JoinColumn(name = "troll_id")
    private Troll troll;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public Integer getPercentBonus() {
        return percentBonus;
    }

    public void setPercentBonus(Integer percentBonus) {
        this.percentBonus = percentBonus;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public SpellMH getSpellMH() {
        return spellMH;
    }

    public void setSpellMH(SpellMH spellMH) {
        this.spellMH = spellMH;
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
        Spell spell = (Spell) o;
        if (spell.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, spell.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Spell{" +
            "id=" + id +
            ", percent='" + percent + "'" +
            ", percentBonus='" + percentBonus + "'" +
            ", level='" + level + "'" +
            '}';
    }
}
