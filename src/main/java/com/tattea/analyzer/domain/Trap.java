package com.tattea.analyzer.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Trap.
 */
@Entity
@Table(name = "trap")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Trap implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time")
    private String time;

    @Column(name = "trap")
    private String trap;

    @Column(name = "jhi_values")
    private String values;

    @Column(name = "jhi_trigger")
    private String trigger;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Trap id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Trap date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return this.time;
    }

    public Trap time(String time) {
        this.setTime(time);
        return this;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTrap() {
        return this.trap;
    }

    public Trap trap(String trap) {
        this.setTrap(trap);
        return this;
    }

    public void setTrap(String trap) {
        this.trap = trap;
    }

    public String getValues() {
        return this.values;
    }

    public Trap values(String values) {
        this.setValues(values);
        return this;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getTrigger() {
        return this.trigger;
    }

    public Trap trigger(String trigger) {
        this.setTrigger(trigger);
        return this;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trap)) {
            return false;
        }
        return id != null && id.equals(((Trap) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trap{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", time='" + getTime() + "'" +
            ", trap='" + getTrap() + "'" +
            ", values='" + getValues() + "'" +
            ", trigger='" + getTrigger() + "'" +
            "}";
    }
}
