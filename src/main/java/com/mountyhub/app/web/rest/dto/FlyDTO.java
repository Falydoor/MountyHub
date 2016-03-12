package com.mountyhub.app.web.rest.dto;

import com.mountyhub.app.domain.enumeration.FlyType;

/**
 * Created by Theo on 3/12/16.
 */
public class FlyDTO {
    private String name;
    private FlyType type;
    private String effect;
    private Boolean here;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FlyType getType() {
        return type;
    }

    public void setType(FlyType type) {
        this.type = type;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public Boolean getHere() {
        return here;
    }

    public void setHere(Boolean here) {
        this.here = here;
    }
}
