package com.tattea.analyzer.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.tattea.analyzer.domain.Netflow} entity.
 */
public class NetflowDTO implements Serializable {

    private Long id;

    private LocalDate dateFirstSeen;

    private String timeFirstSeen;

    private BigDecimal duration;

    private String protocol;

    private String srcIp;

    private String dstIp;

    private String flags;

    private Integer tos;

    private Integer packetNo;

    private String bytes;

    private String pps;

    private String bps;

    private String bpp;

    private String flows;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateFirstSeen() {
        return dateFirstSeen;
    }

    public void setDateFirstSeen(LocalDate dateFirstSeen) {
        this.dateFirstSeen = dateFirstSeen;
    }

    public String getTimeFirstSeen() {
        return timeFirstSeen;
    }

    public void setTimeFirstSeen(String timeFirstSeen) {
        this.timeFirstSeen = timeFirstSeen;
    }

    public BigDecimal getDuration() {
        return duration;
    }

    public void setDuration(BigDecimal duration) {
        this.duration = duration;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }

    public String getDstIp() {
        return dstIp;
    }

    public void setDstIp(String dstIp) {
        this.dstIp = dstIp;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public Integer getTos() {
        return tos;
    }

    public void setTos(Integer tos) {
        this.tos = tos;
    }

    public Integer getPacketNo() {
        return packetNo;
    }

    public void setPacketNo(Integer packetNo) {
        this.packetNo = packetNo;
    }

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }

    public String getPps() {
        return pps;
    }

    public void setPps(String pps) {
        this.pps = pps;
    }

    public String getBps() {
        return bps;
    }

    public void setBps(String bps) {
        this.bps = bps;
    }

    public String getBpp() {
        return bpp;
    }

    public void setBpp(String bpp) {
        this.bpp = bpp;
    }

    public String getFlows() {
        return flows;
    }

    public void setFlows(String flows) {
        this.flows = flows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NetflowDTO)) {
            return false;
        }

        NetflowDTO netflowDTO = (NetflowDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, netflowDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NetflowDTO{" +
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
