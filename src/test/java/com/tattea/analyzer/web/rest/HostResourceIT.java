package com.tattea.analyzer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tattea.analyzer.IntegrationTest;
import com.tattea.analyzer.domain.Host;
import com.tattea.analyzer.repository.HostRepository;
import com.tattea.analyzer.service.dto.HostDTO;
import com.tattea.analyzer.service.mapper.HostMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HostResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HostResourceIT {

    private static final String DEFAULT_HOST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_HOST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_ASNAME = "AAAAAAAAAA";
    private static final String UPDATED_ASNAME = "BBBBBBBBBB";

    private static final String DEFAULT_ORG = "AAAAAAAAAA";
    private static final String UPDATED_ORG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/hosts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private HostMapper hostMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHostMockMvc;

    private Host host;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Host createEntity(EntityManager em) {
        Host host = new Host().hostName(DEFAULT_HOST_NAME).ipAddress(DEFAULT_IP_ADDRESS).asname(DEFAULT_ASNAME).org(DEFAULT_ORG);
        return host;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Host createUpdatedEntity(EntityManager em) {
        Host host = new Host().hostName(UPDATED_HOST_NAME).ipAddress(UPDATED_IP_ADDRESS).asname(UPDATED_ASNAME).org(UPDATED_ORG);
        return host;
    }

    @BeforeEach
    public void initTest() {
        host = createEntity(em);
    }

    @Test
    @Transactional
    void createHost() throws Exception {
        int databaseSizeBeforeCreate = hostRepository.findAll().size();
        // Create the Host
        HostDTO hostDTO = hostMapper.toDto(host);
        restHostMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hostDTO)))
            .andExpect(status().isCreated());

        // Validate the Host in the database
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeCreate + 1);
        Host testHost = hostList.get(hostList.size() - 1);
        assertThat(testHost.getHostName()).isEqualTo(DEFAULT_HOST_NAME);
        assertThat(testHost.getIpAddress()).isEqualTo(DEFAULT_IP_ADDRESS);
        assertThat(testHost.getAsname()).isEqualTo(DEFAULT_ASNAME);
        assertThat(testHost.getOrg()).isEqualTo(DEFAULT_ORG);
    }

    @Test
    @Transactional
    void createHostWithExistingId() throws Exception {
        // Create the Host with an existing ID
        host.setId(1L);
        HostDTO hostDTO = hostMapper.toDto(host);

        int databaseSizeBeforeCreate = hostRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHostMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hostDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Host in the database
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHosts() throws Exception {
        // Initialize the database
        hostRepository.saveAndFlush(host);

        // Get all the hostList
        restHostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(host.getId().intValue())))
            .andExpect(jsonPath("$.[*].hostName").value(hasItem(DEFAULT_HOST_NAME)))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].asname").value(hasItem(DEFAULT_ASNAME)))
            .andExpect(jsonPath("$.[*].org").value(hasItem(DEFAULT_ORG)));
    }

    @Test
    @Transactional
    void getHost() throws Exception {
        // Initialize the database
        hostRepository.saveAndFlush(host);

        // Get the host
        restHostMockMvc
            .perform(get(ENTITY_API_URL_ID, host.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(host.getId().intValue()))
            .andExpect(jsonPath("$.hostName").value(DEFAULT_HOST_NAME))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS))
            .andExpect(jsonPath("$.asname").value(DEFAULT_ASNAME))
            .andExpect(jsonPath("$.org").value(DEFAULT_ORG));
    }

    @Test
    @Transactional
    void getNonExistingHost() throws Exception {
        // Get the host
        restHostMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHost() throws Exception {
        // Initialize the database
        hostRepository.saveAndFlush(host);

        int databaseSizeBeforeUpdate = hostRepository.findAll().size();

        // Update the host
        Host updatedHost = hostRepository.findById(host.getId()).get();
        // Disconnect from session so that the updates on updatedHost are not directly saved in db
        em.detach(updatedHost);
        updatedHost.hostName(UPDATED_HOST_NAME).ipAddress(UPDATED_IP_ADDRESS).asname(UPDATED_ASNAME).org(UPDATED_ORG);
        HostDTO hostDTO = hostMapper.toDto(updatedHost);

        restHostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hostDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hostDTO))
            )
            .andExpect(status().isOk());

        // Validate the Host in the database
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeUpdate);
        Host testHost = hostList.get(hostList.size() - 1);
        assertThat(testHost.getHostName()).isEqualTo(UPDATED_HOST_NAME);
        assertThat(testHost.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testHost.getAsname()).isEqualTo(UPDATED_ASNAME);
        assertThat(testHost.getOrg()).isEqualTo(UPDATED_ORG);
    }

    @Test
    @Transactional
    void putNonExistingHost() throws Exception {
        int databaseSizeBeforeUpdate = hostRepository.findAll().size();
        host.setId(count.incrementAndGet());

        // Create the Host
        HostDTO hostDTO = hostMapper.toDto(host);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hostDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Host in the database
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHost() throws Exception {
        int databaseSizeBeforeUpdate = hostRepository.findAll().size();
        host.setId(count.incrementAndGet());

        // Create the Host
        HostDTO hostDTO = hostMapper.toDto(host);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Host in the database
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHost() throws Exception {
        int databaseSizeBeforeUpdate = hostRepository.findAll().size();
        host.setId(count.incrementAndGet());

        // Create the Host
        HostDTO hostDTO = hostMapper.toDto(host);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHostMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hostDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Host in the database
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHostWithPatch() throws Exception {
        // Initialize the database
        hostRepository.saveAndFlush(host);

        int databaseSizeBeforeUpdate = hostRepository.findAll().size();

        // Update the host using partial update
        Host partialUpdatedHost = new Host();
        partialUpdatedHost.setId(host.getId());

        partialUpdatedHost.hostName(UPDATED_HOST_NAME).org(UPDATED_ORG);

        restHostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHost.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHost))
            )
            .andExpect(status().isOk());

        // Validate the Host in the database
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeUpdate);
        Host testHost = hostList.get(hostList.size() - 1);
        assertThat(testHost.getHostName()).isEqualTo(UPDATED_HOST_NAME);
        assertThat(testHost.getIpAddress()).isEqualTo(DEFAULT_IP_ADDRESS);
        assertThat(testHost.getAsname()).isEqualTo(DEFAULT_ASNAME);
        assertThat(testHost.getOrg()).isEqualTo(UPDATED_ORG);
    }

    @Test
    @Transactional
    void fullUpdateHostWithPatch() throws Exception {
        // Initialize the database
        hostRepository.saveAndFlush(host);

        int databaseSizeBeforeUpdate = hostRepository.findAll().size();

        // Update the host using partial update
        Host partialUpdatedHost = new Host();
        partialUpdatedHost.setId(host.getId());

        partialUpdatedHost.hostName(UPDATED_HOST_NAME).ipAddress(UPDATED_IP_ADDRESS).asname(UPDATED_ASNAME).org(UPDATED_ORG);

        restHostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHost.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHost))
            )
            .andExpect(status().isOk());

        // Validate the Host in the database
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeUpdate);
        Host testHost = hostList.get(hostList.size() - 1);
        assertThat(testHost.getHostName()).isEqualTo(UPDATED_HOST_NAME);
        assertThat(testHost.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testHost.getAsname()).isEqualTo(UPDATED_ASNAME);
        assertThat(testHost.getOrg()).isEqualTo(UPDATED_ORG);
    }

    @Test
    @Transactional
    void patchNonExistingHost() throws Exception {
        int databaseSizeBeforeUpdate = hostRepository.findAll().size();
        host.setId(count.incrementAndGet());

        // Create the Host
        HostDTO hostDTO = hostMapper.toDto(host);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hostDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Host in the database
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHost() throws Exception {
        int databaseSizeBeforeUpdate = hostRepository.findAll().size();
        host.setId(count.incrementAndGet());

        // Create the Host
        HostDTO hostDTO = hostMapper.toDto(host);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Host in the database
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHost() throws Exception {
        int databaseSizeBeforeUpdate = hostRepository.findAll().size();
        host.setId(count.incrementAndGet());

        // Create the Host
        HostDTO hostDTO = hostMapper.toDto(host);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHostMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(hostDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Host in the database
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHost() throws Exception {
        // Initialize the database
        hostRepository.saveAndFlush(host);

        int databaseSizeBeforeDelete = hostRepository.findAll().size();

        // Delete the host
        restHostMockMvc
            .perform(delete(ENTITY_API_URL_ID, host.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
