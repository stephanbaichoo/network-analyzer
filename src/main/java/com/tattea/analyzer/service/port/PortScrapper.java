package com.tattea.analyzer.service.port;

import com.tattea.analyzer.domain.Port;
import com.tattea.analyzer.service.PortService;
import com.tattea.analyzer.service.dto.PortDTO;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for scraping {@link Port} Data .
 */
@Service
@Transactional
public class PortScrapper {

    private final PortService portService;

    private static final String WIKI_URL = "https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers";

    @Autowired
    public PortScrapper(PortService portService) {
        this.portService = portService;
    }

    public void buildPorts() {
        List
            .of(
                PortDTO.builder().name("Https").description("Secure Https").id((long) 443).build(),
                PortDTO.builder().name("Http").description("Http").id((long) 80).build()
            )
            .forEach(portService::save);
    }

    public static void scrapperWiki() {
        List<String> results = new ArrayList<>();
        Pattern p = Pattern.compile("<thead><tbody>(.*?)</tbody>");
        Matcher m = p.matcher(getHTMLFromPage());
        while (m.find()) { //Loop through all matches
            results.add(m.group()); //Get value and store in collection.
        }
        results.forEach(System.out::println);
    }

    public static String getHTMLFromPage() {
        try {
            URL url = new URL(WIKI_URL);
            url.openConnection();
            InputStream reader = url.openStream();
            return new String(reader.readAllBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
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
