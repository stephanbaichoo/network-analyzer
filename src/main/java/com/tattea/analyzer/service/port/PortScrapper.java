package com.tattea.analyzer.service.port;

import com.tattea.analyzer.domain.Port;
import com.tattea.analyzer.service.PortService;
import com.tattea.analyzer.service.dto.PortDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service Implementation for scraping {@link Port} Data .
 */
@Service
@Transactional
public class PortScrapper {

    private static final String WELL_KNOWN_PORTS = "/home/tattea/network-analyzer/src/main/resources/wikipedia_ports.csv";

    private static final Integer portMax = 1023;
    private final PortService portService;

    @Autowired
    public PortScrapper(PortService portService) {
        this.portService = portService;
    }

    public void getWellKnownPorts() {
        try (
            Reader reader = Files.newBufferedReader(Paths.get(WELL_KNOWN_PORTS));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
        ) {
            csvParser.getRecords()
                .stream()
                .skip(1)
                .map(csvRecord -> {
                    if (!StringUtils.isNumeric(csvRecord.get(0))) {
                        return null;
                    }

                    return PortDTO.builder()
                        .port(Long.parseLong(csvRecord.get(0)))
                        .name(csvRecord.get(3))
                        .isTCP(csvRecord.get(1))
                        .isUDP(csvRecord.get(2))
                        .build();
                })
                .filter(Objects::nonNull)
                .forEach(portService::save);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public enum Legend {
        YES("Described protocol is assigned by IANA for this port, and is: standardized, specified, or widely used for such."),
        UNOFFICIAL("Described protocol is not assigned by IANA for this port, but is: standardized, specified, or widely used for such."),
        ASSIGNED("Described protocol is assigned by IANA for this port,[2] but is not: standardized, specified, or widely used for such."),
        NO("Described protocol is not: assigned by IANA for this port, standardized, specified, or widely used for such."),
        RESERVED(
            "Port is reserved by IANA,[2] generally to prevent collision having its previous use removed.[3][4] The port number may be available for assignment upon request to IANA."
        );

        private String description;

        Legend(String description) {
            this.description = description;
        }

        public Legend getLegend(String legend) {
            return Arrays.stream(Legend.values()).map(leg -> leg.getLegend(legend.toUpperCase())).findFirst().orElseThrow();
        }

        public String getDescription() {
            return description;
        }
    }
}
