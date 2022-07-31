package com.tattea.analyzer.service.snmp;

import com.tattea.analyzer.repository.TrapRepository;
import com.tattea.analyzer.service.GeneralService;
import com.tattea.analyzer.service.dto.TrapDTO;
import com.tattea.analyzer.service.mapper.TrapMapper;
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
public class TrapStatsService {

    private final TrapRepository trapRepository;

    private final TrapMapper trapMapper;

    private final LocalDate fourDaysAgo = LocalDate.now().minusDays(4L);

    @Autowired
    public TrapStatsService(TrapRepository trapRepository, TrapMapper trapMapper) {
        this.trapRepository = trapRepository;
        this.trapMapper = trapMapper;
    }

    public List<TrapDTO> getLastFourSNMPLogs() {
        return trapRepository
            .findAll()
            .stream()
            .map(trapMapper::toDto)
            .filter(netflowDTO -> netflowDTO.getDate().isAfter(fourDaysAgo))
            .collect(Collectors.toList());
    }

    public List<Long> getLastFourSNMPLogsCount() {
        List<TrapsPerHour> collect = getLastFourSNMPLogs()
            .stream()
            .collect(Collectors.groupingBy(value -> GeneralService.FourDaysPredicate.accept(value.getDate())))
            .entrySet()
            .stream()
            .map(v -> TrapsPerHour.builder().hour(v.getKey().getRange().toString()).trapDTOS(v.getValue()).build())
            .collect(Collectors.toList());

        boolean match0 = collect.stream().anyMatch(bytesPerHour -> bytesPerHour.getHour().equals("0"));
        boolean match1 = collect.stream().anyMatch(bytesPerHour -> bytesPerHour.getHour().equals("1"));
        boolean match2 = collect.stream().anyMatch(bytesPerHour -> bytesPerHour.getHour().equals("2"));
        boolean match3 = collect.stream().anyMatch(bytesPerHour -> bytesPerHour.getHour().equals("3"));

        if (!match0) {
            collect = Stream.concat(collect.stream(), Stream.of(TrapsPerHour.builder().hour("0").build())).collect(Collectors.toList());
        }

        if (!match1) {
            collect = Stream.concat(collect.stream(), Stream.of(TrapsPerHour.builder().hour("1").build())).collect(Collectors.toList());
        }

        if (!match2) {
            collect = Stream.concat(collect.stream(), Stream.of(TrapsPerHour.builder().hour("2").build())).collect(Collectors.toList());
        }

        if (!match3) {
            collect = Stream.concat(collect.stream(), Stream.of(TrapsPerHour.builder().hour("3").build())).collect(Collectors.toList());
        }

        return collect
            .stream()
            .sorted(Comparator.comparing(TrapsPerHour::getHour).reversed())
            .map(value -> {
                if (value.getTrapDTOS() == null) {
                    return 0L;
                }

                return (long) value.getTrapDTOS().size();
            })
            .collect(Collectors.toList());
    }

    @Getter
    @AllArgsConstructor
    public enum FourDaysPredicates {
        D1(0L),
        D2(1L),
        D3(2L),
        D4(3L);

        private final Long range;

        public static FourDaysPredicates accept(LocalDate localDate) {
            return Arrays
                .stream(FourDaysPredicates.values())
                .filter(value -> localDate.isEqual(LocalDate.now().minusDays(value.getRange())))
                .findFirst()
                .orElse(FourDaysPredicates.D4);
        }
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    @Setter
    public static class TrapsPerHour {

        private String hour;

        private List<TrapDTO> trapDTOS;
    }
}
