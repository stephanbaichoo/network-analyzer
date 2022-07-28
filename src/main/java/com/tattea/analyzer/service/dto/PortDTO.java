package com.tattea.analyzer.service.dto;

import java.io.Serializable;
import java.util.Objects;

import lombok.*;

/**
 * A DTO for the {@link com.tattea.analyzer.domain.Port} entity.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PortDTO implements Serializable {

    private Long id;

    private Long port;

    private String isTCP;

    private String isUDP;

    private String isSCTP;

    private String description;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public String getIsTCP() {
        return isTCP;
    }

    public void setIsTCP(String isTCP) {
        this.isTCP = isTCP;
    }

    public String getIsUDP() {
        return isUDP;
    }

    public void setIsUDP(String isUDP) {
        this.isUDP = isUDP;
    }

    public String getIsSCTP() {
        return isSCTP;
    }

    public void setIsSCTP(String isSCTP) {
        this.isSCTP = isSCTP;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PortDTO)) {
            return false;
        }

        PortDTO portDTO = (PortDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, portDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PortDTO{" +
            "id=" + getId() +
            ", port=" + getPort() +
            ", isTCP='" + getIsTCP() + "'" +
            ", isUDP='" + getIsUDP() + "'" +
            ", isSCTP='" + getIsSCTP() + "'" +
            ", description='" + getDescription() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
