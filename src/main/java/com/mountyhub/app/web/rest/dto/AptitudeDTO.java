package com.mountyhub.app.web.rest.dto;

import com.mountyhub.app.domain.Competence;
import com.mountyhub.app.domain.Spell;
import com.mountyhub.app.service.util.MountyHallUtil;

import java.util.List;

/**
 * Created by Theo on 3/23/16.
 */
public class AptitudeDTO {
    private String name;
    private Integer level;
    private Integer percent;
    private String type;
    private String tooltip;

    public AptitudeDTO(Competence competence) {
        setLevel(competence.getLevel());
        setName(competence.getCompetenceMH().getName());
        setPercent(competence.getPercent() + competence.getPercentBonus());
        setType("C");
        setTooltip(MountyHallUtil.getCompetenceTooltip(competence));
    }

    public AptitudeDTO(Spell spell) {
        setLevel(spell.getLevel());
        setName(spell.getSpellMH().getName());
        setPercent(spell.getPercent() + spell.getPercentBonus());
        setType("S");
        setTooltip(MountyHallUtil.getSpellTooltip(spell));
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
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
