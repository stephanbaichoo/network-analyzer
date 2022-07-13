package com.tattea.analyzer.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Host.
 */
@Entity
@Table(name = "host")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Host implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "host_name")
    private String hostName;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "asname")
    private String asname;

    @Column(name = "org")
    private String org;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Host id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHostName() {
        return this.hostName;
    }

    public Host hostName(String hostName) {
        this.setHostName(hostName);
        return this;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public Host ipAddress(String ipAddress) {
        this.setIpAddress(ipAddress);
        return this;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getAsname() {
        return this.asname;
    }

    public Host asname(String asname) {
        this.setAsname(asname);
        return this;
    }

    public void setAsname(String asname) {
        this.asname = asname;
    }

    public String getOrg() {
        return this.org;
    }

    public Host org(String org) {
        this.setOrg(org);
        return this;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Host)) {
            return false;
        }
        return id != null && id.equals(((Host) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Host{" +
            "id=" + getId() +
            ", hostName='" + getHostName() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", asname='" + getAsname() + "'" +
            ", org='" + getOrg() + "'" +
            "}";
    }
}
