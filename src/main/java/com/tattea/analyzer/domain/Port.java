package com.tattea.analyzer.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Port.
 */
@Entity
@Table(name = "port")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Port implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "port")
    private Long port;

    @Column(name = "is_tcp")
    private String isTCP;

    @Column(name = "is_udp")
    private String isUDP;

    @Column(name = "is_sctp")
    private String isSCTP;

    @Column(name = "description")
    private String description;

    @Column(name = "name")
    private String name;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Port id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPort() {
        return this.port;
    }

    public Port port(Long port) {
        this.setPort(port);
        return this;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public String getIsTCP() {
        return this.isTCP;
    }

    public Port isTCP(String isTCP) {
        this.setIsTCP(isTCP);
        return this;
    }

    public void setIsTCP(String isTCP) {
        this.isTCP = isTCP;
    }

    public String getIsUDP() {
        return this.isUDP;
    }

    public Port isUDP(String isUDP) {
        this.setIsUDP(isUDP);
        return this;
    }

    public void setIsUDP(String isUDP) {
        this.isUDP = isUDP;
    }

    public String getIsSCTP() {
        return this.isSCTP;
    }

    public Port isSCTP(String isSCTP) {
        this.setIsSCTP(isSCTP);
        return this;
    }

    public void setIsSCTP(String isSCTP) {
        this.isSCTP = isSCTP;
    }

    public String getDescription() {
        return this.description;
    }

    public Port description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public Port name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Port)) {
            return false;
        }
        return id != null && id.equals(((Port) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Port{" +
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
