package com.tattea.analyzer.service.impl;

import com.tattea.analyzer.domain.Trap;
import com.tattea.analyzer.repository.TrapRepository;
import com.tattea.analyzer.service.TrapService;
import com.tattea.analyzer.service.dto.TrapDTO;
import com.tattea.analyzer.service.mapper.TrapMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Trap}.
 */
@Service
@Transactional
public class TrapServiceImpl implements TrapService {

    private final Logger log = LoggerFactory.getLogger(TrapServiceImpl.class);

    private final TrapRepository trapRepository;

    private final TrapMapper trapMapper;

    public TrapServiceImpl(TrapRepository trapRepository, TrapMapper trapMapper) {
        this.trapRepository = trapRepository;
        this.trapMapper = trapMapper;
    }

    @Override
    public TrapDTO save(TrapDTO trapDTO) {
        log.debug("Request to save Trap : {}", trapDTO);
        Trap trap = trapMapper.toEntity(trapDTO);
        trap = trapRepository.save(trap);
        return trapMapper.toDto(trap);
    }

    @Override
    public TrapDTO update(TrapDTO trapDTO) {
        log.debug("Request to save Trap : {}", trapDTO);
        Trap trap = trapMapper.toEntity(trapDTO);
        trap = trapRepository.save(trap);
        return trapMapper.toDto(trap);
    }

    @Override
    public Optional<TrapDTO> partialUpdate(TrapDTO trapDTO) {
        log.debug("Request to partially update Trap : {}", trapDTO);

        return trapRepository
            .findById(trapDTO.getId())
            .map(existingTrap -> {
                trapMapper.partialUpdate(existingTrap, trapDTO);

                return existingTrap;
            })
            .map(trapRepository::save)
            .map(trapMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrapDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Traps");
        return trapRepository.findAll(pageable).map(trapMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrapDTO> findOne(Long id) {
        log.debug("Request to get Trap : {}", id);
        return trapRepository.findById(id).map(trapMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Trap : {}", id);
        trapRepository.deleteById(id);
    }
}
