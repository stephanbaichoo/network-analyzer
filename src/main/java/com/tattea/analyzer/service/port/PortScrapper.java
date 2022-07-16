package com.tattea.analyzer.service.port;

import com.tattea.analyzer.domain.Port;
import com.tattea.analyzer.service.PortService;
import com.tattea.analyzer.service.dto.PortDTO;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

/**
 * Service Implementation for scraping {@link Port} Data .
 */
@Service
@Transactional
public class PortScrapper {

    private static final String WELL_KNOWN_PORTS = "wikipedia_ports_1.txt";

    private static final String REGISTERED_PORTS = "wikipedia_ports_2.txt";

    private final PortService portService;

    @Autowired
    public PortScrapper(PortService portService) {
        this.portService = portService;
    }

    public void buildPorts() {}

    public List<PortDTO> getWellKnownPorts() {
        Pattern patternRow = Pattern.compile("<tr>(.*?)</tr>");
        String fileText = "";
        List<String> rows = new ArrayList<>();

        File file = new File(WELL_KNOWN_PORTS);

        try {
            List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
            fileText = String.join("", lines);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Matcher matcher = patternRow.matcher(fileText);
        while (matcher.find()) {
            rows.add(matcher.group(0));
        }

        rows.forEach(System.out::println);
        return null;
    }

    public List<PortDTO> getRegisteredPorts() {
        return null;
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
