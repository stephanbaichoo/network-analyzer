package com.tattea.analyzer.service;

import com.tattea.analyzer.domain.Netflow;
import com.tattea.analyzer.repository.NetflowRepository;
import com.tattea.analyzer.service.dto.NetflowDTO;
import com.tattea.analyzer.service.mapper.NetflowMapper;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    public List<NetflowDTO> getLastFourNetflowData() {
        return netflowRepository
            .findAll()
            .stream()
            .map(netflowMapper::toDto)
            .filter(netflowDTO -> netflowDTO.getDateFirstSeen().isAfter(fourDaysAgo))
            .collect(Collectors.toList());
    }

    public List<Long> getLastFourBytesSum() {
        return getLastFourNetflowData()
            .stream()
            .collect(Collectors.groupingBy(value -> FourDaysPredicate.accept(value.getDateFirstSeen())))
            .entrySet()
            .stream()
            .map(v -> BytesPerHour.builder().hour(v.getKey().getRange().toString()).netflowDTOS(v.getValue()).build())
            .sorted(Comparator.comparing(BytesPerHour::getHour))
            .map(value -> value.getNetflowDTOS().stream().mapToLong(netflow -> getBytes(netflow.getBytes())).sum())
            .collect(Collectors.toList());
    }

    private static Integer getBytes(String bytes) {
        double p = bytes.contains("M") ? Double.parseDouble(bytes.replace("M", "")) * 1000000 : Double.parseDouble(bytes);
        return (int) p;
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
}
