package com.tattea.analyzer.service;

import com.tattea.analyzer.repository.NetflowRepository;
import com.tattea.analyzer.service.dto.NetflowDTO;
import com.tattea.analyzer.service.mapper.NetflowMapper;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneralService {

    private final NetflowRepository netflowRepository;

    private final NetflowMapper netflowMapper;

    private final LocalDate fourDaysAgo = LocalDate.now().minusDays(4L);

    @Autowired
    public GeneralService(NetflowRepository netflowRepository, NetflowMapper netflowMapper) {
        this.netflowRepository = netflowRepository;
        this.netflowMapper = netflowMapper;
    }

    private static Integer getBytes(String bytes) {
        double p = bytes.contains("M") ? Double.parseDouble(bytes.replace("M", "")) * 1000000 : Double.parseDouble(bytes);
        return (int) p;
    }

    public List<NetflowDTO> getLastFourNetflowData() {
        return netflowRepository
            .findAll()
            .stream()
            .map(netflowMapper::toDto)
            .filter(netflowDTO -> netflowDTO.getDateFirstSeen().isAfter(fourDaysAgo))
            .collect(Collectors.toList());
    }

    public List<Integer> getLastFourPacketsSum() {
        List<BytesPerHour> bytesPerHourStream = getLastFourNetflowData()
            .stream()
            .collect(Collectors.groupingBy(value -> FourDaysPredicate.accept(value.getDateFirstSeen())))
            .entrySet()
            .stream()
            .map(v -> BytesPerHour.builder().hour(v.getKey().getRange().toString()).netflowDTOS(v.getValue()).build())
            .collect(Collectors.toList());

        List<BytesPerHour> collect = bytesPerHourStream;

        boolean match0 = collect.stream().anyMatch(bytesPerHour -> bytesPerHour.getHour().equals("0"));
        boolean match1 = collect.stream().anyMatch(bytesPerHour -> bytesPerHour.getHour().equals("1"));
        boolean match2 = collect.stream().anyMatch(bytesPerHour -> bytesPerHour.getHour().equals("2"));
        boolean match3 = collect.stream().anyMatch(bytesPerHour -> bytesPerHour.getHour().equals("3"));

        if (!match0) {
            collect = Stream.concat(collect.stream(), Stream.of(BytesPerHour.builder().hour("0").build())).collect(Collectors.toList());
        }

        if (!match1) {
            collect = Stream.concat(collect.stream(), Stream.of(BytesPerHour.builder().hour("1").build())).collect(Collectors.toList());
        }

        if (!match2) {
            collect = Stream.concat(collect.stream(), Stream.of(BytesPerHour.builder().hour("2").build())).collect(Collectors.toList());
        }

        if (!match3) {
            collect = Stream.concat(collect.stream(), Stream.of(BytesPerHour.builder().hour("3").build())).collect(Collectors.toList());
        }

        return collect
            .stream()
            .sorted(Comparator.comparing(BytesPerHour::getHour).reversed())
            .map(value -> {
                if (value.getNetflowDTOS() == null) {
                    return 0;
                }

                return value.getNetflowDTOS().stream().mapToInt(NetflowDTO::getPacketNo).sum();
            })
            .collect(Collectors.toList());
    }

    public List<Long> getLastFourBytesSum() {
        Stream<BytesPerHour> bytesPerHourStream = getLastFourNetflowData()
            .stream()
            .collect(Collectors.groupingBy(value -> FourDaysPredicate.accept(value.getDateFirstSeen())))
            .entrySet()
            .stream()
            .map(v -> BytesPerHour.builder().hour(v.getKey().getRange().toString()).netflowDTOS(v.getValue()).build());

        List<BytesPerHour> collect = bytesPerHourStream.collect(Collectors.toList());

        boolean match0 = collect.stream().anyMatch(bytesPerHour -> bytesPerHour.getHour().equals("0"));
        boolean match1 = collect.stream().anyMatch(bytesPerHour -> bytesPerHour.getHour().equals("1"));
        boolean match2 = collect.stream().anyMatch(bytesPerHour -> bytesPerHour.getHour().equals("2"));
        boolean match3 = collect.stream().anyMatch(bytesPerHour -> bytesPerHour.getHour().equals("3"));

        if (!match0) {
            collect = Stream.concat(collect.stream(), Stream.of(BytesPerHour.builder().hour("0").build())).collect(Collectors.toList());
        }

        if (!match1) {
            collect = Stream.concat(collect.stream(), Stream.of(BytesPerHour.builder().hour("1").build())).collect(Collectors.toList());
        }

        if (!match2) {
            collect = Stream.concat(collect.stream(), Stream.of(BytesPerHour.builder().hour("2").build())).collect(Collectors.toList());
        }

        if (!match3) {
            collect = Stream.concat(collect.stream(), Stream.of(BytesPerHour.builder().hour("3").build())).collect(Collectors.toList());
        }

        return collect
            .stream()
            .sorted(Comparator.comparing(BytesPerHour::getHour).reversed())
            .map(value -> {
                if (value.getNetflowDTOS() == null) {
                    return 0L;
                }

                return value.getNetflowDTOS().stream().mapToLong(netflow -> getBytes(netflow.getBytes())).sum();
            })
            .collect(Collectors.toList());
    }

    @Getter
    @AllArgsConstructor
    public enum FourDaysPredicate {
        D1(0L),
        D2(1L),
        D3(2L),
        D4(3L);

        private final Long range;

        public static FourDaysPredicate accept(LocalDate localDate) {
            return Arrays
                .stream(FourDaysPredicate.values())
                .filter(value -> localDate.isEqual(LocalDate.now().minusDays(value.getRange())))
                .findFirst()
                .orElse(D4);
        }
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Setter
    public static class BytesPerHour {

        private String hour;

        private List<NetflowDTO> netflowDTOS;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Setter
    public static class PacketsPerHour {

        private Long packets;

        private List<NetflowDTO> netflowDTOS;
    }
}
