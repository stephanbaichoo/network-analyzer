package com.tattea.analyzer.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.tattea.analyzer.domain.Netflow} entity. This class is used
 * in {@link com.tattea.analyzer.web.rest.NetflowResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /netflows?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class NetflowCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dateFirstSeen;

    private StringFilter timeFirstSeen;

    private BigDecimalFilter duration;

    private StringFilter protocol;

    private StringFilter srcIp;

    private StringFilter dstIp;

    private StringFilter flags;

    private IntegerFilter tos;

    private IntegerFilter packetNo;

    private StringFilter bytes;

    private StringFilter pps;

    private StringFilter bps;

    private StringFilter bpp;

    private StringFilter flows;

    private Boolean distinct;

    public NetflowCriteria() {}

    public NetflowCriteria(NetflowCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dateFirstSeen = other.dateFirstSeen == null ? null : other.dateFirstSeen.copy();
        this.timeFirstSeen = other.timeFirstSeen == null ? null : other.timeFirstSeen.copy();
        this.duration = other.duration == null ? null : other.duration.copy();
        this.protocol = other.protocol == null ? null : other.protocol.copy();
        this.srcIp = other.srcIp == null ? null : other.srcIp.copy();
        this.dstIp = other.dstIp == null ? null : other.dstIp.copy();
        this.flags = other.flags == null ? null : other.flags.copy();
        this.tos = other.tos == null ? null : other.tos.copy();
        this.packetNo = other.packetNo == null ? null : other.packetNo.copy();
        this.bytes = other.bytes == null ? null : other.bytes.copy();
        this.pps = other.pps == null ? null : other.pps.copy();
        this.bps = other.bps == null ? null : other.bps.copy();
        this.bpp = other.bpp == null ? null : other.bpp.copy();
        this.flows = other.flows == null ? null : other.flows.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NetflowCriteria copy() {
        return new NetflowCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDateFirstSeen() {
        return dateFirstSeen;
    }

    public LocalDateFilter dateFirstSeen() {
        if (dateFirstSeen == null) {
            dateFirstSeen = new LocalDateFilter();
        }
        return dateFirstSeen;
    }

    public void setDateFirstSeen(LocalDateFilter dateFirstSeen) {
        this.dateFirstSeen = dateFirstSeen;
    }

    public StringFilter getTimeFirstSeen() {
        return timeFirstSeen;
    }

    public StringFilter timeFirstSeen() {
        if (timeFirstSeen == null) {
            timeFirstSeen = new StringFilter();
        }
        return timeFirstSeen;
    }

    public void setTimeFirstSeen(StringFilter timeFirstSeen) {
        this.timeFirstSeen = timeFirstSeen;
    }

    public BigDecimalFilter getDuration() {
        return duration;
    }

    public BigDecimalFilter duration() {
        if (duration == null) {
            duration = new BigDecimalFilter();
        }
        return duration;
    }

    public void setDuration(BigDecimalFilter duration) {
        this.duration = duration;
    }

    public StringFilter getProtocol() {
        return protocol;
    }

    public StringFilter protocol() {
        if (protocol == null) {
            protocol = new StringFilter();
        }
        return protocol;
    }

    public void setProtocol(StringFilter protocol) {
        this.protocol = protocol;
    }

    public StringFilter getSrcIp() {
        return srcIp;
    }

    public StringFilter srcIp() {
        if (srcIp == null) {
            srcIp = new StringFilter();
        }
        return srcIp;
    }

    public void setSrcIp(StringFilter srcIp) {
        this.srcIp = srcIp;
    }

    public StringFilter getDstIp() {
        return dstIp;
    }

    public StringFilter dstIp() {
        if (dstIp == null) {
            dstIp = new StringFilter();
        }
        return dstIp;
    }

    public void setDstIp(StringFilter dstIp) {
        this.dstIp = dstIp;
    }

    public StringFilter getFlags() {
        return flags;
    }

    public StringFilter flags() {
        if (flags == null) {
            flags = new StringFilter();
        }
        return flags;
    }

    public void setFlags(StringFilter flags) {
        this.flags = flags;
    }

    public IntegerFilter getTos() {
        return tos;
    }

    public IntegerFilter tos() {
        if (tos == null) {
            tos = new IntegerFilter();
        }
        return tos;
    }

    public void setTos(IntegerFilter tos) {
        this.tos = tos;
    }

    public IntegerFilter getPacketNo() {
        return packetNo;
    }

    public IntegerFilter packetNo() {
        if (packetNo == null) {
            packetNo = new IntegerFilter();
        }
        return packetNo;
    }

    public void setPacketNo(IntegerFilter packetNo) {
        this.packetNo = packetNo;
    }

    public StringFilter getBytes() {
        return bytes;
    }

    public StringFilter bytes() {
        if (bytes == null) {
            bytes = new StringFilter();
        }
        return bytes;
    }

    public void setBytes(StringFilter bytes) {
        this.bytes = bytes;
    }

    public StringFilter getPps() {
        return pps;
    }

    public StringFilter pps() {
        if (pps == null) {
            pps = new StringFilter();
        }
        return pps;
    }

    public void setPps(StringFilter pps) {
        this.pps = pps;
    }

    public StringFilter getBps() {
        return bps;
    }

    public StringFilter bps() {
        if (bps == null) {
            bps = new StringFilter();
        }
        return bps;
    }

    public void setBps(StringFilter bps) {
        this.bps = bps;
    }

    public StringFilter getBpp() {
        return bpp;
    }

    public StringFilter bpp() {
        if (bpp == null) {
            bpp = new StringFilter();
        }
        return bpp;
    }

    public void setBpp(StringFilter bpp) {
        this.bpp = bpp;
    }

    public StringFilter getFlows() {
        return flows;
    }

    public StringFilter flows() {
        if (flows == null) {
            flows = new StringFilter();
        }
        return flows;
    }

    public void setFlows(StringFilter flows) {
        this.flows = flows;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NetflowCriteria that = (NetflowCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dateFirstSeen, that.dateFirstSeen) &&
            Objects.equals(timeFirstSeen, that.timeFirstSeen) &&
            Objects.equals(duration, that.duration) &&
            Objects.equals(protocol, that.protocol) &&
            Objects.equals(srcIp, that.srcIp) &&
            Objects.equals(dstIp, that.dstIp) &&
            Objects.equals(flags, that.flags) &&
            Objects.equals(tos, that.tos) &&
            Objects.equals(packetNo, that.packetNo) &&
            Objects.equals(bytes, that.bytes) &&
            Objects.equals(pps, that.pps) &&
            Objects.equals(bps, that.bps) &&
            Objects.equals(bpp, that.bpp) &&
            Objects.equals(flows, that.flows) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dateFirstSeen,
            timeFirstSeen,
            duration,
            protocol,
            srcIp,
            dstIp,
            flags,
            tos,
            packetNo,
            bytes,
            pps,
            bps,
            bpp,
            flows,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NetflowCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dateFirstSeen != null ? "dateFirstSeen=" + dateFirstSeen + ", " : "") +
            (timeFirstSeen != null ? "timeFirstSeen=" + timeFirstSeen + ", " : "") +
            (duration != null ? "duration=" + duration + ", " : "") +
            (protocol != null ? "protocol=" + protocol + ", " : "") +
            (srcIp != null ? "srcIp=" + srcIp + ", " : "") +
            (dstIp != null ? "dstIp=" + dstIp + ", " : "") +
            (flags != null ? "flags=" + flags + ", " : "") +
            (tos != null ? "tos=" + tos + ", " : "") +
            (packetNo != null ? "packetNo=" + packetNo + ", " : "") +
            (bytes != null ? "bytes=" + bytes + ", " : "") +
            (pps != null ? "pps=" + pps + ", " : "") +
            (bps != null ? "bps=" + bps + ", " : "") +
            (bpp != null ? "bpp=" + bpp + ", " : "") +
            (flows != null ? "flows=" + flows + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
