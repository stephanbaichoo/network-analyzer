package com.tattea.analyzer.service.dto;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class DashboardDTO implements Serializable {

    private NetflowDTO netflowDTO;

    private PortDTO srcPort;

    private PortDTO dstPort;

    private HostDTO srcHost;

    private HostDTO dstHost;

}
