package com.tattea.analyzer.service.impl;

import com.tattea.analyzer.domain.Port;
import com.tattea.analyzer.repository.PortRepository;
import com.tattea.analyzer.service.PortService;
import com.tattea.analyzer.service.dto.PortDTO;
import com.tattea.analyzer.service.mapper.PortMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Port}.
 */
@Service
@Transactional
public class PortServiceImpl implements PortService {


    private final Logger log = LoggerFactory.getLogger(PortServiceImpl.class);

    private final PortRepository portRepository;

    private final PortMapper portMapper;

    public PortServiceImpl(PortRepository portRepository, PortMapper portMapper) {
        this.portRepository = portRepository;
        this.portMapper = portMapper;
    }

    @Override
    public Optional<PortDTO> findOneByPort(Long port) {
        return portRepository.findPortByPort(port).map(portMapper::toDto);
    }

    @Override
    public PortDTO save(PortDTO portDTO) {
        log.debug("Request to save Port : {}", portDTO);
        Port port = portMapper.toEntity(portDTO);
        port = portRepository.save(port);
        return portMapper.toDto(port);
    }

    @Override
    public PortDTO update(PortDTO portDTO) {
        log.debug("Request to save Port : {}", portDTO);
        Port port = portMapper.toEntity(portDTO);
        port = portRepository.save(port);
        return portMapper.toDto(port);
    }

    @Override
    public Optional<PortDTO> partialUpdate(PortDTO portDTO) {
        log.debug("Request to partially update Port : {}", portDTO);

        return portRepository
            .findById(portDTO.getId())
            .map(existingPort -> {
                portMapper.partialUpdate(existingPort, portDTO);

                return existingPort;
            })
            .map(portRepository::save)
            .map(portMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PortDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ports");
        return portRepository.findAll(pageable).map(portMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PortDTO> findOne(Long id) {
        log.debug("Request to get Port : {}", id);
        return portRepository.findById(id).map(portMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Port : {}", id);
        portRepository.deleteById(id);
    }
}
