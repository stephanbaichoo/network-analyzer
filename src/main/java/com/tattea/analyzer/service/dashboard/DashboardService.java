package com.tattea.analyzer.service.dashboard;

import com.tattea.analyzer.domain.Netflow;
import com.tattea.analyzer.repository.NetflowRepository;
import com.tattea.analyzer.service.HostService;
import com.tattea.analyzer.service.PortService;
import com.tattea.analyzer.service.dto.DashboardDTO;
import com.tattea.analyzer.service.dto.HostDTO;
import com.tattea.analyzer.service.dto.NetflowDTO;
import com.tattea.analyzer.service.dto.PortDTO;
import com.tattea.analyzer.service.host.HostAPIService;
import com.tattea.analyzer.service.host.HostAPIService.IpAPIResponse;
import com.tattea.analyzer.service.mapper.NetflowMapper;
import com.tattea.analyzer.web.rest.DashboardResource;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DashboardService {

    private final NetflowRepository netflowRepository;

    private final NetflowMapper netflowMapper;

    private final HostAPIService hostAPIService;

    private final HostService hostService;

    private final PortService portService;

    @Autowired
    public DashboardService(
        NetflowRepository netflowRepository,
        NetflowMapper netflowMapper,
        HostAPIService hostAPIService,
        HostService hostService,
        PortService portService
    ) {
        this.netflowRepository = netflowRepository;
        this.netflowMapper = netflowMapper;
        this.hostAPIService = hostAPIService;
        this.hostService = hostService;
        this.portService = portService;
    }

    //@Scheduled(cron = "*/50 * * * * *") // will save each 25 seconds for eg : "*/25 * * * * *"
    public void buildDashboardDB() {
        log.info("Build Dashboard Data at".concat(LocalDateTime.now().toString()));
        netflowRepository.findAll().stream().map(netflowMapper::toDto).forEach(this::buildHosts);
    }

    //@Scheduled(cron = "* */1 * * *") // will save each 25 seconds for eg : "*/25 * * * * *"
    public List<DashboardDTO> buildDashboardDTO() {
        log.info("Build All Dashboard Data at".concat(LocalDateTime.now().toString()));

        return netflowRepository.findAll()
            .stream()
            .map(netflow -> {

                //List<Netflow> netflows = netflowRepository.findAll();

                HostDTO dstHostDTO = getHostDTO(netflow, Netflow::getDstIp);
                HostDTO srcHostDTO = getHostDTO(netflow, Netflow::getSrcIp);

                PortDTO dstPortDTO = getPortDTO(netflow, Netflow::getDstIp);
                PortDTO srcPortDTO = getPortDTO(netflow, Netflow::getSrcIp);

                return DashboardDTO.builder()
                    .netflowDTO(netflowMapper.toDto(netflow))
                    .dstHost(dstHostDTO)
                    .srcHost(srcHostDTO)
                    .dstPort(dstPortDTO)
                    .srcPort(srcPortDTO)
                    .build();
            }).collect(Collectors.toList());
    }

    private HostDTO getHostDTO(Netflow netflow, Function<Netflow, String> function) {
        return Optional.of(netflow)
            .map(function)
            .map(s -> s.split(":")[0])
            .map(ip -> hostService.findOneByIp(ip).orElse(HostDTO.builder()
                .hostName("Host ".concat(ip))
                .ipAddress(ip)
                .build()))
            .orElse(HostDTO.builder().build());
    }

    private PortDTO getPortDTO(Netflow netflow, Function<Netflow, String> function) {
        return Optional.of(netflow)
            .map(function)
            .map(s -> s.split(":")[1])
            .filter(port->!port.contains("."))
            .map(port -> portService.findOneByPort(Long.valueOf(port)).orElse(PortDTO.builder()
                .port(Long.valueOf(port))
                .name("Port Number ".concat(port))
                .build()))
            .orElse(PortDTO.builder().build());
    }

    public List<PortStatistic> getPortStats() {
        return this.buildDashboardDTO()
            .stream()
            .collect(Collectors.groupingBy(DashboardDTO::getDstPort,
                Collectors.summingInt(foo -> getBytes(foo.getNetflowDTO().getBytes()))))
            .entrySet()
            .stream()
            .map(portDTOIntegerEntry -> PortStatistic.builder()
                .portDTO(portDTOIntegerEntry.getKey())
                .bytesSum(portDTOIntegerEntry.getValue())
                .build())
            .collect(Collectors.toList());
    }

    // TODO : Build Hosts
    public void buildHosts(NetflowDTO netflowDTO) {
        // build local hosts
        List<String> localIPAddresses = List.of("10.0", "192.168");

        /*        List.of(netflowDTO.getSrcIp(), netflowDTO.getDstIp())
            .stream()
            .map(ip -> ip.split(":")[1])
            .map(port -> PortDTO.builder()
                .port(Long.parseLong(port))
                .name("Port Number ".concat(port))
                .build())*/

        List<String> ipWithNoPorts = List
            .of(netflowDTO.getSrcIp(), netflowDTO.getDstIp())
            .stream()
            .map(ip -> ip.split(":")[0])
            .collect(Collectors.toList());

        ipWithNoPorts
            .stream()
            .filter(ip -> localIPAddresses.stream().anyMatch(ip::startsWith) && ip.contains("10.0.0.254"))
            .filter(ip -> hostService.findOneByIp(ip).isEmpty())
            .map(ip -> HostDTO.builder().ipAddress(ip).hostName("Ubuntu Host-".concat(UUID.randomUUID().toString().substring(0, 5))).build()
            )
            .forEach(hostService::save);


        // build routers
        List<String> routerIPAddresses = List.of("10.0.0.254");

        List<String> routerIpWithNoPorts = List
            .of(netflowDTO.getSrcIp(), netflowDTO.getDstIp())
            .stream()
            .map(ip -> ip.split(":")[0])
            .collect(Collectors.toList());

        ipWithNoPorts
            .stream()
            .filter(ip -> routerIPAddresses.stream().anyMatch(ip::startsWith))
            .filter(ip -> hostService.findOneByIp(ip).isEmpty())
            .map(ip -> HostDTO.builder().ipAddress(ip).hostName("Gateway Router-".concat(UUID.randomUUID().toString().substring(0, 5))).build()
            )
            .forEach(hostService::save);


        // build public hosts
        ipWithNoPorts
            .stream()
            .filter(ip -> localIPAddresses.stream().noneMatch(ip::startsWith))
            .filter(ip -> hostService.findOneByIp(ip).isEmpty())
            .map(ip -> buildHostDTO(hostAPIService.getAPIResponse(ip), ip))
            .forEach(hostService::save);
    }

    public HostDTO buildHostDTO(IpAPIResponse apiResponse, String ipAddress) {
        return HostDTO.builder().ipAddress(ipAddress).asname(apiResponse.getAsname()).org(apiResponse.getOrg()).build();
    }

    private Integer getBytes(String bytes) {
        double p = bytes.contains("M") ?
            Double.parseDouble(bytes.replace("M", "")) * 1000000 : Double.parseDouble(bytes);
        return (int) p;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Setter
    public static class PortStatistic {

        private PortDTO portDTO;

        private Integer bytesSum;

    }
}
