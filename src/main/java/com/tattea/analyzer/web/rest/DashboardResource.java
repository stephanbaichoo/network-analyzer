package com.tattea.analyzer.web.rest;

import com.tattea.analyzer.service.criteria.NetflowCriteria;
import com.tattea.analyzer.service.dto.NetflowDTO;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link com.tattea.analyzer.domain}.
 */
@RestController
@RequestMapping("/api")
public class DashboardResource {

    @GetMapping("/dashboard/protocol")
    public ResponseEntity<List<NetflowDTO>> getProtocolStats(String protocol) {
        return null;
    }
}
