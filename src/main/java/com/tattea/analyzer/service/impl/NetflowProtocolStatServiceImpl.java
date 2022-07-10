package com.tattea.analyzer.service.impl;

import com.tattea.analyzer.repository.NetflowRepository;
import com.tattea.analyzer.service.dto.NetflowProtocolStat;
import com.tattea.analyzer.service.mapper.NetflowProtocolStatMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.tattea.analyzer.service.dto.NetflowProtocolStat}.
 */
@Service
@Transactional
public class NetflowProtocolStatServiceImpl {

    private final NetflowRepository netflowRepository;

    private final NetflowProtocolStatMapper netflowProtocolStatMapper;

    @Autowired
    public NetflowProtocolStatServiceImpl(NetflowRepository netflowRepository, NetflowProtocolStatMapper netflowProtocolStatMapper) {
        this.netflowRepository = netflowRepository;
        this.netflowProtocolStatMapper = netflowProtocolStatMapper;
    }

    public List<NetflowProtocolStat> getNetflowProtocolStat(String protocol, Integer lastHour, Integer lastDay) {
        return netflowRepository
            .findAll()
            .stream()
            .map(netflowProtocolStatMapper::toNetflowProtocolStat)
            //.filter(netflowProtocolStat -> netflowProtocolStat.getLocalDateTime())
            .collect(Collectors.toList());
    }
}
