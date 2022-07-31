package com.tattea.analyzer.web.rest;

import com.tattea.analyzer.service.GeneralService;
import com.tattea.analyzer.service.ProtocolTypeService;
import com.tattea.analyzer.service.snmp.TrapStatsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing General Statistics.
 */
@RestController
@RequestMapping("/api")
public class GeneralResource {

    private final GeneralService generalService;

    private final TrapStatsService trapStatsService;

    private final ProtocolTypeService protocolTypeService;

    @Autowired
    public GeneralResource(GeneralService generalService, TrapStatsService trapStatsService, ProtocolTypeService protocolTypeService) {
        this.generalService = generalService;
        this.trapStatsService = trapStatsService;
        this.protocolTypeService = protocolTypeService;
    }

    @GetMapping("/general/traffic/bytes")
    public ResponseEntity<List<Long>> getLastFourBytesSum() {
        return ResponseEntity.ok(generalService.getLastFourBytesSum());
    }

    @GetMapping("/general/traffic/packets")
    public ResponseEntity<List<Integer>> getLastFourPacketsSum() {
        return ResponseEntity.ok(generalService.getLastFourPacketsSum());
    }

    @GetMapping("/general/traffic/snmp")
    public ResponseEntity<List<Long>> getLastFourSNMPLogsCount() {
        return ResponseEntity.ok(trapStatsService.getLastFourSNMPLogsCount());
    }

    @GetMapping("/general/protocol/hour")
    public ResponseEntity<List<Long>> getUDPTCPDataPerHour() {
        return ResponseEntity.ok(protocolTypeService.getUDPTCPDataPerHour());
    }

    @GetMapping("/general/protocol/yesterday")
    public ResponseEntity<List<Long>> getUDPTCPDataYesterday() {
        return ResponseEntity.ok(protocolTypeService.getUDPTCPDataYesterday());
    }

    @GetMapping("/general/protocol/four")
    public ResponseEntity<List<Long>> getUDPTCPDataFourDays() {
        return ResponseEntity.ok(protocolTypeService.getUDPTCPDataFourDays());
    }
}
