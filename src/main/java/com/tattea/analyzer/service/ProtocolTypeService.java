package com.tattea.analyzer.service;

import com.tattea.analyzer.repository.NetflowRepository;
import com.tattea.analyzer.service.dto.NetflowDTO;
import com.tattea.analyzer.service.mapper.NetflowMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProtocolTypeService {

    private final NetflowRepository netflowRepository;

    private final NetflowMapper netflowMapper;

    private final LocalDate fourDaysAgo = LocalDate.now().minusDays(4L);

    @Autowired
    public ProtocolTypeService(NetflowRepository netflowRepository, NetflowMapper netflowMapper) {
        this.netflowRepository = netflowRepository;
        this.netflowMapper = netflowMapper;
    }

    private static Integer getBytes(String bytes) {
        double p = bytes.contains("M") ? Double.parseDouble(bytes.replace("M", "")) * 1000000 : Double.parseDouble(bytes);
        return (int) p;
    }

    public List<NetflowDTO> getLastFourNetflow() {
        return netflowRepository
            .findAll()
            .stream()
            .map(netflowMapper::toDto)
            .filter(netflowDTO -> netflowDTO.getDateFirstSeen().isAfter(fourDaysAgo))
            .collect(Collectors.toList());
    }

    public List<NetflowDTO> getYesterdayNetflow() {
        return netflowRepository
            .findAll()
            .stream()
            .map(netflowMapper::toDto)
            .filter(netflowDTO ->
                LocalDateTime
                    .of(netflowDTO.getDateFirstSeen(), LocalTime.parse(netflowDTO.getTimeFirstSeen()))
                    .isAfter(LocalDateTime.now().minusDays(1L))
            )
            .collect(Collectors.toList());
    }

    public List<NetflowDTO> getLastHourNetflow() {
        return netflowRepository
            .findAll()
            .stream()
            .map(netflowMapper::toDto)
            .filter(netflowDTO -> netflowDTO.getDateFirstSeen().isEqual(LocalDate.now()))
            .filter(netflowDTO -> LocalTime.parse(netflowDTO.getTimeFirstSeen()).isAfter(LocalTime.now().minusHours(1)))
            .collect(Collectors.toList());
    }

    public List<Long> getUDPTCPDataPerHour() {
        Map<String, Long> collect = getLastHourNetflow()
            .stream()
            .collect(Collectors.groupingBy(NetflowDTO::getProtocol, Collectors.summingLong(proto -> getBytes(proto.getBytes()))));

        Long udp = collect.entrySet().stream().filter(map -> map.getKey().equals("UDP")).map(Map.Entry::getValue).findFirst().orElse(0L);

        Long tcp = collect.entrySet().stream().filter(map -> map.getKey().equals("TCP")).map(Map.Entry::getValue).findFirst().orElse(0L);

        return List.of(udp, tcp);
    }

    public List<Long> getUDPTCPDataYesterday() {
        Map<String, Long> collect = getYesterdayNetflow()
            .stream()
            .collect(Collectors.groupingBy(NetflowDTO::getProtocol, Collectors.summingLong(proto -> getBytes(proto.getBytes()))));

        Long udp = collect.entrySet().stream().filter(map -> map.getKey().equals("UDP")).map(Map.Entry::getValue).findFirst().orElse(0L);

        Long tcp = collect.entrySet().stream().filter(map -> map.getKey().equals("TCP")).map(Map.Entry::getValue).findFirst().orElse(0L);

        return List.of(udp, tcp);
    }

    public List<Long> getUDPTCPDataFourDays() {
        Map<String, Long> collect = getLastFourNetflow()
            .stream()
            .collect(Collectors.groupingBy(NetflowDTO::getProtocol, Collectors.summingLong(proto -> getBytes(proto.getBytes()))));

        Long udp = collect.entrySet().stream().filter(map -> map.getKey().equals("UDP")).map(Map.Entry::getValue).findFirst().orElse(0L);

        Long tcp = collect.entrySet().stream().filter(map -> map.getKey().equals("TCP")).map(Map.Entry::getValue).findFirst().orElse(0L);

        return List.of(udp, tcp);
    }
}
