package com.tattea.analyzer.service.dashboard;

import com.tattea.analyzer.repository.NetflowRepository;
import com.tattea.analyzer.service.HostService;
import com.tattea.analyzer.service.PortService;
import com.tattea.analyzer.service.dto.HostDTO;
import com.tattea.analyzer.service.dto.NetflowDTO;
import com.tattea.analyzer.service.dto.PortDTO;
import com.tattea.analyzer.service.host.HostAPIService;
import com.tattea.analyzer.service.host.HostAPIService.IpAPIResponse;
import com.tattea.analyzer.service.mapper.NetflowMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

    @Scheduled(cron = "*/25 * * * * *") // will save each 25 seconds for eg : "*/25 * * * * *"
    public void buildDashboardDB() {
        log.info("Build Dashboard Data at".concat(LocalDateTime.now().toString()));
        netflowRepository.findAll().stream().map(netflowMapper::toDto).forEach(this::buildHosts);
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
            .filter(ip -> localIPAddresses.stream().anyMatch(ip::startsWith))
            .map(ip -> HostDTO.builder().ipAddress(ip).hostName("Ubuntu Host-".concat(UUID.randomUUID().toString().substring(0, 5))).build()
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
}
