package com.tattea.analyzer.service.impl;

import com.tattea.analyzer.domain.Host;
import com.tattea.analyzer.repository.HostRepository;
import com.tattea.analyzer.service.HostService;
import com.tattea.analyzer.service.dto.HostDTO;
import com.tattea.analyzer.service.mapper.HostMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Host}.
 */
@Service
@Transactional
public class HostServiceImpl implements HostService {

    private final Logger log = LoggerFactory.getLogger(HostServiceImpl.class);

    private final HostRepository hostRepository;

    private final HostMapper hostMapper;

    public HostServiceImpl(HostRepository hostRepository, HostMapper hostMapper) {
        this.hostRepository = hostRepository;
        this.hostMapper = hostMapper;
    }

    @Override
    public HostDTO save(HostDTO hostDTO) {
        log.debug("Request to save Host : {}", hostDTO);
        Host host = hostMapper.toEntity(hostDTO);
        host = hostRepository.save(host);
        return hostMapper.toDto(host);
    }

    @Override
    public HostDTO update(HostDTO hostDTO) {
        log.debug("Request to save Host : {}", hostDTO);
        Host host = hostMapper.toEntity(hostDTO);
        host = hostRepository.save(host);
        return hostMapper.toDto(host);
    }

    @Override
    public Optional<HostDTO> partialUpdate(HostDTO hostDTO) {
        log.debug("Request to partially update Host : {}", hostDTO);

        return hostRepository
            .findById(hostDTO.getId())
            .map(existingHost -> {
                hostMapper.partialUpdate(existingHost, hostDTO);

                return existingHost;
            })
            .map(hostRepository::save)
            .map(hostMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HostDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Hosts");
        return hostRepository.findAll(pageable).map(hostMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HostDTO> findOne(Long id) {
        log.debug("Request to get Host : {}", id);
        return hostRepository.findById(id).map(hostMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Host : {}", id);
        hostRepository.deleteById(id);
    }
}
