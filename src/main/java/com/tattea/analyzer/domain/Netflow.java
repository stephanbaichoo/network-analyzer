package com.tattea.analyzer.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Netflow.
 */
@Entity
@Table(name = "netflow")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Netflow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_first_seen")
    private LocalDate dateFirstSeen;

    @Column(name = "time_first_seen")
    private String timeFirstSeen;

    @Column(name = "duration", precision = 21, scale = 2)
    private BigDecimal duration;

    @Column(name = "protocol")
    private String protocol;

    @Column(name = "src_ip")
    private String srcIp;

    @Column(name = "dst_ip")
    private String dstIp;

    @Column(name = "flags")
    private String flags;

    @Column(name = "tos")
    private Integer tos;

    @Column(name = "packet_no")
    private Integer packetNo;

    @Column(name = "bytes")
    private String bytes;

    @Column(name = "pps")
    private String pps;

    @Column(name = "bps")
    private String bps;

    @Column(name = "bpp")
    private String bpp;

    @Column(name = "flows")
    private String flows;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Netflow id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateFirstSeen() {
        return this.dateFirstSeen;
    }

    public Netflow dateFirstSeen(LocalDate dateFirstSeen) {
        this.setDateFirstSeen(dateFirstSeen);
        return this;
    }

    public void setDateFirstSeen(LocalDate dateFirstSeen) {
        this.dateFirstSeen = dateFirstSeen;
    }

    public String getTimeFirstSeen() {
        return this.timeFirstSeen;
    }

    public Netflow timeFirstSeen(String timeFirstSeen) {
        this.setTimeFirstSeen(timeFirstSeen);
        return this;
    }

    public void setTimeFirstSeen(String timeFirstSeen) {
        this.timeFirstSeen = timeFirstSeen;
    }

    public BigDecimal getDuration() {
        return this.duration;
    }

    public Netflow duration(BigDecimal duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(BigDecimal duration) {
        this.duration = duration;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public Netflow protocol(String protocol) {
        this.setProtocol(protocol);
        return this;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getSrcIp() {
        return this.srcIp;
    }

    public Netflow srcIp(String srcIp) {
        this.setSrcIp(srcIp);
        return this;
    }

    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }

    public String getDstIp() {
        return this.dstIp;
    }

    public Netflow dstIp(String dstIp) {
        this.setDstIp(dstIp);
        return this;
    }

    public void setDstIp(String dstIp) {
        this.dstIp = dstIp;
    }

    public String getFlags() {
        return this.flags;
    }

    public Netflow flags(String flags) {
        this.setFlags(flags);
        return this;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public Integer getTos() {
        return this.tos;
    }

    public Netflow tos(Integer tos) {
        this.setTos(tos);
        return this;
    }

    public void setTos(Integer tos) {
        this.tos = tos;
    }

    public Integer getPacketNo() {
        return this.packetNo;
    }

    public Netflow packetNo(Integer packetNo) {
        this.setPacketNo(packetNo);
        return this;
    }

    public void setPacketNo(Integer packetNo) {
        this.packetNo = packetNo;
    }

    public String getBytes() {
        return this.bytes;
    }

    public Netflow bytes(String bytes) {
        this.setBytes(bytes);
        return this;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }

    public String getPps() {
        return this.pps;
    }

    public Netflow pps(String pps) {
        this.setPps(pps);
        return this;
    }

    public void setPps(String pps) {
        this.pps = pps;
    }

    public String getBps() {
        return this.bps;
    }

    public Netflow bps(String bps) {
        this.setBps(bps);
        return this;
    }

    public void setBps(String bps) {
        this.bps = bps;
    }

    public String getBpp() {
        return this.bpp;
    }

    public Netflow bpp(String bpp) {
        this.setBpp(bpp);
        return this;
    }

    public void setBpp(String bpp) {
        this.bpp = bpp;
    }

    public String getFlows() {
        return this.flows;
    }

    public Netflow flows(String flows) {
        this.setFlows(flows);
        return this;
    }

    public void setFlows(String flows) {
        this.flows = flows;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Netflow)) {
            return false;
        }
        return id != null && id.equals(((Netflow) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Netflow{" +
            "id=" + getId() +
            ", dateFirstSeen='" + getDateFirstSeen() + "'" +
            ", timeFirstSeen='" + getTimeFirstSeen() + "'" +
            ", duration=" + getDuration() +
            ", protocol='" + getProtocol() + "'" +
            ", srcIp='" + getSrcIp() + "'" +
            ", dstIp='" + getDstIp() + "'" +
            ", flags='" + getFlags() + "'" +
            ", tos=" + getTos() +
            ", packetNo=" + getPacketNo() +
            ", bytes='" + getBytes() + "'" +
            ", pps='" + getPps() + "'" +
            ", bps='" + getBps() + "'" +
            ", bpp='" + getBpp() + "'" +
            ", flows='" + getFlows() + "'" +
            "}";
    }
}
