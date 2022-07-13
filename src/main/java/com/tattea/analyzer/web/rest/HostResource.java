package com.tattea.analyzer.web.rest;

import com.tattea.analyzer.repository.HostRepository;
import com.tattea.analyzer.service.HostService;
import com.tattea.analyzer.service.dto.HostDTO;
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
 * REST controller for managing {@link com.tattea.analyzer.domain.Host}.
 */
@RestController
@RequestMapping("/api")
public class HostResource {

    private final Logger log = LoggerFactory.getLogger(HostResource.class);

    private static final String ENTITY_NAME = "host";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HostService hostService;

    private final HostRepository hostRepository;

    public HostResource(HostService hostService, HostRepository hostRepository) {
        this.hostService = hostService;
        this.hostRepository = hostRepository;
    }

    /**
     * {@code POST  /hosts} : Create a new host.
     *
     * @param hostDTO the hostDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hostDTO, or with status {@code 400 (Bad Request)} if the host has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hosts")
    public ResponseEntity<HostDTO> createHost(@RequestBody HostDTO hostDTO) throws URISyntaxException {
        log.debug("REST request to save Host : {}", hostDTO);
        if (hostDTO.getId() != null) {
            throw new BadRequestAlertException("A new host cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HostDTO result = hostService.save(hostDTO);
        return ResponseEntity
            .created(new URI("/api/hosts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hosts/:id} : Updates an existing host.
     *
     * @param id the id of the hostDTO to save.
     * @param hostDTO the hostDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hostDTO,
     * or with status {@code 400 (Bad Request)} if the hostDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hostDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hosts/{id}")
    public ResponseEntity<HostDTO> updateHost(@PathVariable(value = "id", required = false) final Long id, @RequestBody HostDTO hostDTO)
        throws URISyntaxException {
        log.debug("REST request to update Host : {}, {}", id, hostDTO);
        if (hostDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hostDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HostDTO result = hostService.update(hostDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, hostDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /hosts/:id} : Partial updates given fields of an existing host, field will ignore if it is null
     *
     * @param id the id of the hostDTO to save.
     * @param hostDTO the hostDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hostDTO,
     * or with status {@code 400 (Bad Request)} if the hostDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hostDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hostDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/hosts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HostDTO> partialUpdateHost(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HostDTO hostDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Host partially : {}, {}", id, hostDTO);
        if (hostDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hostDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HostDTO> result = hostService.partialUpdate(hostDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, hostDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hosts} : get all the hosts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hosts in body.
     */
    @GetMapping("/hosts")
    public ResponseEntity<List<HostDTO>> getAllHosts(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Hosts");
        Page<HostDTO> page = hostService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hosts/:id} : get the "id" host.
     *
     * @param id the id of the hostDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hostDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hosts/{id}")
    public ResponseEntity<HostDTO> getHost(@PathVariable Long id) {
        log.debug("REST request to get Host : {}", id);
        Optional<HostDTO> hostDTO = hostService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hostDTO);
    }

    /**
     * {@code DELETE  /hosts/:id} : delete the "id" host.
     *
     * @param id the id of the hostDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hosts/{id}")
    public ResponseEntity<Void> deleteHost(@PathVariable Long id) {
        log.debug("REST request to delete Host : {}", id);
        hostService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
