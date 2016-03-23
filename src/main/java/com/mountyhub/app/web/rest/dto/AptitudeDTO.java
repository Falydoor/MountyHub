package com.mountyhub.app.web.rest.dto;

import com.mountyhub.app.domain.Competence;
import com.mountyhub.app.domain.Spell;

/**
 * Created by Theo on 3/23/16.
 */
public class AptitudeDTO {
    private String name;
    private Integer level;
    private Integer percent;
    private String type;
    
    public AptitudeDTO(Competence competence) {
        this.setLevel(competence.getLevel());
        this.setName(competence.getCompetenceMH().getName());
        this.setPercent(competence.getPercent() + competence.getPercentBonus());
        this.setType("C");
    }

    public AptitudeDTO(Spell spell) {
        this.setLevel(spell.getLevel());
        this.setName(spell.getSpellMH().getName());
        this.setPercent(spell.getPercent() + spell.getPercentBonus());
        this.setType("S");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }
}
