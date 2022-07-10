package com.tattea.analyzer.web.rest;

import com.tattea.analyzer.repository.PortRepository;
import com.tattea.analyzer.service.PortService;
import com.tattea.analyzer.service.dto.PortDTO;
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
 * REST controller for managing {@link com.tattea.analyzer.domain.Port}.
 */
@RestController
@RequestMapping("/api")
public class PortResource {

    private final Logger log = LoggerFactory.getLogger(PortResource.class);

    private static final String ENTITY_NAME = "port";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PortService portService;

    private final PortRepository portRepository;

    public PortResource(PortService portService, PortRepository portRepository) {
        this.portService = portService;
        this.portRepository = portRepository;
    }

    /**
     * {@code POST  /ports} : Create a new port.
     *
     * @param portDTO the portDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new portDTO, or with status {@code 400 (Bad Request)} if the port has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ports")
    public ResponseEntity<PortDTO> createPort(@RequestBody PortDTO portDTO) throws URISyntaxException {
        log.debug("REST request to save Port : {}", portDTO);
        if (portDTO.getId() != null) {
            throw new BadRequestAlertException("A new port cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PortDTO result = portService.save(portDTO);
        return ResponseEntity
            .created(new URI("/api/ports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ports/:id} : Updates an existing port.
     *
     * @param id the id of the portDTO to save.
     * @param portDTO the portDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated portDTO,
     * or with status {@code 400 (Bad Request)} if the portDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the portDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ports/{id}")
    public ResponseEntity<PortDTO> updatePort(@PathVariable(value = "id", required = false) final Long id, @RequestBody PortDTO portDTO)
        throws URISyntaxException {
        log.debug("REST request to update Port : {}, {}", id, portDTO);
        if (portDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, portDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!portRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PortDTO result = portService.update(portDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, portDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ports/:id} : Partial updates given fields of an existing port, field will ignore if it is null
     *
     * @param id the id of the portDTO to save.
     * @param portDTO the portDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated portDTO,
     * or with status {@code 400 (Bad Request)} if the portDTO is not valid,
     * or with status {@code 404 (Not Found)} if the portDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the portDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PortDTO> partialUpdatePort(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PortDTO portDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Port partially : {}, {}", id, portDTO);
        if (portDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, portDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!portRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PortDTO> result = portService.partialUpdate(portDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, portDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ports} : get all the ports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ports in body.
     */
    @GetMapping("/ports")
    public ResponseEntity<List<PortDTO>> getAllPorts(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Ports");
        Page<PortDTO> page = portService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ports/:id} : get the "id" port.
     *
     * @param id the id of the portDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the portDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ports/{id}")
    public ResponseEntity<PortDTO> getPort(@PathVariable Long id) {
        log.debug("REST request to get Port : {}", id);
        Optional<PortDTO> portDTO = portService.findOne(id);
        return ResponseUtil.wrapOrNotFound(portDTO);
    }

    /**
     * {@code DELETE  /ports/:id} : delete the "id" port.
     *
     * @param id the id of the portDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ports/{id}")
    public ResponseEntity<Void> deletePort(@PathVariable Long id) {
        log.debug("REST request to delete Port : {}", id);
        portService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
