package com.tattea.analyzer.service.port;

import com.tattea.analyzer.service.dashboard.DashboardService;
import com.tattea.analyzer.service.dto.DashboardDTO;
import com.tattea.analyzer.service.dto.NetflowDTO;
import com.tattea.analyzer.service.dto.PortDTO;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PortDashboardService {

    private final DashboardService dashboardService;

    @Autowired
    public PortDashboardService(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    private static Integer getBytes(String bytes) {
        double p = bytes.contains("M") ? Double.parseDouble(bytes.replace("M", "")) * 1000000 : Double.parseDouble(bytes);
        return (int) p;
    }

    private static Map<String, Long> groupBy(List<MostPortData> mostPortData) {
        return mostPortData
            .stream()
            .collect(Collectors.groupingBy(MostPortData::getPortName, Collectors.summingInt(MostPortData::getBytes)))
            .entrySet()
            .stream()
            .limit(10)
            .flatMap(Stream::of)
            .collect(Collectors.toMap(Map.Entry::getKey, o -> Long.valueOf(o.getValue())));
    }

    public List<MostPortData> getMostTrafficOutgoingPortsYesterday() {
        return dashboardService
            .buildDashboardDTO()
            .stream()
            .filter(dashboardDTO -> {
                NetflowDTO netflowDTO = dashboardDTO.getNetflowDTO();
                return (
                    LocalDate.now().minusDays(1).isBefore(netflowDTO.getDateFirstSeen()) ||
                    LocalDate.now().minusDays(1).isEqual(netflowDTO.getDateFirstSeen())
                );
            })
            .map(dashboardDTO -> {
                MostPortData build = MostPortData
                    .builder()
                    .bytes(getBytes(dashboardDTO.getNetflowDTO().getBytes()))
                    .localTime(LocalTime.parse(dashboardDTO.getNetflowDTO().getTimeFirstSeen()))
                    .localDate(dashboardDTO.getNetflowDTO().getDateFirstSeen())
                    .portName(dashboardDTO.getDstPort().getName())
                    .portNumber(dashboardDTO.getDstPort().getPort().toString())
                    .build();

                MostPortData build1 = MostPortData
                    .builder()
                    .bytes(getBytes(dashboardDTO.getNetflowDTO().getBytes()))
                    .localTime(LocalTime.parse(dashboardDTO.getNetflowDTO().getTimeFirstSeen()))
                    .localDate(dashboardDTO.getNetflowDTO().getDateFirstSeen())
                    .portName(dashboardDTO.getSrcPort().getName())
                    .portNumber(dashboardDTO.getSrcPort().getPort().toString())
                    .build();

                return Stream.of(build, build1);
            })
            .flatMap(mostPortDataStream -> mostPortDataStream)
            .filter(mostPortData -> Integer.parseInt(mostPortData.getPortNumber()) < 2000)
            .collect(Collectors.toList());
    }

    public List<PortWidgetData> getMostTrafficOutgoingPortsYesterdaySegregated() {
        return getMostTrafficPortsYesterdaySegregated(getMostTrafficOutgoingPortsYesterday());
    }

    public List<PortWidgetData> getMostTrafficIngoingPortsYesterdaySegregated() {
        return getMostTrafficPortsYesterdaySegregated(getMostTrafficIngoingPortsYesterday());
    }

    public List<PortWidgetData> getMostTrafficPortsYesterdaySegregated(List<MostPortData> portData) {
        List<PortWidget> portWidgets = portData
            .stream()
            .collect(Collectors.groupingBy(value -> YoloWorld.accept(value.getLocalTime())))
            .entrySet()
            .stream()
            .map(map -> PortWidget.builder().hour(map.getKey().range).portNameBytes(groupBy(map.getValue())).build())
            .collect(Collectors.toList());

        List<PortWidgetData> portWidgetData = portWidgets
            .stream()
            .map(portWidget -> portWidget.getPortNameBytes().keySet())
            .flatMap(Collection::stream)
            .distinct()
            .map(strings -> {
                List<Integer> collect = Arrays.stream(Collections.nCopies(24, 0).toArray(new Integer[24])).collect(Collectors.toList());

                return PortWidgetData.builder().portName(strings).bytesPerHour(collect).build();
            })
            .collect(Collectors.toList());

        portWidgetData.forEach(portWidgetDataStillEmpty -> {
            String portName = portWidgetDataStillEmpty.getPortName();
            portWidgets
                .stream()
                .filter(portWidget -> portWidget.getPortNameBytes().keySet().stream().anyMatch(s -> s.equals(portName)))
                .forEach(portWidget -> {
                    Integer aLong = portWidget
                        .getPortNameBytes()
                        .entrySet()
                        .stream()
                        .filter(stringLongEntry -> stringLongEntry.getKey().equals(portName))
                        .findFirst()
                        .map(Map.Entry::getValue)
                        .map(Long::intValue)
                        .orElse(0);

                    portWidgetDataStillEmpty.getBytesPerHour().set(portWidget.getHour().intValue(), aLong);
                });
        });

        return portWidgetData;
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

    /* public List<MostPortDataSummary> getMostTrafficPortsYesterdaySegregated(List<MostPortData> portData) {

        List<MostPortData> collect = IntStream.of(0, 23)
            .boxed()
            .map(integer -> portData
                .stream()
                .filter(mostPortData -> mostPortData.getLocalTime().isAfter(LocalTime.now().minusHours(integer + 1))
                    && mostPortData.getLocalTime().isBefore(LocalTime.now().minusHours(integer)))
                .collect(Collectors.toList()))
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

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
    }*/

    public List<MostPortDataTable> getMostPortDataTableForLastHour(Function<DashboardDTO, PortDTO> fn) {
        return dashboardService
            .buildDashboardDTO()
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
                String general_port = stringListEntry
                    .getValue()
                    .stream()
                    .findFirst()
                    .map(fn)
                    .map(PortDTO::getPort)
                    .map(String::valueOf)
                    .orElse("General Port");

                // Description
                String description = stringListEntry
                    .getValue()
                    .stream()
                    .findFirst()
                    .map(fn)
                    .map(PortDTO::getDescription)
                    .orElse("See Port Info Page");

                // OutgoingBytesSum
                int outgoingBytesSum = stringListEntry
                    .getValue()
                    .stream()
                    .map(DashboardDTO::getNetflowDTO)
                    .map(NetflowDTO::getBytes)
                    .mapToInt(PortDashboardService::getBytes)
                    .sum();

                // OutgoingPacketsSum
                int outgoingPacketsSum = stringListEntry
                    .getValue()
                    .stream()
                    .map(DashboardDTO::getNetflowDTO)
                    .mapToInt(NetflowDTO::getPacketNo)
                    .sum();

                return MostPortDataTable
                    .builder()
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
        return dashboardService
            .buildDashboardDTO()
            .stream()
            .filter(dashboardDTO -> {
                NetflowDTO netflowDTO = dashboardDTO.getNetflowDTO();
                return (
                    LocalDate.now().isEqual(netflowDTO.getDateFirstSeen()) ||
                    LocalDate.now().minusDays(1).isEqual(netflowDTO.getDateFirstSeen())
                );
            })
            .collect(Collectors.groupingBy(dashboardDTO -> dashboardDTO.getDstPort().getName(), Collectors.toList()))
            .entrySet()
            .stream()
            .flatMap(Stream::of)
            .map(stringListEntry -> {
                // Port Number
                String general_port = stringListEntry
                    .getValue()
                    .stream()
                    .findFirst()
                    .map(DashboardDTO::getDstPort)
                    .map(PortDTO::getPort)
                    .map(String::valueOf)
                    .orElse("General Port");

                // Description
                String description = stringListEntry
                    .getValue()
                    .stream()
                    .findFirst()
                    .map(DashboardDTO::getDstPort)
                    .map(PortDTO::getDescription)
                    .orElse("See Port Info Page");

                // OutgoingBytesSum
                int outgoingBytesSum = stringListEntry
                    .getValue()
                    .stream()
                    .map(DashboardDTO::getNetflowDTO)
                    .map(NetflowDTO::getBytes)
                    .mapToInt(PortDashboardService::getBytes)
                    .sum();

                // OutgoingPacketsSum
                int outgoingPacketsSum = stringListEntry
                    .getValue()
                    .stream()
                    .map(DashboardDTO::getNetflowDTO)
                    .mapToInt(NetflowDTO::getPacketNo)
                    .sum();

                return MostPortDataTable
                    .builder()
                    .portName(stringListEntry.getKey())
                    .portNumber(general_port)
                    .hoverDescription(description)
                    .OutgoingBytesSum(outgoingBytesSum)
                    .OutgoingPacketsSum(outgoingPacketsSum)
                    .build();
            })
            .filter(mostPortDataTable -> Long.valueOf(mostPortDataTable.getPortNumber()) < 2000)
            .collect(Collectors.toList());
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
        return dashboardService
            .buildDashboardDTO()
            .stream()
            .filter(dashboardDTO -> {
                NetflowDTO netflowDTO = dashboardDTO.getNetflowDTO();
                return (
                    LocalDate.now().minusDays(back).isBefore(netflowDTO.getDateFirstSeen()) &&
                    LocalDate.now().minusDays(back).isEqual(netflowDTO.getDateFirstSeen())
                );
            })
            .collect(Collectors.groupingBy(dashboardDTO -> fn.apply(dashboardDTO).getName(), Collectors.toList()))
            .entrySet()
            .stream()
            .flatMap(Stream::of)
            .map(stringListEntry -> {
                // Port Number
                String general_port = stringListEntry
                    .getValue()
                    .stream()
                    .findFirst()
                    .map(fn)
                    .map(PortDTO::getPort)
                    .map(String::valueOf)
                    .orElse("General Port");

                // Description
                String description = stringListEntry
                    .getValue()
                    .stream()
                    .findFirst()
                    .map(fn)
                    .map(PortDTO::getDescription)
                    .orElse("See Port Info Page");

                // OutgoingBytesSum
                int outgoingBytesSum = stringListEntry
                    .getValue()
                    .stream()
                    .map(DashboardDTO::getNetflowDTO)
                    .map(NetflowDTO::getBytes)
                    .mapToInt(PortDashboardService::getBytes)
                    .sum();

                // OutgoingPacketsSum
                int outgoingPacketsSum = stringListEntry
                    .getValue()
                    .stream()
                    .map(DashboardDTO::getNetflowDTO)
                    .mapToInt(NetflowDTO::getPacketNo)
                    .sum();

                return MostPortDataTable
                    .builder()
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
        return dashboardService
            .buildDashboardDTO()
            .stream()
            .filter(dashboardDTO -> {
                NetflowDTO netflowDTO = dashboardDTO.getNetflowDTO();
                return (
                    LocalDate.now().minusDays(back).isBefore(netflowDTO.getDateFirstSeen()) ||
                    LocalDate.now().minusDays(back).isEqual(netflowDTO.getDateFirstSeen())
                );
            })
            .map(dashboardDTO ->
                MostPortData
                    .builder()
                    .bytes(getBytes(dashboardDTO.getNetflowDTO().getBytes()))
                    .localTime(LocalTime.parse(dashboardDTO.getNetflowDTO().getTimeFirstSeen()))
                    .localDate(dashboardDTO.getNetflowDTO().getDateFirstSeen())
                    .portName(fn.apply(dashboardDTO).getName())
                    .portNumber(fn.apply(dashboardDTO).getPort().toString())
                    .build()
            )
            .filter(mostPortData -> Integer.valueOf(mostPortData.getPortNumber()) < 2000)
            .collect(Collectors.toList());
    }

    @Getter
    @AllArgsConstructor
    public enum YoloWorld {
        T1(1L),
        T2(2L),
        T3(3L),
        T4(4L),
        T5(5L),
        T6(6L),
        T7(7L),
        T8(8L),
        T9(9L),
        T10(10L),
        T11(11L),
        T12(12L),
        T13(13L),
        T14(14L),
        T15(15L),
        T16(16L),
        T17(17L),
        T18(18L),
        T19(19L),
        T20(20L),
        T21(21L),
        T22(22L),
        T23(23L),
        T0(0L);

        public final Long range;

        public static YoloWorld accept(LocalTime localTime) {
            return Arrays
                .stream(YoloWorld.values())
                .filter(value ->
                    localTime.isAfter(LocalTime.MIDNIGHT.plusHours(value.getRange())) &&
                    localTime.isBefore(LocalTime.MIDNIGHT.minusNanos(1).plusHours(value.getRange() + 1L))
                )
                .findFirst()
                .orElse(T0);
        }
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Setter
    public static class PortWidgetData {

        private String portName;

        private List<Integer> bytesPerHour;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Setter
    public static class PortWidget {

        private Long hour;

        private Map<String, Long> portNameBytes;
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
