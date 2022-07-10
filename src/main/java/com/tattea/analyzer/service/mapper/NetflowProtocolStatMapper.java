package com.tattea.analyzer.service.mapper;

import com.tattea.analyzer.domain.Netflow;
import com.tattea.analyzer.service.dto.NetflowProtocolStat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface NetflowProtocolStatMapper {
    // @Mapping(target = "localDateTime", source = "netflow", qualifiedByName = "mapToLocalDateTime")
    // @Mapping(target = "bytes", source = "bytes", qualifiedByName = "mapToBytes")
    NetflowProtocolStat toNetflowProtocolStat(Netflow netflow);

    @Named(value = "mapToLocalDateTime")
    default LocalDateTime mapToLocalDateTime(Netflow netflow) {
        return LocalDateTime.of(netflow.getDateFirstSeen(), LocalTime.parse(netflow.getTimeFirstSeen()));
    }

    @Named(value = "mapToBytes")
    default Long mapToBytes(String bytes) {
        return bytes.contains("M") ? Long.parseLong(bytes.replace("M", StringUtils.EMPTY)) * 1000000 : Long.parseLong(bytes);
    }
}
