package com.tattea.analyzer.service;

import com.tattea.analyzer.service.dto.TrapDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.tattea.analyzer.domain.Trap}.
 */
public interface TrapService {
    /**
     * Save a trap.
     *
     * @param trapDTO the entity to save.
     * @return the persisted entity.
     */
    TrapDTO save(TrapDTO trapDTO);

    /**
     * Updates a trap.
     *
     * @param trapDTO the entity to update.
     * @return the persisted entity.
     */
    TrapDTO update(TrapDTO trapDTO);

    /**
     * Partially updates a trap.
     *
     * @param trapDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TrapDTO> partialUpdate(TrapDTO trapDTO);

    /**
     * Get all the traps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TrapDTO> findAll(Pageable pageable);

    /**
     * Get the "id" trap.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TrapDTO> findOne(Long id);

    /**
     * Delete the "id" trap.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
