package com.tattea.analyzer.web.rest;

import com.tattea.analyzer.service.GeneralService;
import com.tattea.analyzer.service.port.PortDashboardService;
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

    @Autowired
    public GeneralResource(GeneralService generalService) {
        this.generalService = generalService;
    }

    @GetMapping("/general/traffic/bytes")
    public ResponseEntity<List<Long>> getLastFourBytesSum() {
        return ResponseEntity.ok(generalService.getLastFourBytesSum());
    }
}
