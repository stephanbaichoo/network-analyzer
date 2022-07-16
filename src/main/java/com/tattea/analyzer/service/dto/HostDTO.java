package com.tattea.analyzer.service.dto;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link com.tattea.analyzer.domain.Host} entity.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HostDTO implements Serializable {

    private Long id;

    private String hostName;

    private String ipAddress;

    private String asname;

    private String org;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getAsname() {
        return asname;
    }

    public void setAsname(String asname) {
        this.asname = asname;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HostDTO)) {
            return false;
        }

        HostDTO hostDTO = (HostDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hostDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HostDTO{" +
            "id=" + getId() +
            ", hostName='" + getHostName() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", asname='" + getAsname() + "'" +
            ", org='" + getOrg() + "'" +
            "}";
    }
}
