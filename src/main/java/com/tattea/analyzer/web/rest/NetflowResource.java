package com.tattea.analyzer.web.rest;

import com.tattea.analyzer.repository.NetflowRepository;
import com.tattea.analyzer.service.NetflowQueryService;
import com.tattea.analyzer.service.NetflowService;
import com.tattea.analyzer.service.criteria.NetflowCriteria;
import com.tattea.analyzer.service.dto.NetflowDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.tattea.analyzer.domain.Netflow}.
 */
@RestController
@RequestMapping("/api")
public class NetflowResource {

    private final Logger log = LoggerFactory.getLogger(NetflowResource.class);

    private static final String ENTITY_NAME = "netflow";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NetflowService netflowService;

    private final NetflowRepository netflowRepository;

    private final NetflowQueryService netflowQueryService;

    public NetflowResource(NetflowService netflowService, NetflowRepository netflowRepository, NetflowQueryService netflowQueryService) {
        this.netflowService = netflowService;
        this.netflowRepository = netflowRepository;
        this.netflowQueryService = netflowQueryService;
    }

    /**
     * {@code POST  /netflows} : Create a new netflow.
     *
     * @param netflowDTO the netflowDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new netflowDTO, or with status {@code 400 (Bad Request)} if the netflow has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/netflows")
    public ResponseEntity<NetflowDTO> createNetflow(@RequestBody NetflowDTO netflowDTO) throws URISyntaxException {
        log.debug("REST request to save Netflow : {}", netflowDTO);
        if (netflowDTO.getId() != null) {
            throw new BadRequestAlertException("A new netflow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NetflowDTO result = netflowService.save(netflowDTO);
        return ResponseEntity
            .created(new URI("/api/netflows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /netflows/:id} : Updates an existing netflow.
     *
     * @param id the id of the netflowDTO to save.
     * @param netflowDTO the netflowDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated netflowDTO,
     * or with status {@code 400 (Bad Request)} if the netflowDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the netflowDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/netflows/{id}")
    public ResponseEntity<NetflowDTO> updateNetflow(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NetflowDTO netflowDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Netflow : {}, {}", id, netflowDTO);
        if (netflowDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, netflowDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!netflowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NetflowDTO result = netflowService.update(netflowDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, netflowDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /netflows/:id} : Partial updates given fields of an existing netflow, field will ignore if it is null
     *
     * @param id the id of the netflowDTO to save.
     * @param netflowDTO the netflowDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated netflowDTO,
     * or with status {@code 400 (Bad Request)} if the netflowDTO is not valid,
     * or with status {@code 404 (Not Found)} if the netflowDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the netflowDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/netflows/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NetflowDTO> partialUpdateNetflow(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NetflowDTO netflowDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Netflow partially : {}, {}", id, netflowDTO);
        if (netflowDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, netflowDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!netflowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NetflowDTO> result = netflowService.partialUpdate(netflowDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, netflowDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /netflows} : get all the netflows.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of netflows in body.
     */
    @GetMapping("/netflows")
    public ResponseEntity<List<NetflowDTO>> getAllNetflows(
        NetflowCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Netflows by criteria: {}", criteria);
        Page<NetflowDTO> page = netflowQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /netflows/count} : count all the netflows.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/netflows/count")
    public ResponseEntity<Long> countNetflows(NetflowCriteria criteria) {
        log.debug("REST request to count Netflows by criteria: {}", criteria);
        return ResponseEntity.ok().body(netflowQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /netflows/:id} : get the "id" netflow.
     *
     * @param id the id of the netflowDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the netflowDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/netflows/{id}")
    public ResponseEntity<NetflowDTO> getNetflow(@PathVariable Long id) {
        log.debug("REST request to get Netflow : {}", id);
        Optional<NetflowDTO> netflowDTO = netflowService.findOne(id);
        return ResponseUtil.wrapOrNotFound(netflowDTO);
    }

    /**
     * {@code DELETE  /netflows/:id} : delete the "id" netflow.
     *
     * @param id the id of the netflowDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/netflows/{id}")
    public ResponseEntity<Void> deleteNetflow(@PathVariable Long id) {
        log.debug("REST request to delete Netflow : {}", id);
        netflowService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
