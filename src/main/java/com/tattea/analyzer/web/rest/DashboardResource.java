package com.tattea.analyzer.web.rest;

import com.tattea.analyzer.service.dashboard.DashboardService;
import com.tattea.analyzer.service.dto.DashboardDTO;
import com.tattea.analyzer.service.port.PortDashboardService;
import com.tattea.analyzer.service.port.PortDashboardService.MostPortDataSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing {@link com.tattea.analyzer.domain}.
 */
@RestController
@RequestMapping("/api")
public class DashboardResource {

    private final DashboardService dashboardService;

    private final PortDashboardService portDashboardService;

    @Autowired
    public DashboardResource(DashboardService dashboardService, PortDashboardService portDashboardService) {
        this.dashboardService = dashboardService;
        this.portDashboardService = portDashboardService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<DashboardDTO>> getProtocolStats() {
        return ResponseEntity.ok(dashboardService.buildDashboardDTO());
    }

    @GetMapping("/dashboard/port")
    public ResponseEntity<List<DashboardService.PortStatistic>> getPortStats() {
        return ResponseEntity.ok(dashboardService.getPortStats());
    }

    @GetMapping("/dashboard/port/outgoing")
    public ResponseEntity<List<MostPortDataSummary>> getMostTrafficOutgoingPortsYesterdaySegregated() {
        return ResponseEntity.ok(portDashboardService.getMostTrafficOutgoingPortsYesterdaySegregated());
    }

    @GetMapping("/dashboard/port/ingoing")
    public ResponseEntity<List<MostPortDataSummary>> getMostTrafficIngoingPortsYesterdaySegregated() {
        return ResponseEntity.ok(portDashboardService.getMostTrafficIngoingPortsYesterdaySegregated());
    }

}
