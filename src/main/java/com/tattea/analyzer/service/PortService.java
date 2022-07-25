package com.tattea.analyzer.service;

import com.tattea.analyzer.service.dto.PortDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.tattea.analyzer.domain.Port}.
 */
public interface PortService {
    /**
     * Save a port.
     *
     * @param portDTO the entity to save.
     * @return the persisted entity.
     */
    PortDTO save(PortDTO portDTO);

    /**
     * Updates a port.
     *
     * @param portDTO the entity to update.
     * @return the persisted entity.
     */
    PortDTO update(PortDTO portDTO);

    /**
     * Partially updates a port.
     *
     * @param portDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PortDTO> partialUpdate(PortDTO portDTO);

    /**
     * Get all the ports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PortDTO> findAll(Pageable pageable);

    /**
     * Get the "id" port.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PortDTO> findOne(Long id);

    /**
     * Get the "id" port.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PortDTO> findOneByPort(Long port);

    /**
     * Delete the "id" port.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
