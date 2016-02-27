package com.mountyhub.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mountyhub.app.domain.enumeration.ScriptName;

import com.mountyhub.app.domain.enumeration.ScriptType;

/**
 * A ScriptCall.
 */
@Entity
@Table(name = "script_call")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ScriptCall implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private ScriptName name;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ScriptType type;
    
    @NotNull
    @Column(name = "date_called", nullable = false)
    private ZonedDateTime dateCalled;
    
    @NotNull
    @Column(name = "url", nullable = false)
    private String url;
    
    @NotNull
    @Column(name = "successful", nullable = false)
    private Boolean successful;
    
    @ManyToOne
    @JoinColumn(name = "troll_id")
    private Troll troll;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ScriptName getName() {
        return name;
    }
    
    public void setName(ScriptName name) {
        this.name = name;
    }

    public ScriptType getType() {
        return type;
    }
    
    public void setType(ScriptType type) {
        this.type = type;
    }

    public ZonedDateTime getDateCalled() {
        return dateCalled;
    }
    
    public void setDateCalled(ZonedDateTime dateCalled) {
        this.dateCalled = dateCalled;
    }

    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getSuccessful() {
        return successful;
    }
    
    public void setSuccessful(Boolean successful) {
        this.successful = successful;
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
        ScriptCall scriptCall = (ScriptCall) o;
        if(scriptCall.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, scriptCall.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ScriptCall{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", type='" + type + "'" +
            ", dateCalled='" + dateCalled + "'" +
            ", url='" + url + "'" +
            ", successful='" + successful + "'" +
            '}';
    }
}
