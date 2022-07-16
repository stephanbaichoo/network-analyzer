package com.tattea.analyzer.service.host;

import static org.springframework.util.Assert.notNull;

import java.io.Serializable;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class HostAPIService {

    private static final String IP_API_URL = "http://ip-api.com/json/";

    private static final String FIELDS =
        "?fields=status,message,country,countryCode,region,regionName,city,zip,lat,lon,timezone,isp,org,as,asname,query";

    private static void logErrorResponse(ClientResponse response) {
        log.error("Error Response status: {}", response.statusCode());
        log.error("Error Response headers: {}", response.headers().asHttpHeaders());
    }

    public IpAPIResponse getAPIResponse(String ipAddress) {
        String url = IP_API_URL.concat(ipAddress).concat(FIELDS);
        return executeMono(
            WebClient.builder().defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build(),
            url,
            IpAPIResponse.class
        );
    }

    public <T> T executeMono(WebClient webClient, String url, Class<T> responseClass) {
        notNull(webClient, "Web Client cannot be null");

        return webClient
            .get()
            .uri(url)
            .retrieve()
            .onStatus(
                HttpStatus::is5xxServerError,
                response -> {
                    logErrorResponse(response);
                    return Mono.error(new Exception(String.format("Server Error : %s", response.rawStatusCode())));
                }
            )
            .onStatus(
                HttpStatus::is4xxClientError,
                response -> {
                    logErrorResponse(response);
                    return Mono.error(new Exception(String.format("Client Error : %s", response.rawStatusCode())));
                }
            )
            .bodyToMono(responseClass)
            .block();
    }

    @Builder
    @Data
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IpAPIResponse implements Serializable {

        private String query;
        private String status;
        private String country;
        private String countryCode;
        private String region;
        private String regionName;
        private String city;
        private String zip;
        private float lat;
        private float lon;
        private String timezone;
        private String isp;
        private String org;
        private String as;
        private String asname;
    }
}
