package com.mountyhub.app.web.rest.dto;

import com.mountyhub.app.domain.BonusMalus;
import org.springframework.beans.BeanUtils;

/**
 * Created by Theo on 3/28/16.
 */
public class BonusMalusDTO {
    private String name;

    private String type;

    private String effect;

    private Integer duration;

    private String realEffect;

    public BonusMalusDTO(BonusMalus bonusMalus) {
        BeanUtils.copyProperties(bonusMalus, this);
        setType(bonusMalus.getType().getType().toString());
    }

    public String getRealEffect() {
        return realEffect;
    }

    public void setRealEffect(String realEffect) {
        this.realEffect = realEffect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
