package com.tattea.analyzer.service.port;

import com.tattea.analyzer.service.dashboard.DashboardService;
import com.tattea.analyzer.service.dto.DashboardDTO;
import com.tattea.analyzer.service.dto.NetflowDTO;
import com.tattea.analyzer.service.dto.PortDTO;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Service
public class PortDashboardService {

    private final DashboardService dashboardService;

    @Autowired
    public PortDashboardService(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    private static Integer getBytes(String bytes) {
        double p = bytes.contains("M") ?
            Double.parseDouble(bytes.replace("M", "")) * 1000000 : Double.parseDouble(bytes);
        return (int) p;
    }

    public List<MostPortData> getMostTrafficOutgoingPortsYesterday() {
        return dashboardService.buildDashboardDTO()
            .stream()
            .filter(dashboardDTO -> {
                NetflowDTO netflowDTO = dashboardDTO.getNetflowDTO();
                return LocalDate.now().minusDays(1).isBefore(netflowDTO.getDateFirstSeen())
                    || LocalDate.now().minusDays(1).isEqual(netflowDTO.getDateFirstSeen());
            }).map(
                dashboardDTO -> MostPortData.builder()
                    .bytes(getBytes(dashboardDTO.getNetflowDTO().getBytes()))
                    .localTime(LocalTime.parse(dashboardDTO.getNetflowDTO().getTimeFirstSeen()))
                    .localDate(dashboardDTO.getNetflowDTO().getDateFirstSeen())
                    .portName(dashboardDTO.getDstPort().getName())
                    .portNumber(dashboardDTO.getDstPort().getPort().toString())
                    .build())
            .filter(mostPortData -> Integer.parseInt(mostPortData.getPortNumber()) < 2000)
            .collect(Collectors.toList());
    }

    public List<MostPortDataSummary> getMostTrafficOutgoingPortsYesterdaySegregated() {
        return getMostTrafficPortsYesterdaySegregated(getMostTrafficOutgoingPortsYesterday());
    }

    public List<MostPortDataSummary> getMostTrafficIngoingPortsYesterdaySegregated() {
        return getMostTrafficPortsYesterdaySegregated(getMostTrafficIngoingPortsYesterday());
    }

    public List<MostPortDataSummary> getMostTrafficPortsYesterdaySegregated(List<MostPortData> portData) {
        return IntStream.of(0, 23)
            .boxed()
            .flatMap(integer -> portData
                .stream()
                .filter(mostPortData -> mostPortData.getLocalTime().isAfter(LocalTime.now().minusHours(integer + 1))
                    && mostPortData.getLocalTime().isBefore(LocalTime.now().minusHours(integer)))
                .map(mostPortData -> MostPortDataSummary.builder()
                    .bytesSum(mostPortData.bytes)
                    .hour(String.valueOf(integer))
                    .portName(mostPortData.getPortName())
                    .portNumber(mostPortData.getPortNumber())
                    .build())
                .collect(Collectors.groupingBy(MostPortDataSummary::getPortName,
                    Collectors.summingInt(mostPortDataSummary -> getBytes(mostPortDataSummary.getBytesSum().toString()))))
                .entrySet()
                .stream()
                .flatMap(Stream::of)
                .map(stringIntegerEntry -> MostPortDataSummary.builder()
                    .portName(stringIntegerEntry.getKey())
                    .hour(integer.toString())
                    .bytesSum(stringIntegerEntry.getValue())
                    .build())
            )
            .collect(Collectors.toList());
    }

    public List<MostPortData> getMostTrafficIngoingPortsYesterday() {
        return getMostTrafficPorts((long) 1, DashboardDTO::getSrcPort);
    }

    public List<MostPortDataTable> getMostOutgoingPortDataTableForLastHour() {
        return getMostPortDataTableForLastHour(DashboardDTO::getDstPort);
    }

    public List<MostPortDataTable> getMostIngoingPortDataTableForLastHour() {
        return getMostPortDataTableForLastHour(DashboardDTO::getSrcPort);
    }

    public List<MostPortDataTable> getMostPortDataTableForLastHour(Function<DashboardDTO, PortDTO> fn) {
        return dashboardService.buildDashboardDTO()
            .stream()
            .filter(dashboardDTO -> {
                NetflowDTO netflowDTO = dashboardDTO.getNetflowDTO();
                return LocalTime.now().minusHours(1).isBefore(LocalTime.parse(netflowDTO.getTimeFirstSeen()));
            })
            .collect(Collectors.groupingBy(dashboardDTO -> fn.apply(dashboardDTO).getName(), Collectors.toList()))
            .entrySet()
            .stream()
            .flatMap(Stream::of)
            .map(stringListEntry -> {
                // Port Number
                String general_port = stringListEntry.getValue().stream().findFirst()
                    .map(fn)
                    .map(PortDTO::getPort)
                    .map(String::valueOf)
                    .orElse("General Port");

                // Description
                String description = stringListEntry.getValue().stream().findFirst()
                    .map(fn)
                    .map(PortDTO::getDescription)
                    .orElse("See Port Info Page");

                // OutgoingBytesSum
                int outgoingBytesSum = stringListEntry.getValue().stream()
                    .map(DashboardDTO::getNetflowDTO)
                    .map(NetflowDTO::getBytes)
                    .mapToInt(PortDashboardService::getBytes)
                    .sum();

                // OutgoingPacketsSum
                int outgoingPacketsSum = stringListEntry.getValue().stream()
                    .map(DashboardDTO::getNetflowDTO)
                    .mapToInt(NetflowDTO::getPacketNo)
                    .sum();


                return MostPortDataTable.builder()
                    .portName(stringListEntry.getKey())
                    .portNumber(general_port)
                    .hoverDescription(description)
                    .OutgoingBytesSum(outgoingBytesSum)
                    .OutgoingPacketsSum(outgoingPacketsSum)
                    .build();
            })
            .collect(Collectors.toList());
    }

    public List<MostPortDataTable> getMostOutgoingPortDataTableYesterday() {
        return getMostPortDataTableForLast3Days(1, DashboardDTO::getDstPort);
    }

    public List<MostPortDataTable> getMostIngoingPortDataTableYesterday() {
        return getMostPortDataTableForLast3Days(1, DashboardDTO::getSrcPort);
    }

    public List<MostPortDataTable> getMostIngoingPortDataTableForLast3Days() {
        return getMostPortDataTableForLast3Days(3, DashboardDTO::getSrcPort);
    }

    public List<MostPortDataTable> getMostOutgoingPortDataTableForLast3Days() {
        return getMostPortDataTableForLast3Days(3, DashboardDTO::getDstPort);
    }

    public List<MostPortDataTable> getMostPortDataTableForLast3Days(long back, Function<DashboardDTO, PortDTO> fn) {
        return dashboardService.buildDashboardDTO()
            .stream()
            .filter(dashboardDTO -> {
                NetflowDTO netflowDTO = dashboardDTO.getNetflowDTO();
                return LocalDate.now().minusDays(back).isBefore(netflowDTO.getDateFirstSeen())
                    && LocalDate.now().minusDays(back).isEqual(netflowDTO.getDateFirstSeen());
            })
            .collect(Collectors.groupingBy(dashboardDTO -> fn.apply(dashboardDTO).getName(), Collectors.toList()))
            .entrySet()
            .stream()
            .flatMap(Stream::of)
            .map(stringListEntry -> {
                // Port Number
                String general_port = stringListEntry.getValue().stream().findFirst()
                    .map(fn)
                    .map(PortDTO::getPort)
                    .map(String::valueOf)
                    .orElse("General Port");

                // Description
                String description = stringListEntry.getValue().stream().findFirst()
                    .map(fn)
                    .map(PortDTO::getDescription)
                    .orElse("See Port Info Page");

                // OutgoingBytesSum
                int outgoingBytesSum = stringListEntry.getValue().stream()
                    .map(DashboardDTO::getNetflowDTO)
                    .map(NetflowDTO::getBytes)
                    .mapToInt(PortDashboardService::getBytes)
                    .sum();

                // OutgoingPacketsSum
                int outgoingPacketsSum = stringListEntry.getValue().stream()
                    .map(DashboardDTO::getNetflowDTO)
                    .mapToInt(NetflowDTO::getPacketNo)
                    .sum();


                return MostPortDataTable.builder()
                    .portName(stringListEntry.getKey())
                    .portNumber(general_port)
                    .hoverDescription(description)
                    .OutgoingBytesSum(outgoingBytesSum)
                    .OutgoingPacketsSum(outgoingPacketsSum)
                    .build();
            })
            .collect(Collectors.toList());
    }

    public List<MostPortData> getMostTrafficPorts(Long back, Function<DashboardDTO, PortDTO> fn) {
        return dashboardService.buildDashboardDTO()
            .stream()
            .filter(dashboardDTO -> {
                NetflowDTO netflowDTO = dashboardDTO.getNetflowDTO();
                return LocalDate.now().minusDays(back).isBefore(netflowDTO.getDateFirstSeen())
                    || LocalDate.now().minusDays(back).isEqual(netflowDTO.getDateFirstSeen());
            }).map(
                dashboardDTO -> MostPortData.builder()
                    .bytes(getBytes(dashboardDTO.getNetflowDTO().getBytes()))
                    .localTime(LocalTime.parse(dashboardDTO.getNetflowDTO().getTimeFirstSeen()))
                    .localDate(dashboardDTO.getNetflowDTO().getDateFirstSeen())
                    .portName(fn.apply(dashboardDTO).getName())
                    .portNumber(fn.apply(dashboardDTO).getPort().toString())
                    .build())
            .filter(mostPortData -> Integer.valueOf(mostPortData.getPortNumber()) < 2000)
            .collect(Collectors.toList());
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Setter
    public static class MostPortData {

        private LocalTime localTime;

        private LocalDate localDate;

        private String portName;

        private String portNumber;

        private Integer bytes;

    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Setter
    public static class MostPortDataSummary {

        private String hour;

        private String portName;

        private String portNumber;

        private Integer bytesSum;

    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Setter
    public static class MostPortDataTable {

        private String portName;

        private String portNumber;

        private String hoverDescription;

        private Integer OutgoingBytesSum;

        private Integer IngoingBytesSum;

        private Integer OutgoingPacketsSum;

        private Integer IngoingPacketsSum;

    }
}
