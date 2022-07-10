package com.tattea.analyzer.service.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class NetflowProtocolStat implements Serializable {

    private LocalDateTime localDateTime;

    private String protocol;

    private Long bytes;
}
