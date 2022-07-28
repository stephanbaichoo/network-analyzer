package com.tattea.analyzer.service.dto;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Data
public class DashboardDTO implements Serializable {

    private NetflowDTO netflowDTO;

    public PortDTO srcPort;

    public PortDTO dstPort;

    private HostDTO srcHost;

    private HostDTO dstHost;

}
