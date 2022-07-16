package com.tattea.analyzer.service;

import com.tattea.analyzer.service.dto.HostDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.tattea.analyzer.domain.Host}.
 */
public interface HostService {
    /**
     * Save a host.
     *
     * @param hostDTO the entity to save.
     * @return the persisted entity.
     */
    HostDTO save(HostDTO hostDTO);

    /**
     * Updates a host.
     *
     * @param hostDTO the entity to update.
     * @return the persisted entity.
     */
    HostDTO update(HostDTO hostDTO);

    /**
     * Partially updates a host.
     *
     * @param hostDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HostDTO> partialUpdate(HostDTO hostDTO);

    /**
     * Get all the hosts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HostDTO> findAll(Pageable pageable);

    /**
     * Get the "id" host.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HostDTO> findOne(Long id);

    /**
     * Get the "id" host.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HostDTO> findOneByIp(String ip);

    /**
     * Delete the "id" host.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
