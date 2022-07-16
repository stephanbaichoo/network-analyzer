package com.tattea.analyzer.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link com.tattea.analyzer.domain.Trap} entity.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrapDTO implements Serializable {

    private Long id;

    private LocalDate date;

    private String time;

    private String trap;

    private String values;

    private String trigger;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTrap() {
        return trap;
    }

    public void setTrap(String trap) {
        this.trap = trap;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrapDTO)) {
            return false;
        }

        TrapDTO trapDTO = (TrapDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trapDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrapDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", time='" + getTime() + "'" +
            ", trap='" + getTrap() + "'" +
            ", values='" + getValues() + "'" +
            ", trigger='" + getTrigger() + "'" +
            "}";
    }
}
