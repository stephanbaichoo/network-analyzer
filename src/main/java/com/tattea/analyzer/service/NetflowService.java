package com.tattea.analyzer.service;

import com.tattea.analyzer.service.dto.NetflowDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.tattea.analyzer.domain.Netflow}.
 */
public interface NetflowService {
    /**
     * Save a netflow.
     *
     * @param netflowDTO the entity to save.
     * @return the persisted entity.
     */
    NetflowDTO save(NetflowDTO netflowDTO);

    /**
     * Updates a netflow.
     *
     * @param netflowDTO the entity to update.
     * @return the persisted entity.
     */
    NetflowDTO update(NetflowDTO netflowDTO);

    /**
     * Partially updates a netflow.
     *
     * @param netflowDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NetflowDTO> partialUpdate(NetflowDTO netflowDTO);

    /**
     * Get all the netflows.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NetflowDTO> findAll(Pageable pageable);

    /**
     * Get the "id" netflow.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NetflowDTO> findOne(Long id);

    /**
     * Delete the "id" netflow.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
