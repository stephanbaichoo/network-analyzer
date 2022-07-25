package com.tattea.analyzer.web.rest;

import com.tattea.analyzer.service.dashboard.DashboardService;
import com.tattea.analyzer.service.dto.DashboardDTO;
import com.tattea.analyzer.service.dto.PortDTO;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.tattea.analyzer.domain}.
 */
@RestController
@RequestMapping("/api")
public class DashboardResource {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardResource(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<DashboardDTO>> getProtocolStats() {
        return ResponseEntity.ok(dashboardService.buildDashboardDTO());
    }

    @GetMapping("/dashboard/port")
    public ResponseEntity<List<PortStatistic>> getPortStats() {
        List<PortStatistic> collect = dashboardService.buildDashboardDTO()
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

        return ResponseEntity.ok(collect);
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
    private static class PortStatistic {

        private PortDTO portDTO;

        private Integer bytesSum;

    }

}
