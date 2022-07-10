package com.tattea.analyzer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tattea.analyzer.IntegrationTest;
import com.tattea.analyzer.domain.Port;
import com.tattea.analyzer.repository.PortRepository;
import com.tattea.analyzer.service.dto.PortDTO;
import com.tattea.analyzer.service.mapper.PortMapper;
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
 * Integration tests for the {@link PortResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PortResourceIT {

    private static final Long DEFAULT_PORT = 1L;
    private static final Long UPDATED_PORT = 2L;

    private static final String DEFAULT_IS_TCP = "AAAAAAAAAA";
    private static final String UPDATED_IS_TCP = "BBBBBBBBBB";

    private static final String DEFAULT_IS_UDP = "AAAAAAAAAA";
    private static final String UPDATED_IS_UDP = "BBBBBBBBBB";

    private static final String DEFAULT_IS_SCTP = "AAAAAAAAAA";
    private static final String UPDATED_IS_SCTP = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PortRepository portRepository;

    @Autowired
    private PortMapper portMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPortMockMvc;

    private Port port;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Port createEntity(EntityManager em) {
        Port port = new Port()
            .port(DEFAULT_PORT)
            .isTCP(DEFAULT_IS_TCP)
            .isUDP(DEFAULT_IS_UDP)
            .isSCTP(DEFAULT_IS_SCTP)
            .description(DEFAULT_DESCRIPTION)
            .name(DEFAULT_NAME);
        return port;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Port createUpdatedEntity(EntityManager em) {
        Port port = new Port()
            .port(UPDATED_PORT)
            .isTCP(UPDATED_IS_TCP)
            .isUDP(UPDATED_IS_UDP)
            .isSCTP(UPDATED_IS_SCTP)
            .description(UPDATED_DESCRIPTION)
            .name(UPDATED_NAME);
        return port;
    }

    @BeforeEach
    public void initTest() {
        port = createEntity(em);
    }

    @Test
    @Transactional
    void createPort() throws Exception {
        int databaseSizeBeforeCreate = portRepository.findAll().size();
        // Create the Port
        PortDTO portDTO = portMapper.toDto(port);
        restPortMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(portDTO)))
            .andExpect(status().isCreated());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeCreate + 1);
        Port testPort = portList.get(portList.size() - 1);
        assertThat(testPort.getPort()).isEqualTo(DEFAULT_PORT);
        assertThat(testPort.getIsTCP()).isEqualTo(DEFAULT_IS_TCP);
        assertThat(testPort.getIsUDP()).isEqualTo(DEFAULT_IS_UDP);
        assertThat(testPort.getIsSCTP()).isEqualTo(DEFAULT_IS_SCTP);
        assertThat(testPort.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPort.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createPortWithExistingId() throws Exception {
        // Create the Port with an existing ID
        port.setId(1L);
        PortDTO portDTO = portMapper.toDto(port);

        int databaseSizeBeforeCreate = portRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPortMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(portDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPorts() throws Exception {
        // Initialize the database
        portRepository.saveAndFlush(port);

        // Get all the portList
        restPortMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(port.getId().intValue())))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT.intValue())))
            .andExpect(jsonPath("$.[*].isTCP").value(hasItem(DEFAULT_IS_TCP)))
            .andExpect(jsonPath("$.[*].isUDP").value(hasItem(DEFAULT_IS_UDP)))
            .andExpect(jsonPath("$.[*].isSCTP").value(hasItem(DEFAULT_IS_SCTP)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getPort() throws Exception {
        // Initialize the database
        portRepository.saveAndFlush(port);

        // Get the port
        restPortMockMvc
            .perform(get(ENTITY_API_URL_ID, port.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(port.getId().intValue()))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT.intValue()))
            .andExpect(jsonPath("$.isTCP").value(DEFAULT_IS_TCP))
            .andExpect(jsonPath("$.isUDP").value(DEFAULT_IS_UDP))
            .andExpect(jsonPath("$.isSCTP").value(DEFAULT_IS_SCTP))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPort() throws Exception {
        // Get the port
        restPortMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPort() throws Exception {
        // Initialize the database
        portRepository.saveAndFlush(port);

        int databaseSizeBeforeUpdate = portRepository.findAll().size();

        // Update the port
        Port updatedPort = portRepository.findById(port.getId()).get();
        // Disconnect from session so that the updates on updatedPort are not directly saved in db
        em.detach(updatedPort);
        updatedPort
            .port(UPDATED_PORT)
            .isTCP(UPDATED_IS_TCP)
            .isUDP(UPDATED_IS_UDP)
            .isSCTP(UPDATED_IS_SCTP)
            .description(UPDATED_DESCRIPTION)
            .name(UPDATED_NAME);
        PortDTO portDTO = portMapper.toDto(updatedPort);

        restPortMockMvc
            .perform(
                put(ENTITY_API_URL_ID, portDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(portDTO))
            )
            .andExpect(status().isOk());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeUpdate);
        Port testPort = portList.get(portList.size() - 1);
        assertThat(testPort.getPort()).isEqualTo(UPDATED_PORT);
        assertThat(testPort.getIsTCP()).isEqualTo(UPDATED_IS_TCP);
        assertThat(testPort.getIsUDP()).isEqualTo(UPDATED_IS_UDP);
        assertThat(testPort.getIsSCTP()).isEqualTo(UPDATED_IS_SCTP);
        assertThat(testPort.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPort.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPort() throws Exception {
        int databaseSizeBeforeUpdate = portRepository.findAll().size();
        port.setId(count.incrementAndGet());

        // Create the Port
        PortDTO portDTO = portMapper.toDto(port);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPortMockMvc
            .perform(
                put(ENTITY_API_URL_ID, portDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(portDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPort() throws Exception {
        int databaseSizeBeforeUpdate = portRepository.findAll().size();
        port.setId(count.incrementAndGet());

        // Create the Port
        PortDTO portDTO = portMapper.toDto(port);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(portDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPort() throws Exception {
        int databaseSizeBeforeUpdate = portRepository.findAll().size();
        port.setId(count.incrementAndGet());

        // Create the Port
        PortDTO portDTO = portMapper.toDto(port);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(portDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePortWithPatch() throws Exception {
        // Initialize the database
        portRepository.saveAndFlush(port);

        int databaseSizeBeforeUpdate = portRepository.findAll().size();

        // Update the port using partial update
        Port partialUpdatedPort = new Port();
        partialUpdatedPort.setId(port.getId());

        partialUpdatedPort.port(UPDATED_PORT).isTCP(UPDATED_IS_TCP).isSCTP(UPDATED_IS_SCTP).name(UPDATED_NAME);

        restPortMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPort.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPort))
            )
            .andExpect(status().isOk());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeUpdate);
        Port testPort = portList.get(portList.size() - 1);
        assertThat(testPort.getPort()).isEqualTo(UPDATED_PORT);
        assertThat(testPort.getIsTCP()).isEqualTo(UPDATED_IS_TCP);
        assertThat(testPort.getIsUDP()).isEqualTo(DEFAULT_IS_UDP);
        assertThat(testPort.getIsSCTP()).isEqualTo(UPDATED_IS_SCTP);
        assertThat(testPort.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPort.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePortWithPatch() throws Exception {
        // Initialize the database
        portRepository.saveAndFlush(port);

        int databaseSizeBeforeUpdate = portRepository.findAll().size();

        // Update the port using partial update
        Port partialUpdatedPort = new Port();
        partialUpdatedPort.setId(port.getId());

        partialUpdatedPort
            .port(UPDATED_PORT)
            .isTCP(UPDATED_IS_TCP)
            .isUDP(UPDATED_IS_UDP)
            .isSCTP(UPDATED_IS_SCTP)
            .description(UPDATED_DESCRIPTION)
            .name(UPDATED_NAME);

        restPortMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPort.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPort))
            )
            .andExpect(status().isOk());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeUpdate);
        Port testPort = portList.get(portList.size() - 1);
        assertThat(testPort.getPort()).isEqualTo(UPDATED_PORT);
        assertThat(testPort.getIsTCP()).isEqualTo(UPDATED_IS_TCP);
        assertThat(testPort.getIsUDP()).isEqualTo(UPDATED_IS_UDP);
        assertThat(testPort.getIsSCTP()).isEqualTo(UPDATED_IS_SCTP);
        assertThat(testPort.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPort.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPort() throws Exception {
        int databaseSizeBeforeUpdate = portRepository.findAll().size();
        port.setId(count.incrementAndGet());

        // Create the Port
        PortDTO portDTO = portMapper.toDto(port);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPortMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, portDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(portDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPort() throws Exception {
        int databaseSizeBeforeUpdate = portRepository.findAll().size();
        port.setId(count.incrementAndGet());

        // Create the Port
        PortDTO portDTO = portMapper.toDto(port);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(portDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPort() throws Exception {
        int databaseSizeBeforeUpdate = portRepository.findAll().size();
        port.setId(count.incrementAndGet());

        // Create the Port
        PortDTO portDTO = portMapper.toDto(port);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(portDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePort() throws Exception {
        // Initialize the database
        portRepository.saveAndFlush(port);

        int databaseSizeBeforeDelete = portRepository.findAll().size();

        // Delete the port
        restPortMockMvc
            .perform(delete(ENTITY_API_URL_ID, port.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
