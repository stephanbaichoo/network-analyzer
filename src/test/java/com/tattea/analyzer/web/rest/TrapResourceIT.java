package com.tattea.analyzer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tattea.analyzer.IntegrationTest;
import com.tattea.analyzer.domain.Trap;
import com.tattea.analyzer.repository.TrapRepository;
import com.tattea.analyzer.service.dto.TrapDTO;
import com.tattea.analyzer.service.mapper.TrapMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link TrapResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrapResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TIME = "AAAAAAAAAA";
    private static final String UPDATED_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_TRAP = "AAAAAAAAAA";
    private static final String UPDATED_TRAP = "BBBBBBBBBB";

    private static final String DEFAULT_VALUES = "AAAAAAAAAA";
    private static final String UPDATED_VALUES = "BBBBBBBBBB";

    private static final String DEFAULT_TRIGGER = "AAAAAAAAAA";
    private static final String UPDATED_TRIGGER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/traps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrapRepository trapRepository;

    @Autowired
    private TrapMapper trapMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrapMockMvc;

    private Trap trap;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trap createEntity(EntityManager em) {
        Trap trap = new Trap().date(DEFAULT_DATE).time(DEFAULT_TIME).trap(DEFAULT_TRAP).values(DEFAULT_VALUES).trigger(DEFAULT_TRIGGER);
        return trap;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trap createUpdatedEntity(EntityManager em) {
        Trap trap = new Trap().date(UPDATED_DATE).time(UPDATED_TIME).trap(UPDATED_TRAP).values(UPDATED_VALUES).trigger(UPDATED_TRIGGER);
        return trap;
    }

    @BeforeEach
    public void initTest() {
        trap = createEntity(em);
    }

    @Test
    @Transactional
    void createTrap() throws Exception {
        int databaseSizeBeforeCreate = trapRepository.findAll().size();
        // Create the Trap
        TrapDTO trapDTO = trapMapper.toDto(trap);
        restTrapMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trapDTO)))
            .andExpect(status().isCreated());

        // Validate the Trap in the database
        List<Trap> trapList = trapRepository.findAll();
        assertThat(trapList).hasSize(databaseSizeBeforeCreate + 1);
        Trap testTrap = trapList.get(trapList.size() - 1);
        assertThat(testTrap.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTrap.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testTrap.getTrap()).isEqualTo(DEFAULT_TRAP);
        assertThat(testTrap.getValues()).isEqualTo(DEFAULT_VALUES);
        assertThat(testTrap.getTrigger()).isEqualTo(DEFAULT_TRIGGER);
    }

    @Test
    @Transactional
    void createTrapWithExistingId() throws Exception {
        // Create the Trap with an existing ID
        trap.setId(1L);
        TrapDTO trapDTO = trapMapper.toDto(trap);

        int databaseSizeBeforeCreate = trapRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrapMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trapDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Trap in the database
        List<Trap> trapList = trapRepository.findAll();
        assertThat(trapList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTraps() throws Exception {
        // Initialize the database
        trapRepository.saveAndFlush(trap);

        // Get all the trapList
        restTrapMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trap.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME)))
            .andExpect(jsonPath("$.[*].trap").value(hasItem(DEFAULT_TRAP)))
            .andExpect(jsonPath("$.[*].values").value(hasItem(DEFAULT_VALUES)))
            .andExpect(jsonPath("$.[*].trigger").value(hasItem(DEFAULT_TRIGGER)));
    }

    @Test
    @Transactional
    void getTrap() throws Exception {
        // Initialize the database
        trapRepository.saveAndFlush(trap);

        // Get the trap
        restTrapMockMvc
            .perform(get(ENTITY_API_URL_ID, trap.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trap.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME))
            .andExpect(jsonPath("$.trap").value(DEFAULT_TRAP))
            .andExpect(jsonPath("$.values").value(DEFAULT_VALUES))
            .andExpect(jsonPath("$.trigger").value(DEFAULT_TRIGGER));
    }

    @Test
    @Transactional
    void getNonExistingTrap() throws Exception {
        // Get the trap
        restTrapMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTrap() throws Exception {
        // Initialize the database
        trapRepository.saveAndFlush(trap);

        int databaseSizeBeforeUpdate = trapRepository.findAll().size();

        // Update the trap
        Trap updatedTrap = trapRepository.findById(trap.getId()).get();
        // Disconnect from session so that the updates on updatedTrap are not directly saved in db
        em.detach(updatedTrap);
        updatedTrap.date(UPDATED_DATE).time(UPDATED_TIME).trap(UPDATED_TRAP).values(UPDATED_VALUES).trigger(UPDATED_TRIGGER);
        TrapDTO trapDTO = trapMapper.toDto(updatedTrap);

        restTrapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trapDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trapDTO))
            )
            .andExpect(status().isOk());

        // Validate the Trap in the database
        List<Trap> trapList = trapRepository.findAll();
        assertThat(trapList).hasSize(databaseSizeBeforeUpdate);
        Trap testTrap = trapList.get(trapList.size() - 1);
        assertThat(testTrap.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTrap.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testTrap.getTrap()).isEqualTo(UPDATED_TRAP);
        assertThat(testTrap.getValues()).isEqualTo(UPDATED_VALUES);
        assertThat(testTrap.getTrigger()).isEqualTo(UPDATED_TRIGGER);
    }

    @Test
    @Transactional
    void putNonExistingTrap() throws Exception {
        int databaseSizeBeforeUpdate = trapRepository.findAll().size();
        trap.setId(count.incrementAndGet());

        // Create the Trap
        TrapDTO trapDTO = trapMapper.toDto(trap);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trapDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trapDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trap in the database
        List<Trap> trapList = trapRepository.findAll();
        assertThat(trapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrap() throws Exception {
        int databaseSizeBeforeUpdate = trapRepository.findAll().size();
        trap.setId(count.incrementAndGet());

        // Create the Trap
        TrapDTO trapDTO = trapMapper.toDto(trap);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trapDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trap in the database
        List<Trap> trapList = trapRepository.findAll();
        assertThat(trapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrap() throws Exception {
        int databaseSizeBeforeUpdate = trapRepository.findAll().size();
        trap.setId(count.incrementAndGet());

        // Create the Trap
        TrapDTO trapDTO = trapMapper.toDto(trap);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrapMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trapDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trap in the database
        List<Trap> trapList = trapRepository.findAll();
        assertThat(trapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrapWithPatch() throws Exception {
        // Initialize the database
        trapRepository.saveAndFlush(trap);

        int databaseSizeBeforeUpdate = trapRepository.findAll().size();

        // Update the trap using partial update
        Trap partialUpdatedTrap = new Trap();
        partialUpdatedTrap.setId(trap.getId());

        partialUpdatedTrap.time(UPDATED_TIME).trap(UPDATED_TRAP);

        restTrapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrap.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrap))
            )
            .andExpect(status().isOk());

        // Validate the Trap in the database
        List<Trap> trapList = trapRepository.findAll();
        assertThat(trapList).hasSize(databaseSizeBeforeUpdate);
        Trap testTrap = trapList.get(trapList.size() - 1);
        assertThat(testTrap.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTrap.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testTrap.getTrap()).isEqualTo(UPDATED_TRAP);
        assertThat(testTrap.getValues()).isEqualTo(DEFAULT_VALUES);
        assertThat(testTrap.getTrigger()).isEqualTo(DEFAULT_TRIGGER);
    }

    @Test
    @Transactional
    void fullUpdateTrapWithPatch() throws Exception {
        // Initialize the database
        trapRepository.saveAndFlush(trap);

        int databaseSizeBeforeUpdate = trapRepository.findAll().size();

        // Update the trap using partial update
        Trap partialUpdatedTrap = new Trap();
        partialUpdatedTrap.setId(trap.getId());

        partialUpdatedTrap.date(UPDATED_DATE).time(UPDATED_TIME).trap(UPDATED_TRAP).values(UPDATED_VALUES).trigger(UPDATED_TRIGGER);

        restTrapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrap.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrap))
            )
            .andExpect(status().isOk());

        // Validate the Trap in the database
        List<Trap> trapList = trapRepository.findAll();
        assertThat(trapList).hasSize(databaseSizeBeforeUpdate);
        Trap testTrap = trapList.get(trapList.size() - 1);
        assertThat(testTrap.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTrap.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testTrap.getTrap()).isEqualTo(UPDATED_TRAP);
        assertThat(testTrap.getValues()).isEqualTo(UPDATED_VALUES);
        assertThat(testTrap.getTrigger()).isEqualTo(UPDATED_TRIGGER);
    }

    @Test
    @Transactional
    void patchNonExistingTrap() throws Exception {
        int databaseSizeBeforeUpdate = trapRepository.findAll().size();
        trap.setId(count.incrementAndGet());

        // Create the Trap
        TrapDTO trapDTO = trapMapper.toDto(trap);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trapDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trapDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trap in the database
        List<Trap> trapList = trapRepository.findAll();
        assertThat(trapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrap() throws Exception {
        int databaseSizeBeforeUpdate = trapRepository.findAll().size();
        trap.setId(count.incrementAndGet());

        // Create the Trap
        TrapDTO trapDTO = trapMapper.toDto(trap);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trapDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trap in the database
        List<Trap> trapList = trapRepository.findAll();
        assertThat(trapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrap() throws Exception {
        int databaseSizeBeforeUpdate = trapRepository.findAll().size();
        trap.setId(count.incrementAndGet());

        // Create the Trap
        TrapDTO trapDTO = trapMapper.toDto(trap);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrapMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(trapDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trap in the database
        List<Trap> trapList = trapRepository.findAll();
        assertThat(trapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrap() throws Exception {
        // Initialize the database
        trapRepository.saveAndFlush(trap);

        int databaseSizeBeforeDelete = trapRepository.findAll().size();

        // Delete the trap
        restTrapMockMvc
            .perform(delete(ENTITY_API_URL_ID, trap.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Trap> trapList = trapRepository.findAll();
        assertThat(trapList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
