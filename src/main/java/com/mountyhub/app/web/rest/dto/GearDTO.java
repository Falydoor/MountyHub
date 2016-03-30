package com.mountyhub.app.web.rest.dto;

import com.mountyhub.app.domain.Gear;
import com.mountyhub.app.domain.enumeration.GearType;
import org.springframework.beans.BeanUtils;

/**
 * Created by Theo on 3/4/16.
 */
public class GearDTO {
    private String type;

    private String name;

    private String template;

    private String description;

    private Float weight;

    private Boolean wore;

    public GearDTO(Gear gear) {
        BeanUtils.copyProperties(gear, this);
        this.type = gear.getType().toString();
    }

    public Boolean getWore() {
        return wore;
    }

    public void setWore(Boolean wore) {
        this.wore = wore;
    }

    public String getType() {
        return type;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
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
}
