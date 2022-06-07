package com.tattea.analyzer.service.impl;

import com.tattea.analyzer.domain.Netflow;
import com.tattea.analyzer.repository.NetflowRepository;
import com.tattea.analyzer.service.NetflowService;
import com.tattea.analyzer.service.dto.NetflowDTO;
import com.tattea.analyzer.service.mapper.NetflowMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Netflow}.
 */
@Service
@Transactional
public class NetflowServiceImpl implements NetflowService {

    private final Logger log = LoggerFactory.getLogger(NetflowServiceImpl.class);

    private final NetflowRepository netflowRepository;

    private final NetflowMapper netflowMapper;

    public NetflowServiceImpl(NetflowRepository netflowRepository, NetflowMapper netflowMapper) {
        this.netflowRepository = netflowRepository;
        this.netflowMapper = netflowMapper;
    }

    @Override
    public NetflowDTO save(NetflowDTO netflowDTO) {
        log.debug("Request to save Netflow : {}", netflowDTO);
        Netflow netflow = netflowMapper.toEntity(netflowDTO);
        netflow = netflowRepository.save(netflow);
        return netflowMapper.toDto(netflow);
    }

    @Override
    public NetflowDTO update(NetflowDTO netflowDTO) {
        log.debug("Request to save Netflow : {}", netflowDTO);
        Netflow netflow = netflowMapper.toEntity(netflowDTO);
        netflow = netflowRepository.save(netflow);
        return netflowMapper.toDto(netflow);
    }

    @Override
    public Optional<NetflowDTO> partialUpdate(NetflowDTO netflowDTO) {
        log.debug("Request to partially update Netflow : {}", netflowDTO);

        return netflowRepository
            .findById(netflowDTO.getId())
            .map(existingNetflow -> {
                netflowMapper.partialUpdate(existingNetflow, netflowDTO);

                return existingNetflow;
            })
            .map(netflowRepository::save)
            .map(netflowMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NetflowDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Netflows");
        return netflowRepository.findAll(pageable).map(netflowMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NetflowDTO> findOne(Long id) {
        log.debug("Request to get Netflow : {}", id);
        return netflowRepository.findById(id).map(netflowMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Netflow : {}", id);
        netflowRepository.deleteById(id);
    }
}
