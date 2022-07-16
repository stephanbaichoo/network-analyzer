package com.tattea.analyzer.web.rest;

import com.tattea.analyzer.repository.TrapRepository;
import com.tattea.analyzer.service.TrapService;
import com.tattea.analyzer.service.dto.TrapDTO;
import com.tattea.analyzer.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.tattea.analyzer.domain.Trap}.
 */
@RestController
@RequestMapping("/api")
public class TrapResource {

    private final Logger log = LoggerFactory.getLogger(TrapResource.class);

    private static final String ENTITY_NAME = "trap";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrapService trapService;

    private final TrapRepository trapRepository;

    public TrapResource(TrapService trapService, TrapRepository trapRepository) {
        this.trapService = trapService;
        this.trapRepository = trapRepository;
    }

    /**
     * {@code POST  /traps} : Create a new trap.
     *
     * @param trapDTO the trapDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trapDTO, or with status {@code 400 (Bad Request)} if the trap has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/traps")
    public ResponseEntity<TrapDTO> createTrap(@RequestBody TrapDTO trapDTO) throws URISyntaxException {
        log.debug("REST request to save Trap : {}", trapDTO);
        if (trapDTO.getId() != null) {
            throw new BadRequestAlertException("A new trap cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrapDTO result = trapService.save(trapDTO);
        return ResponseEntity
            .created(new URI("/api/traps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /traps/:id} : Updates an existing trap.
     *
     * @param id the id of the trapDTO to save.
     * @param trapDTO the trapDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trapDTO,
     * or with status {@code 400 (Bad Request)} if the trapDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trapDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/traps/{id}")
    public ResponseEntity<TrapDTO> updateTrap(@PathVariable(value = "id", required = false) final Long id, @RequestBody TrapDTO trapDTO)
        throws URISyntaxException {
        log.debug("REST request to update Trap : {}, {}", id, trapDTO);
        if (trapDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trapDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trapRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TrapDTO result = trapService.update(trapDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trapDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /traps/:id} : Partial updates given fields of an existing trap, field will ignore if it is null
     *
     * @param id the id of the trapDTO to save.
     * @param trapDTO the trapDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trapDTO,
     * or with status {@code 400 (Bad Request)} if the trapDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trapDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trapDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/traps/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrapDTO> partialUpdateTrap(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrapDTO trapDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Trap partially : {}, {}", id, trapDTO);
        if (trapDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trapDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trapRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrapDTO> result = trapService.partialUpdate(trapDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trapDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /traps} : get all the traps.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of traps in body.
     */
    @GetMapping("/traps")
    public ResponseEntity<List<TrapDTO>> getAllTraps(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Traps");
        Page<TrapDTO> page = trapService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /traps/:id} : get the "id" trap.
     *
     * @param id the id of the trapDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trapDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/traps/{id}")
    public ResponseEntity<TrapDTO> getTrap(@PathVariable Long id) {
        log.debug("REST request to get Trap : {}", id);
        Optional<TrapDTO> trapDTO = trapService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trapDTO);
    }

    /**
     * {@code DELETE  /traps/:id} : delete the "id" trap.
     *
     * @param id the id of the trapDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/traps/{id}")
    public ResponseEntity<Void> deleteTrap(@PathVariable Long id) {
        log.debug("REST request to delete Trap : {}", id);
        trapService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
