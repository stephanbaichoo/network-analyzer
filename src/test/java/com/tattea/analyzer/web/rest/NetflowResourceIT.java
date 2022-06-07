package com.tattea.analyzer.web.rest;

import static com.tattea.analyzer.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tattea.analyzer.IntegrationTest;
import com.tattea.analyzer.domain.Netflow;
import com.tattea.analyzer.repository.NetflowRepository;
import com.tattea.analyzer.service.criteria.NetflowCriteria;
import com.tattea.analyzer.service.dto.NetflowDTO;
import com.tattea.analyzer.service.mapper.NetflowMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link NetflowResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NetflowResourceIT {

    private static final LocalDate DEFAULT_DATE_FIRST_SEEN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIRST_SEEN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_FIRST_SEEN = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TIME_FIRST_SEEN = "AAAAAAAAAA";
    private static final String UPDATED_TIME_FIRST_SEEN = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DURATION = new BigDecimal(1);
    private static final BigDecimal UPDATED_DURATION = new BigDecimal(2);
    private static final BigDecimal SMALLER_DURATION = new BigDecimal(1 - 1);

    private static final String DEFAULT_PROTOCOL = "AAAAAAAAAA";
    private static final String UPDATED_PROTOCOL = "BBBBBBBBBB";

    private static final String DEFAULT_SRC_IP = "AAAAAAAAAA";
    private static final String UPDATED_SRC_IP = "BBBBBBBBBB";

    private static final String DEFAULT_DST_IP = "AAAAAAAAAA";
    private static final String UPDATED_DST_IP = "BBBBBBBBBB";

    private static final String DEFAULT_FLAGS = "AAAAAAAAAA";
    private static final String UPDATED_FLAGS = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOS = 1;
    private static final Integer UPDATED_TOS = 2;
    private static final Integer SMALLER_TOS = 1 - 1;

    private static final Integer DEFAULT_PACKET_NO = 1;
    private static final Integer UPDATED_PACKET_NO = 2;
    private static final Integer SMALLER_PACKET_NO = 1 - 1;

    private static final String DEFAULT_BYTES = "AAAAAAAAAA";
    private static final String UPDATED_BYTES = "BBBBBBBBBB";

    private static final String DEFAULT_PPS = "AAAAAAAAAA";
    private static final String UPDATED_PPS = "BBBBBBBBBB";

    private static final String DEFAULT_BPS = "AAAAAAAAAA";
    private static final String UPDATED_BPS = "BBBBBBBBBB";

    private static final String DEFAULT_BPP = "AAAAAAAAAA";
    private static final String UPDATED_BPP = "BBBBBBBBBB";

    private static final String DEFAULT_FLOWS = "AAAAAAAAAA";
    private static final String UPDATED_FLOWS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/netflows";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NetflowRepository netflowRepository;

    @Autowired
    private NetflowMapper netflowMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNetflowMockMvc;

    private Netflow netflow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Netflow createEntity(EntityManager em) {
        Netflow netflow = new Netflow()
            .dateFirstSeen(DEFAULT_DATE_FIRST_SEEN)
            .timeFirstSeen(DEFAULT_TIME_FIRST_SEEN)
            .duration(DEFAULT_DURATION)
            .protocol(DEFAULT_PROTOCOL)
            .srcIp(DEFAULT_SRC_IP)
            .dstIp(DEFAULT_DST_IP)
            .flags(DEFAULT_FLAGS)
            .tos(DEFAULT_TOS)
            .packetNo(DEFAULT_PACKET_NO)
            .bytes(DEFAULT_BYTES)
            .pps(DEFAULT_PPS)
            .bps(DEFAULT_BPS)
            .bpp(DEFAULT_BPP)
            .flows(DEFAULT_FLOWS);
        return netflow;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Netflow createUpdatedEntity(EntityManager em) {
        Netflow netflow = new Netflow()
            .dateFirstSeen(UPDATED_DATE_FIRST_SEEN)
            .timeFirstSeen(UPDATED_TIME_FIRST_SEEN)
            .duration(UPDATED_DURATION)
            .protocol(UPDATED_PROTOCOL)
            .srcIp(UPDATED_SRC_IP)
            .dstIp(UPDATED_DST_IP)
            .flags(UPDATED_FLAGS)
            .tos(UPDATED_TOS)
            .packetNo(UPDATED_PACKET_NO)
            .bytes(UPDATED_BYTES)
            .pps(UPDATED_PPS)
            .bps(UPDATED_BPS)
            .bpp(UPDATED_BPP)
            .flows(UPDATED_FLOWS);
        return netflow;
    }

    @BeforeEach
    public void initTest() {
        netflow = createEntity(em);
    }

    @Test
    @Transactional
    void createNetflow() throws Exception {
        int databaseSizeBeforeCreate = netflowRepository.findAll().size();
        // Create the Netflow
        NetflowDTO netflowDTO = netflowMapper.toDto(netflow);
        restNetflowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(netflowDTO)))
            .andExpect(status().isCreated());

        // Validate the Netflow in the database
        List<Netflow> netflowList = netflowRepository.findAll();
        assertThat(netflowList).hasSize(databaseSizeBeforeCreate + 1);
        Netflow testNetflow = netflowList.get(netflowList.size() - 1);
        assertThat(testNetflow.getDateFirstSeen()).isEqualTo(DEFAULT_DATE_FIRST_SEEN);
        assertThat(testNetflow.getTimeFirstSeen()).isEqualTo(DEFAULT_TIME_FIRST_SEEN);
        assertThat(testNetflow.getDuration()).isEqualByComparingTo(DEFAULT_DURATION);
        assertThat(testNetflow.getProtocol()).isEqualTo(DEFAULT_PROTOCOL);
        assertThat(testNetflow.getSrcIp()).isEqualTo(DEFAULT_SRC_IP);
        assertThat(testNetflow.getDstIp()).isEqualTo(DEFAULT_DST_IP);
        assertThat(testNetflow.getFlags()).isEqualTo(DEFAULT_FLAGS);
        assertThat(testNetflow.getTos()).isEqualTo(DEFAULT_TOS);
        assertThat(testNetflow.getPacketNo()).isEqualTo(DEFAULT_PACKET_NO);
        assertThat(testNetflow.getBytes()).isEqualTo(DEFAULT_BYTES);
        assertThat(testNetflow.getPps()).isEqualTo(DEFAULT_PPS);
        assertThat(testNetflow.getBps()).isEqualTo(DEFAULT_BPS);
        assertThat(testNetflow.getBpp()).isEqualTo(DEFAULT_BPP);
        assertThat(testNetflow.getFlows()).isEqualTo(DEFAULT_FLOWS);
    }

    @Test
    @Transactional
    void createNetflowWithExistingId() throws Exception {
        // Create the Netflow with an existing ID
        netflow.setId(1L);
        NetflowDTO netflowDTO = netflowMapper.toDto(netflow);

        int databaseSizeBeforeCreate = netflowRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNetflowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(netflowDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Netflow in the database
        List<Netflow> netflowList = netflowRepository.findAll();
        assertThat(netflowList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNetflows() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList
        restNetflowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(netflow.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateFirstSeen").value(hasItem(DEFAULT_DATE_FIRST_SEEN.toString())))
            .andExpect(jsonPath("$.[*].timeFirstSeen").value(hasItem(DEFAULT_TIME_FIRST_SEEN)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(sameNumber(DEFAULT_DURATION))))
            .andExpect(jsonPath("$.[*].protocol").value(hasItem(DEFAULT_PROTOCOL)))
            .andExpect(jsonPath("$.[*].srcIp").value(hasItem(DEFAULT_SRC_IP)))
            .andExpect(jsonPath("$.[*].dstIp").value(hasItem(DEFAULT_DST_IP)))
            .andExpect(jsonPath("$.[*].flags").value(hasItem(DEFAULT_FLAGS)))
            .andExpect(jsonPath("$.[*].tos").value(hasItem(DEFAULT_TOS)))
            .andExpect(jsonPath("$.[*].packetNo").value(hasItem(DEFAULT_PACKET_NO)))
            .andExpect(jsonPath("$.[*].bytes").value(hasItem(DEFAULT_BYTES)))
            .andExpect(jsonPath("$.[*].pps").value(hasItem(DEFAULT_PPS)))
            .andExpect(jsonPath("$.[*].bps").value(hasItem(DEFAULT_BPS)))
            .andExpect(jsonPath("$.[*].bpp").value(hasItem(DEFAULT_BPP)))
            .andExpect(jsonPath("$.[*].flows").value(hasItem(DEFAULT_FLOWS)));
    }

    @Test
    @Transactional
    void getNetflow() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get the netflow
        restNetflowMockMvc
            .perform(get(ENTITY_API_URL_ID, netflow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(netflow.getId().intValue()))
            .andExpect(jsonPath("$.dateFirstSeen").value(DEFAULT_DATE_FIRST_SEEN.toString()))
            .andExpect(jsonPath("$.timeFirstSeen").value(DEFAULT_TIME_FIRST_SEEN))
            .andExpect(jsonPath("$.duration").value(sameNumber(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.protocol").value(DEFAULT_PROTOCOL))
            .andExpect(jsonPath("$.srcIp").value(DEFAULT_SRC_IP))
            .andExpect(jsonPath("$.dstIp").value(DEFAULT_DST_IP))
            .andExpect(jsonPath("$.flags").value(DEFAULT_FLAGS))
            .andExpect(jsonPath("$.tos").value(DEFAULT_TOS))
            .andExpect(jsonPath("$.packetNo").value(DEFAULT_PACKET_NO))
            .andExpect(jsonPath("$.bytes").value(DEFAULT_BYTES))
            .andExpect(jsonPath("$.pps").value(DEFAULT_PPS))
            .andExpect(jsonPath("$.bps").value(DEFAULT_BPS))
            .andExpect(jsonPath("$.bpp").value(DEFAULT_BPP))
            .andExpect(jsonPath("$.flows").value(DEFAULT_FLOWS));
    }

    @Test
    @Transactional
    void getNetflowsByIdFiltering() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        Long id = netflow.getId();

        defaultNetflowShouldBeFound("id.equals=" + id);
        defaultNetflowShouldNotBeFound("id.notEquals=" + id);

        defaultNetflowShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNetflowShouldNotBeFound("id.greaterThan=" + id);

        defaultNetflowShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNetflowShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNetflowsByDateFirstSeenIsEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where dateFirstSeen equals to DEFAULT_DATE_FIRST_SEEN
        defaultNetflowShouldBeFound("dateFirstSeen.equals=" + DEFAULT_DATE_FIRST_SEEN);

        // Get all the netflowList where dateFirstSeen equals to UPDATED_DATE_FIRST_SEEN
        defaultNetflowShouldNotBeFound("dateFirstSeen.equals=" + UPDATED_DATE_FIRST_SEEN);
    }

    @Test
    @Transactional
    void getAllNetflowsByDateFirstSeenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where dateFirstSeen not equals to DEFAULT_DATE_FIRST_SEEN
        defaultNetflowShouldNotBeFound("dateFirstSeen.notEquals=" + DEFAULT_DATE_FIRST_SEEN);

        // Get all the netflowList where dateFirstSeen not equals to UPDATED_DATE_FIRST_SEEN
        defaultNetflowShouldBeFound("dateFirstSeen.notEquals=" + UPDATED_DATE_FIRST_SEEN);
    }

    @Test
    @Transactional
    void getAllNetflowsByDateFirstSeenIsInShouldWork() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where dateFirstSeen in DEFAULT_DATE_FIRST_SEEN or UPDATED_DATE_FIRST_SEEN
        defaultNetflowShouldBeFound("dateFirstSeen.in=" + DEFAULT_DATE_FIRST_SEEN + "," + UPDATED_DATE_FIRST_SEEN);

        // Get all the netflowList where dateFirstSeen equals to UPDATED_DATE_FIRST_SEEN
        defaultNetflowShouldNotBeFound("dateFirstSeen.in=" + UPDATED_DATE_FIRST_SEEN);
    }

    @Test
    @Transactional
    void getAllNetflowsByDateFirstSeenIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where dateFirstSeen is not null
        defaultNetflowShouldBeFound("dateFirstSeen.specified=true");

        // Get all the netflowList where dateFirstSeen is null
        defaultNetflowShouldNotBeFound("dateFirstSeen.specified=false");
    }

    @Test
    @Transactional
    void getAllNetflowsByDateFirstSeenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where dateFirstSeen is greater than or equal to DEFAULT_DATE_FIRST_SEEN
        defaultNetflowShouldBeFound("dateFirstSeen.greaterThanOrEqual=" + DEFAULT_DATE_FIRST_SEEN);

        // Get all the netflowList where dateFirstSeen is greater than or equal to UPDATED_DATE_FIRST_SEEN
        defaultNetflowShouldNotBeFound("dateFirstSeen.greaterThanOrEqual=" + UPDATED_DATE_FIRST_SEEN);
    }

    @Test
    @Transactional
    void getAllNetflowsByDateFirstSeenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where dateFirstSeen is less than or equal to DEFAULT_DATE_FIRST_SEEN
        defaultNetflowShouldBeFound("dateFirstSeen.lessThanOrEqual=" + DEFAULT_DATE_FIRST_SEEN);

        // Get all the netflowList where dateFirstSeen is less than or equal to SMALLER_DATE_FIRST_SEEN
        defaultNetflowShouldNotBeFound("dateFirstSeen.lessThanOrEqual=" + SMALLER_DATE_FIRST_SEEN);
    }

    @Test
    @Transactional
    void getAllNetflowsByDateFirstSeenIsLessThanSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where dateFirstSeen is less than DEFAULT_DATE_FIRST_SEEN
        defaultNetflowShouldNotBeFound("dateFirstSeen.lessThan=" + DEFAULT_DATE_FIRST_SEEN);

        // Get all the netflowList where dateFirstSeen is less than UPDATED_DATE_FIRST_SEEN
        defaultNetflowShouldBeFound("dateFirstSeen.lessThan=" + UPDATED_DATE_FIRST_SEEN);
    }

    @Test
    @Transactional
    void getAllNetflowsByDateFirstSeenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where dateFirstSeen is greater than DEFAULT_DATE_FIRST_SEEN
        defaultNetflowShouldNotBeFound("dateFirstSeen.greaterThan=" + DEFAULT_DATE_FIRST_SEEN);

        // Get all the netflowList where dateFirstSeen is greater than SMALLER_DATE_FIRST_SEEN
        defaultNetflowShouldBeFound("dateFirstSeen.greaterThan=" + SMALLER_DATE_FIRST_SEEN);
    }

    @Test
    @Transactional
    void getAllNetflowsByTimeFirstSeenIsEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where timeFirstSeen equals to DEFAULT_TIME_FIRST_SEEN
        defaultNetflowShouldBeFound("timeFirstSeen.equals=" + DEFAULT_TIME_FIRST_SEEN);

        // Get all the netflowList where timeFirstSeen equals to UPDATED_TIME_FIRST_SEEN
        defaultNetflowShouldNotBeFound("timeFirstSeen.equals=" + UPDATED_TIME_FIRST_SEEN);
    }

    @Test
    @Transactional
    void getAllNetflowsByTimeFirstSeenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where timeFirstSeen not equals to DEFAULT_TIME_FIRST_SEEN
        defaultNetflowShouldNotBeFound("timeFirstSeen.notEquals=" + DEFAULT_TIME_FIRST_SEEN);

        // Get all the netflowList where timeFirstSeen not equals to UPDATED_TIME_FIRST_SEEN
        defaultNetflowShouldBeFound("timeFirstSeen.notEquals=" + UPDATED_TIME_FIRST_SEEN);
    }

    @Test
    @Transactional
    void getAllNetflowsByTimeFirstSeenIsInShouldWork() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where timeFirstSeen in DEFAULT_TIME_FIRST_SEEN or UPDATED_TIME_FIRST_SEEN
        defaultNetflowShouldBeFound("timeFirstSeen.in=" + DEFAULT_TIME_FIRST_SEEN + "," + UPDATED_TIME_FIRST_SEEN);

        // Get all the netflowList where timeFirstSeen equals to UPDATED_TIME_FIRST_SEEN
        defaultNetflowShouldNotBeFound("timeFirstSeen.in=" + UPDATED_TIME_FIRST_SEEN);
    }

    @Test
    @Transactional
    void getAllNetflowsByTimeFirstSeenIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where timeFirstSeen is not null
        defaultNetflowShouldBeFound("timeFirstSeen.specified=true");

        // Get all the netflowList where timeFirstSeen is null
        defaultNetflowShouldNotBeFound("timeFirstSeen.specified=false");
    }

    @Test
    @Transactional
    void getAllNetflowsByTimeFirstSeenContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where timeFirstSeen contains DEFAULT_TIME_FIRST_SEEN
        defaultNetflowShouldBeFound("timeFirstSeen.contains=" + DEFAULT_TIME_FIRST_SEEN);

        // Get all the netflowList where timeFirstSeen contains UPDATED_TIME_FIRST_SEEN
        defaultNetflowShouldNotBeFound("timeFirstSeen.contains=" + UPDATED_TIME_FIRST_SEEN);
    }

    @Test
    @Transactional
    void getAllNetflowsByTimeFirstSeenNotContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where timeFirstSeen does not contain DEFAULT_TIME_FIRST_SEEN
        defaultNetflowShouldNotBeFound("timeFirstSeen.doesNotContain=" + DEFAULT_TIME_FIRST_SEEN);

        // Get all the netflowList where timeFirstSeen does not contain UPDATED_TIME_FIRST_SEEN
        defaultNetflowShouldBeFound("timeFirstSeen.doesNotContain=" + UPDATED_TIME_FIRST_SEEN);
    }

    @Test
    @Transactional
    void getAllNetflowsByDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where duration equals to DEFAULT_DURATION
        defaultNetflowShouldBeFound("duration.equals=" + DEFAULT_DURATION);

        // Get all the netflowList where duration equals to UPDATED_DURATION
        defaultNetflowShouldNotBeFound("duration.equals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllNetflowsByDurationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where duration not equals to DEFAULT_DURATION
        defaultNetflowShouldNotBeFound("duration.notEquals=" + DEFAULT_DURATION);

        // Get all the netflowList where duration not equals to UPDATED_DURATION
        defaultNetflowShouldBeFound("duration.notEquals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllNetflowsByDurationIsInShouldWork() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where duration in DEFAULT_DURATION or UPDATED_DURATION
        defaultNetflowShouldBeFound("duration.in=" + DEFAULT_DURATION + "," + UPDATED_DURATION);

        // Get all the netflowList where duration equals to UPDATED_DURATION
        defaultNetflowShouldNotBeFound("duration.in=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllNetflowsByDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where duration is not null
        defaultNetflowShouldBeFound("duration.specified=true");

        // Get all the netflowList where duration is null
        defaultNetflowShouldNotBeFound("duration.specified=false");
    }

    @Test
    @Transactional
    void getAllNetflowsByDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where duration is greater than or equal to DEFAULT_DURATION
        defaultNetflowShouldBeFound("duration.greaterThanOrEqual=" + DEFAULT_DURATION);

        // Get all the netflowList where duration is greater than or equal to UPDATED_DURATION
        defaultNetflowShouldNotBeFound("duration.greaterThanOrEqual=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllNetflowsByDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where duration is less than or equal to DEFAULT_DURATION
        defaultNetflowShouldBeFound("duration.lessThanOrEqual=" + DEFAULT_DURATION);

        // Get all the netflowList where duration is less than or equal to SMALLER_DURATION
        defaultNetflowShouldNotBeFound("duration.lessThanOrEqual=" + SMALLER_DURATION);
    }

    @Test
    @Transactional
    void getAllNetflowsByDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where duration is less than DEFAULT_DURATION
        defaultNetflowShouldNotBeFound("duration.lessThan=" + DEFAULT_DURATION);

        // Get all the netflowList where duration is less than UPDATED_DURATION
        defaultNetflowShouldBeFound("duration.lessThan=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllNetflowsByDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where duration is greater than DEFAULT_DURATION
        defaultNetflowShouldNotBeFound("duration.greaterThan=" + DEFAULT_DURATION);

        // Get all the netflowList where duration is greater than SMALLER_DURATION
        defaultNetflowShouldBeFound("duration.greaterThan=" + SMALLER_DURATION);
    }

    @Test
    @Transactional
    void getAllNetflowsByProtocolIsEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where protocol equals to DEFAULT_PROTOCOL
        defaultNetflowShouldBeFound("protocol.equals=" + DEFAULT_PROTOCOL);

        // Get all the netflowList where protocol equals to UPDATED_PROTOCOL
        defaultNetflowShouldNotBeFound("protocol.equals=" + UPDATED_PROTOCOL);
    }

    @Test
    @Transactional
    void getAllNetflowsByProtocolIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where protocol not equals to DEFAULT_PROTOCOL
        defaultNetflowShouldNotBeFound("protocol.notEquals=" + DEFAULT_PROTOCOL);

        // Get all the netflowList where protocol not equals to UPDATED_PROTOCOL
        defaultNetflowShouldBeFound("protocol.notEquals=" + UPDATED_PROTOCOL);
    }

    @Test
    @Transactional
    void getAllNetflowsByProtocolIsInShouldWork() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where protocol in DEFAULT_PROTOCOL or UPDATED_PROTOCOL
        defaultNetflowShouldBeFound("protocol.in=" + DEFAULT_PROTOCOL + "," + UPDATED_PROTOCOL);

        // Get all the netflowList where protocol equals to UPDATED_PROTOCOL
        defaultNetflowShouldNotBeFound("protocol.in=" + UPDATED_PROTOCOL);
    }

    @Test
    @Transactional
    void getAllNetflowsByProtocolIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where protocol is not null
        defaultNetflowShouldBeFound("protocol.specified=true");

        // Get all the netflowList where protocol is null
        defaultNetflowShouldNotBeFound("protocol.specified=false");
    }

    @Test
    @Transactional
    void getAllNetflowsByProtocolContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where protocol contains DEFAULT_PROTOCOL
        defaultNetflowShouldBeFound("protocol.contains=" + DEFAULT_PROTOCOL);

        // Get all the netflowList where protocol contains UPDATED_PROTOCOL
        defaultNetflowShouldNotBeFound("protocol.contains=" + UPDATED_PROTOCOL);
    }

    @Test
    @Transactional
    void getAllNetflowsByProtocolNotContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where protocol does not contain DEFAULT_PROTOCOL
        defaultNetflowShouldNotBeFound("protocol.doesNotContain=" + DEFAULT_PROTOCOL);

        // Get all the netflowList where protocol does not contain UPDATED_PROTOCOL
        defaultNetflowShouldBeFound("protocol.doesNotContain=" + UPDATED_PROTOCOL);
    }

    @Test
    @Transactional
    void getAllNetflowsBySrcIpIsEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where srcIp equals to DEFAULT_SRC_IP
        defaultNetflowShouldBeFound("srcIp.equals=" + DEFAULT_SRC_IP);

        // Get all the netflowList where srcIp equals to UPDATED_SRC_IP
        defaultNetflowShouldNotBeFound("srcIp.equals=" + UPDATED_SRC_IP);
    }

    @Test
    @Transactional
    void getAllNetflowsBySrcIpIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where srcIp not equals to DEFAULT_SRC_IP
        defaultNetflowShouldNotBeFound("srcIp.notEquals=" + DEFAULT_SRC_IP);

        // Get all the netflowList where srcIp not equals to UPDATED_SRC_IP
        defaultNetflowShouldBeFound("srcIp.notEquals=" + UPDATED_SRC_IP);
    }

    @Test
    @Transactional
    void getAllNetflowsBySrcIpIsInShouldWork() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where srcIp in DEFAULT_SRC_IP or UPDATED_SRC_IP
        defaultNetflowShouldBeFound("srcIp.in=" + DEFAULT_SRC_IP + "," + UPDATED_SRC_IP);

        // Get all the netflowList where srcIp equals to UPDATED_SRC_IP
        defaultNetflowShouldNotBeFound("srcIp.in=" + UPDATED_SRC_IP);
    }

    @Test
    @Transactional
    void getAllNetflowsBySrcIpIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where srcIp is not null
        defaultNetflowShouldBeFound("srcIp.specified=true");

        // Get all the netflowList where srcIp is null
        defaultNetflowShouldNotBeFound("srcIp.specified=false");
    }

    @Test
    @Transactional
    void getAllNetflowsBySrcIpContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where srcIp contains DEFAULT_SRC_IP
        defaultNetflowShouldBeFound("srcIp.contains=" + DEFAULT_SRC_IP);

        // Get all the netflowList where srcIp contains UPDATED_SRC_IP
        defaultNetflowShouldNotBeFound("srcIp.contains=" + UPDATED_SRC_IP);
    }

    @Test
    @Transactional
    void getAllNetflowsBySrcIpNotContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where srcIp does not contain DEFAULT_SRC_IP
        defaultNetflowShouldNotBeFound("srcIp.doesNotContain=" + DEFAULT_SRC_IP);

        // Get all the netflowList where srcIp does not contain UPDATED_SRC_IP
        defaultNetflowShouldBeFound("srcIp.doesNotContain=" + UPDATED_SRC_IP);
    }

    @Test
    @Transactional
    void getAllNetflowsByDstIpIsEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where dstIp equals to DEFAULT_DST_IP
        defaultNetflowShouldBeFound("dstIp.equals=" + DEFAULT_DST_IP);

        // Get all the netflowList where dstIp equals to UPDATED_DST_IP
        defaultNetflowShouldNotBeFound("dstIp.equals=" + UPDATED_DST_IP);
    }

    @Test
    @Transactional
    void getAllNetflowsByDstIpIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where dstIp not equals to DEFAULT_DST_IP
        defaultNetflowShouldNotBeFound("dstIp.notEquals=" + DEFAULT_DST_IP);

        // Get all the netflowList where dstIp not equals to UPDATED_DST_IP
        defaultNetflowShouldBeFound("dstIp.notEquals=" + UPDATED_DST_IP);
    }

    @Test
    @Transactional
    void getAllNetflowsByDstIpIsInShouldWork() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where dstIp in DEFAULT_DST_IP or UPDATED_DST_IP
        defaultNetflowShouldBeFound("dstIp.in=" + DEFAULT_DST_IP + "," + UPDATED_DST_IP);

        // Get all the netflowList where dstIp equals to UPDATED_DST_IP
        defaultNetflowShouldNotBeFound("dstIp.in=" + UPDATED_DST_IP);
    }

    @Test
    @Transactional
    void getAllNetflowsByDstIpIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where dstIp is not null
        defaultNetflowShouldBeFound("dstIp.specified=true");

        // Get all the netflowList where dstIp is null
        defaultNetflowShouldNotBeFound("dstIp.specified=false");
    }

    @Test
    @Transactional
    void getAllNetflowsByDstIpContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where dstIp contains DEFAULT_DST_IP
        defaultNetflowShouldBeFound("dstIp.contains=" + DEFAULT_DST_IP);

        // Get all the netflowList where dstIp contains UPDATED_DST_IP
        defaultNetflowShouldNotBeFound("dstIp.contains=" + UPDATED_DST_IP);
    }

    @Test
    @Transactional
    void getAllNetflowsByDstIpNotContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where dstIp does not contain DEFAULT_DST_IP
        defaultNetflowShouldNotBeFound("dstIp.doesNotContain=" + DEFAULT_DST_IP);

        // Get all the netflowList where dstIp does not contain UPDATED_DST_IP
        defaultNetflowShouldBeFound("dstIp.doesNotContain=" + UPDATED_DST_IP);
    }

    @Test
    @Transactional
    void getAllNetflowsByFlagsIsEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where flags equals to DEFAULT_FLAGS
        defaultNetflowShouldBeFound("flags.equals=" + DEFAULT_FLAGS);

        // Get all the netflowList where flags equals to UPDATED_FLAGS
        defaultNetflowShouldNotBeFound("flags.equals=" + UPDATED_FLAGS);
    }

    @Test
    @Transactional
    void getAllNetflowsByFlagsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where flags not equals to DEFAULT_FLAGS
        defaultNetflowShouldNotBeFound("flags.notEquals=" + DEFAULT_FLAGS);

        // Get all the netflowList where flags not equals to UPDATED_FLAGS
        defaultNetflowShouldBeFound("flags.notEquals=" + UPDATED_FLAGS);
    }

    @Test
    @Transactional
    void getAllNetflowsByFlagsIsInShouldWork() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where flags in DEFAULT_FLAGS or UPDATED_FLAGS
        defaultNetflowShouldBeFound("flags.in=" + DEFAULT_FLAGS + "," + UPDATED_FLAGS);

        // Get all the netflowList where flags equals to UPDATED_FLAGS
        defaultNetflowShouldNotBeFound("flags.in=" + UPDATED_FLAGS);
    }

    @Test
    @Transactional
    void getAllNetflowsByFlagsIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where flags is not null
        defaultNetflowShouldBeFound("flags.specified=true");

        // Get all the netflowList where flags is null
        defaultNetflowShouldNotBeFound("flags.specified=false");
    }

    @Test
    @Transactional
    void getAllNetflowsByFlagsContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where flags contains DEFAULT_FLAGS
        defaultNetflowShouldBeFound("flags.contains=" + DEFAULT_FLAGS);

        // Get all the netflowList where flags contains UPDATED_FLAGS
        defaultNetflowShouldNotBeFound("flags.contains=" + UPDATED_FLAGS);
    }

    @Test
    @Transactional
    void getAllNetflowsByFlagsNotContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where flags does not contain DEFAULT_FLAGS
        defaultNetflowShouldNotBeFound("flags.doesNotContain=" + DEFAULT_FLAGS);

        // Get all the netflowList where flags does not contain UPDATED_FLAGS
        defaultNetflowShouldBeFound("flags.doesNotContain=" + UPDATED_FLAGS);
    }

    @Test
    @Transactional
    void getAllNetflowsByTosIsEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where tos equals to DEFAULT_TOS
        defaultNetflowShouldBeFound("tos.equals=" + DEFAULT_TOS);

        // Get all the netflowList where tos equals to UPDATED_TOS
        defaultNetflowShouldNotBeFound("tos.equals=" + UPDATED_TOS);
    }

    @Test
    @Transactional
    void getAllNetflowsByTosIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where tos not equals to DEFAULT_TOS
        defaultNetflowShouldNotBeFound("tos.notEquals=" + DEFAULT_TOS);

        // Get all the netflowList where tos not equals to UPDATED_TOS
        defaultNetflowShouldBeFound("tos.notEquals=" + UPDATED_TOS);
    }

    @Test
    @Transactional
    void getAllNetflowsByTosIsInShouldWork() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where tos in DEFAULT_TOS or UPDATED_TOS
        defaultNetflowShouldBeFound("tos.in=" + DEFAULT_TOS + "," + UPDATED_TOS);

        // Get all the netflowList where tos equals to UPDATED_TOS
        defaultNetflowShouldNotBeFound("tos.in=" + UPDATED_TOS);
    }

    @Test
    @Transactional
    void getAllNetflowsByTosIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where tos is not null
        defaultNetflowShouldBeFound("tos.specified=true");

        // Get all the netflowList where tos is null
        defaultNetflowShouldNotBeFound("tos.specified=false");
    }

    @Test
    @Transactional
    void getAllNetflowsByTosIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where tos is greater than or equal to DEFAULT_TOS
        defaultNetflowShouldBeFound("tos.greaterThanOrEqual=" + DEFAULT_TOS);

        // Get all the netflowList where tos is greater than or equal to UPDATED_TOS
        defaultNetflowShouldNotBeFound("tos.greaterThanOrEqual=" + UPDATED_TOS);
    }

    @Test
    @Transactional
    void getAllNetflowsByTosIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where tos is less than or equal to DEFAULT_TOS
        defaultNetflowShouldBeFound("tos.lessThanOrEqual=" + DEFAULT_TOS);

        // Get all the netflowList where tos is less than or equal to SMALLER_TOS
        defaultNetflowShouldNotBeFound("tos.lessThanOrEqual=" + SMALLER_TOS);
    }

    @Test
    @Transactional
    void getAllNetflowsByTosIsLessThanSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where tos is less than DEFAULT_TOS
        defaultNetflowShouldNotBeFound("tos.lessThan=" + DEFAULT_TOS);

        // Get all the netflowList where tos is less than UPDATED_TOS
        defaultNetflowShouldBeFound("tos.lessThan=" + UPDATED_TOS);
    }

    @Test
    @Transactional
    void getAllNetflowsByTosIsGreaterThanSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where tos is greater than DEFAULT_TOS
        defaultNetflowShouldNotBeFound("tos.greaterThan=" + DEFAULT_TOS);

        // Get all the netflowList where tos is greater than SMALLER_TOS
        defaultNetflowShouldBeFound("tos.greaterThan=" + SMALLER_TOS);
    }

    @Test
    @Transactional
    void getAllNetflowsByPacketNoIsEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where packetNo equals to DEFAULT_PACKET_NO
        defaultNetflowShouldBeFound("packetNo.equals=" + DEFAULT_PACKET_NO);

        // Get all the netflowList where packetNo equals to UPDATED_PACKET_NO
        defaultNetflowShouldNotBeFound("packetNo.equals=" + UPDATED_PACKET_NO);
    }

    @Test
    @Transactional
    void getAllNetflowsByPacketNoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where packetNo not equals to DEFAULT_PACKET_NO
        defaultNetflowShouldNotBeFound("packetNo.notEquals=" + DEFAULT_PACKET_NO);

        // Get all the netflowList where packetNo not equals to UPDATED_PACKET_NO
        defaultNetflowShouldBeFound("packetNo.notEquals=" + UPDATED_PACKET_NO);
    }

    @Test
    @Transactional
    void getAllNetflowsByPacketNoIsInShouldWork() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where packetNo in DEFAULT_PACKET_NO or UPDATED_PACKET_NO
        defaultNetflowShouldBeFound("packetNo.in=" + DEFAULT_PACKET_NO + "," + UPDATED_PACKET_NO);

        // Get all the netflowList where packetNo equals to UPDATED_PACKET_NO
        defaultNetflowShouldNotBeFound("packetNo.in=" + UPDATED_PACKET_NO);
    }

    @Test
    @Transactional
    void getAllNetflowsByPacketNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where packetNo is not null
        defaultNetflowShouldBeFound("packetNo.specified=true");

        // Get all the netflowList where packetNo is null
        defaultNetflowShouldNotBeFound("packetNo.specified=false");
    }

    @Test
    @Transactional
    void getAllNetflowsByPacketNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where packetNo is greater than or equal to DEFAULT_PACKET_NO
        defaultNetflowShouldBeFound("packetNo.greaterThanOrEqual=" + DEFAULT_PACKET_NO);

        // Get all the netflowList where packetNo is greater than or equal to UPDATED_PACKET_NO
        defaultNetflowShouldNotBeFound("packetNo.greaterThanOrEqual=" + UPDATED_PACKET_NO);
    }

    @Test
    @Transactional
    void getAllNetflowsByPacketNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where packetNo is less than or equal to DEFAULT_PACKET_NO
        defaultNetflowShouldBeFound("packetNo.lessThanOrEqual=" + DEFAULT_PACKET_NO);

        // Get all the netflowList where packetNo is less than or equal to SMALLER_PACKET_NO
        defaultNetflowShouldNotBeFound("packetNo.lessThanOrEqual=" + SMALLER_PACKET_NO);
    }

    @Test
    @Transactional
    void getAllNetflowsByPacketNoIsLessThanSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where packetNo is less than DEFAULT_PACKET_NO
        defaultNetflowShouldNotBeFound("packetNo.lessThan=" + DEFAULT_PACKET_NO);

        // Get all the netflowList where packetNo is less than UPDATED_PACKET_NO
        defaultNetflowShouldBeFound("packetNo.lessThan=" + UPDATED_PACKET_NO);
    }

    @Test
    @Transactional
    void getAllNetflowsByPacketNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where packetNo is greater than DEFAULT_PACKET_NO
        defaultNetflowShouldNotBeFound("packetNo.greaterThan=" + DEFAULT_PACKET_NO);

        // Get all the netflowList where packetNo is greater than SMALLER_PACKET_NO
        defaultNetflowShouldBeFound("packetNo.greaterThan=" + SMALLER_PACKET_NO);
    }

    @Test
    @Transactional
    void getAllNetflowsByBytesIsEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where bytes equals to DEFAULT_BYTES
        defaultNetflowShouldBeFound("bytes.equals=" + DEFAULT_BYTES);

        // Get all the netflowList where bytes equals to UPDATED_BYTES
        defaultNetflowShouldNotBeFound("bytes.equals=" + UPDATED_BYTES);
    }

    @Test
    @Transactional
    void getAllNetflowsByBytesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where bytes not equals to DEFAULT_BYTES
        defaultNetflowShouldNotBeFound("bytes.notEquals=" + DEFAULT_BYTES);

        // Get all the netflowList where bytes not equals to UPDATED_BYTES
        defaultNetflowShouldBeFound("bytes.notEquals=" + UPDATED_BYTES);
    }

    @Test
    @Transactional
    void getAllNetflowsByBytesIsInShouldWork() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where bytes in DEFAULT_BYTES or UPDATED_BYTES
        defaultNetflowShouldBeFound("bytes.in=" + DEFAULT_BYTES + "," + UPDATED_BYTES);

        // Get all the netflowList where bytes equals to UPDATED_BYTES
        defaultNetflowShouldNotBeFound("bytes.in=" + UPDATED_BYTES);
    }

    @Test
    @Transactional
    void getAllNetflowsByBytesIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where bytes is not null
        defaultNetflowShouldBeFound("bytes.specified=true");

        // Get all the netflowList where bytes is null
        defaultNetflowShouldNotBeFound("bytes.specified=false");
    }

    @Test
    @Transactional
    void getAllNetflowsByBytesContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where bytes contains DEFAULT_BYTES
        defaultNetflowShouldBeFound("bytes.contains=" + DEFAULT_BYTES);

        // Get all the netflowList where bytes contains UPDATED_BYTES
        defaultNetflowShouldNotBeFound("bytes.contains=" + UPDATED_BYTES);
    }

    @Test
    @Transactional
    void getAllNetflowsByBytesNotContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where bytes does not contain DEFAULT_BYTES
        defaultNetflowShouldNotBeFound("bytes.doesNotContain=" + DEFAULT_BYTES);

        // Get all the netflowList where bytes does not contain UPDATED_BYTES
        defaultNetflowShouldBeFound("bytes.doesNotContain=" + UPDATED_BYTES);
    }

    @Test
    @Transactional
    void getAllNetflowsByPpsIsEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where pps equals to DEFAULT_PPS
        defaultNetflowShouldBeFound("pps.equals=" + DEFAULT_PPS);

        // Get all the netflowList where pps equals to UPDATED_PPS
        defaultNetflowShouldNotBeFound("pps.equals=" + UPDATED_PPS);
    }

    @Test
    @Transactional
    void getAllNetflowsByPpsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where pps not equals to DEFAULT_PPS
        defaultNetflowShouldNotBeFound("pps.notEquals=" + DEFAULT_PPS);

        // Get all the netflowList where pps not equals to UPDATED_PPS
        defaultNetflowShouldBeFound("pps.notEquals=" + UPDATED_PPS);
    }

    @Test
    @Transactional
    void getAllNetflowsByPpsIsInShouldWork() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where pps in DEFAULT_PPS or UPDATED_PPS
        defaultNetflowShouldBeFound("pps.in=" + DEFAULT_PPS + "," + UPDATED_PPS);

        // Get all the netflowList where pps equals to UPDATED_PPS
        defaultNetflowShouldNotBeFound("pps.in=" + UPDATED_PPS);
    }

    @Test
    @Transactional
    void getAllNetflowsByPpsIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where pps is not null
        defaultNetflowShouldBeFound("pps.specified=true");

        // Get all the netflowList where pps is null
        defaultNetflowShouldNotBeFound("pps.specified=false");
    }

    @Test
    @Transactional
    void getAllNetflowsByPpsContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where pps contains DEFAULT_PPS
        defaultNetflowShouldBeFound("pps.contains=" + DEFAULT_PPS);

        // Get all the netflowList where pps contains UPDATED_PPS
        defaultNetflowShouldNotBeFound("pps.contains=" + UPDATED_PPS);
    }

    @Test
    @Transactional
    void getAllNetflowsByPpsNotContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where pps does not contain DEFAULT_PPS
        defaultNetflowShouldNotBeFound("pps.doesNotContain=" + DEFAULT_PPS);

        // Get all the netflowList where pps does not contain UPDATED_PPS
        defaultNetflowShouldBeFound("pps.doesNotContain=" + UPDATED_PPS);
    }

    @Test
    @Transactional
    void getAllNetflowsByBpsIsEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where bps equals to DEFAULT_BPS
        defaultNetflowShouldBeFound("bps.equals=" + DEFAULT_BPS);

        // Get all the netflowList where bps equals to UPDATED_BPS
        defaultNetflowShouldNotBeFound("bps.equals=" + UPDATED_BPS);
    }

    @Test
    @Transactional
    void getAllNetflowsByBpsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where bps not equals to DEFAULT_BPS
        defaultNetflowShouldNotBeFound("bps.notEquals=" + DEFAULT_BPS);

        // Get all the netflowList where bps not equals to UPDATED_BPS
        defaultNetflowShouldBeFound("bps.notEquals=" + UPDATED_BPS);
    }

    @Test
    @Transactional
    void getAllNetflowsByBpsIsInShouldWork() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where bps in DEFAULT_BPS or UPDATED_BPS
        defaultNetflowShouldBeFound("bps.in=" + DEFAULT_BPS + "," + UPDATED_BPS);

        // Get all the netflowList where bps equals to UPDATED_BPS
        defaultNetflowShouldNotBeFound("bps.in=" + UPDATED_BPS);
    }

    @Test
    @Transactional
    void getAllNetflowsByBpsIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where bps is not null
        defaultNetflowShouldBeFound("bps.specified=true");

        // Get all the netflowList where bps is null
        defaultNetflowShouldNotBeFound("bps.specified=false");
    }

    @Test
    @Transactional
    void getAllNetflowsByBpsContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where bps contains DEFAULT_BPS
        defaultNetflowShouldBeFound("bps.contains=" + DEFAULT_BPS);

        // Get all the netflowList where bps contains UPDATED_BPS
        defaultNetflowShouldNotBeFound("bps.contains=" + UPDATED_BPS);
    }

    @Test
    @Transactional
    void getAllNetflowsByBpsNotContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where bps does not contain DEFAULT_BPS
        defaultNetflowShouldNotBeFound("bps.doesNotContain=" + DEFAULT_BPS);

        // Get all the netflowList where bps does not contain UPDATED_BPS
        defaultNetflowShouldBeFound("bps.doesNotContain=" + UPDATED_BPS);
    }

    @Test
    @Transactional
    void getAllNetflowsByBppIsEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where bpp equals to DEFAULT_BPP
        defaultNetflowShouldBeFound("bpp.equals=" + DEFAULT_BPP);

        // Get all the netflowList where bpp equals to UPDATED_BPP
        defaultNetflowShouldNotBeFound("bpp.equals=" + UPDATED_BPP);
    }

    @Test
    @Transactional
    void getAllNetflowsByBppIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where bpp not equals to DEFAULT_BPP
        defaultNetflowShouldNotBeFound("bpp.notEquals=" + DEFAULT_BPP);

        // Get all the netflowList where bpp not equals to UPDATED_BPP
        defaultNetflowShouldBeFound("bpp.notEquals=" + UPDATED_BPP);
    }

    @Test
    @Transactional
    void getAllNetflowsByBppIsInShouldWork() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where bpp in DEFAULT_BPP or UPDATED_BPP
        defaultNetflowShouldBeFound("bpp.in=" + DEFAULT_BPP + "," + UPDATED_BPP);

        // Get all the netflowList where bpp equals to UPDATED_BPP
        defaultNetflowShouldNotBeFound("bpp.in=" + UPDATED_BPP);
    }

    @Test
    @Transactional
    void getAllNetflowsByBppIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where bpp is not null
        defaultNetflowShouldBeFound("bpp.specified=true");

        // Get all the netflowList where bpp is null
        defaultNetflowShouldNotBeFound("bpp.specified=false");
    }

    @Test
    @Transactional
    void getAllNetflowsByBppContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where bpp contains DEFAULT_BPP
        defaultNetflowShouldBeFound("bpp.contains=" + DEFAULT_BPP);

        // Get all the netflowList where bpp contains UPDATED_BPP
        defaultNetflowShouldNotBeFound("bpp.contains=" + UPDATED_BPP);
    }

    @Test
    @Transactional
    void getAllNetflowsByBppNotContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where bpp does not contain DEFAULT_BPP
        defaultNetflowShouldNotBeFound("bpp.doesNotContain=" + DEFAULT_BPP);

        // Get all the netflowList where bpp does not contain UPDATED_BPP
        defaultNetflowShouldBeFound("bpp.doesNotContain=" + UPDATED_BPP);
    }

    @Test
    @Transactional
    void getAllNetflowsByFlowsIsEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where flows equals to DEFAULT_FLOWS
        defaultNetflowShouldBeFound("flows.equals=" + DEFAULT_FLOWS);

        // Get all the netflowList where flows equals to UPDATED_FLOWS
        defaultNetflowShouldNotBeFound("flows.equals=" + UPDATED_FLOWS);
    }

    @Test
    @Transactional
    void getAllNetflowsByFlowsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where flows not equals to DEFAULT_FLOWS
        defaultNetflowShouldNotBeFound("flows.notEquals=" + DEFAULT_FLOWS);

        // Get all the netflowList where flows not equals to UPDATED_FLOWS
        defaultNetflowShouldBeFound("flows.notEquals=" + UPDATED_FLOWS);
    }

    @Test
    @Transactional
    void getAllNetflowsByFlowsIsInShouldWork() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where flows in DEFAULT_FLOWS or UPDATED_FLOWS
        defaultNetflowShouldBeFound("flows.in=" + DEFAULT_FLOWS + "," + UPDATED_FLOWS);

        // Get all the netflowList where flows equals to UPDATED_FLOWS
        defaultNetflowShouldNotBeFound("flows.in=" + UPDATED_FLOWS);
    }

    @Test
    @Transactional
    void getAllNetflowsByFlowsIsNullOrNotNull() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where flows is not null
        defaultNetflowShouldBeFound("flows.specified=true");

        // Get all the netflowList where flows is null
        defaultNetflowShouldNotBeFound("flows.specified=false");
    }

    @Test
    @Transactional
    void getAllNetflowsByFlowsContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where flows contains DEFAULT_FLOWS
        defaultNetflowShouldBeFound("flows.contains=" + DEFAULT_FLOWS);

        // Get all the netflowList where flows contains UPDATED_FLOWS
        defaultNetflowShouldNotBeFound("flows.contains=" + UPDATED_FLOWS);
    }

    @Test
    @Transactional
    void getAllNetflowsByFlowsNotContainsSomething() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        // Get all the netflowList where flows does not contain DEFAULT_FLOWS
        defaultNetflowShouldNotBeFound("flows.doesNotContain=" + DEFAULT_FLOWS);

        // Get all the netflowList where flows does not contain UPDATED_FLOWS
        defaultNetflowShouldBeFound("flows.doesNotContain=" + UPDATED_FLOWS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNetflowShouldBeFound(String filter) throws Exception {
        restNetflowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(netflow.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateFirstSeen").value(hasItem(DEFAULT_DATE_FIRST_SEEN.toString())))
            .andExpect(jsonPath("$.[*].timeFirstSeen").value(hasItem(DEFAULT_TIME_FIRST_SEEN)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(sameNumber(DEFAULT_DURATION))))
            .andExpect(jsonPath("$.[*].protocol").value(hasItem(DEFAULT_PROTOCOL)))
            .andExpect(jsonPath("$.[*].srcIp").value(hasItem(DEFAULT_SRC_IP)))
            .andExpect(jsonPath("$.[*].dstIp").value(hasItem(DEFAULT_DST_IP)))
            .andExpect(jsonPath("$.[*].flags").value(hasItem(DEFAULT_FLAGS)))
            .andExpect(jsonPath("$.[*].tos").value(hasItem(DEFAULT_TOS)))
            .andExpect(jsonPath("$.[*].packetNo").value(hasItem(DEFAULT_PACKET_NO)))
            .andExpect(jsonPath("$.[*].bytes").value(hasItem(DEFAULT_BYTES)))
            .andExpect(jsonPath("$.[*].pps").value(hasItem(DEFAULT_PPS)))
            .andExpect(jsonPath("$.[*].bps").value(hasItem(DEFAULT_BPS)))
            .andExpect(jsonPath("$.[*].bpp").value(hasItem(DEFAULT_BPP)))
            .andExpect(jsonPath("$.[*].flows").value(hasItem(DEFAULT_FLOWS)));

        // Check, that the count call also returns 1
        restNetflowMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNetflowShouldNotBeFound(String filter) throws Exception {
        restNetflowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNetflowMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNetflow() throws Exception {
        // Get the netflow
        restNetflowMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNetflow() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        int databaseSizeBeforeUpdate = netflowRepository.findAll().size();

        // Update the netflow
        Netflow updatedNetflow = netflowRepository.findById(netflow.getId()).get();
        // Disconnect from session so that the updates on updatedNetflow are not directly saved in db
        em.detach(updatedNetflow);
        updatedNetflow
            .dateFirstSeen(UPDATED_DATE_FIRST_SEEN)
            .timeFirstSeen(UPDATED_TIME_FIRST_SEEN)
            .duration(UPDATED_DURATION)
            .protocol(UPDATED_PROTOCOL)
            .srcIp(UPDATED_SRC_IP)
            .dstIp(UPDATED_DST_IP)
            .flags(UPDATED_FLAGS)
            .tos(UPDATED_TOS)
            .packetNo(UPDATED_PACKET_NO)
            .bytes(UPDATED_BYTES)
            .pps(UPDATED_PPS)
            .bps(UPDATED_BPS)
            .bpp(UPDATED_BPP)
            .flows(UPDATED_FLOWS);
        NetflowDTO netflowDTO = netflowMapper.toDto(updatedNetflow);

        restNetflowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, netflowDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(netflowDTO))
            )
            .andExpect(status().isOk());

        // Validate the Netflow in the database
        List<Netflow> netflowList = netflowRepository.findAll();
        assertThat(netflowList).hasSize(databaseSizeBeforeUpdate);
        Netflow testNetflow = netflowList.get(netflowList.size() - 1);
        assertThat(testNetflow.getDateFirstSeen()).isEqualTo(UPDATED_DATE_FIRST_SEEN);
        assertThat(testNetflow.getTimeFirstSeen()).isEqualTo(UPDATED_TIME_FIRST_SEEN);
        assertThat(testNetflow.getDuration()).isEqualByComparingTo(UPDATED_DURATION);
        assertThat(testNetflow.getProtocol()).isEqualTo(UPDATED_PROTOCOL);
        assertThat(testNetflow.getSrcIp()).isEqualTo(UPDATED_SRC_IP);
        assertThat(testNetflow.getDstIp()).isEqualTo(UPDATED_DST_IP);
        assertThat(testNetflow.getFlags()).isEqualTo(UPDATED_FLAGS);
        assertThat(testNetflow.getTos()).isEqualTo(UPDATED_TOS);
        assertThat(testNetflow.getPacketNo()).isEqualTo(UPDATED_PACKET_NO);
        assertThat(testNetflow.getBytes()).isEqualTo(UPDATED_BYTES);
        assertThat(testNetflow.getPps()).isEqualTo(UPDATED_PPS);
        assertThat(testNetflow.getBps()).isEqualTo(UPDATED_BPS);
        assertThat(testNetflow.getBpp()).isEqualTo(UPDATED_BPP);
        assertThat(testNetflow.getFlows()).isEqualTo(UPDATED_FLOWS);
    }

    @Test
    @Transactional
    void putNonExistingNetflow() throws Exception {
        int databaseSizeBeforeUpdate = netflowRepository.findAll().size();
        netflow.setId(count.incrementAndGet());

        // Create the Netflow
        NetflowDTO netflowDTO = netflowMapper.toDto(netflow);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNetflowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, netflowDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(netflowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Netflow in the database
        List<Netflow> netflowList = netflowRepository.findAll();
        assertThat(netflowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNetflow() throws Exception {
        int databaseSizeBeforeUpdate = netflowRepository.findAll().size();
        netflow.setId(count.incrementAndGet());

        // Create the Netflow
        NetflowDTO netflowDTO = netflowMapper.toDto(netflow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNetflowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(netflowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Netflow in the database
        List<Netflow> netflowList = netflowRepository.findAll();
        assertThat(netflowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNetflow() throws Exception {
        int databaseSizeBeforeUpdate = netflowRepository.findAll().size();
        netflow.setId(count.incrementAndGet());

        // Create the Netflow
        NetflowDTO netflowDTO = netflowMapper.toDto(netflow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNetflowMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(netflowDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Netflow in the database
        List<Netflow> netflowList = netflowRepository.findAll();
        assertThat(netflowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNetflowWithPatch() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        int databaseSizeBeforeUpdate = netflowRepository.findAll().size();

        // Update the netflow using partial update
        Netflow partialUpdatedNetflow = new Netflow();
        partialUpdatedNetflow.setId(netflow.getId());

        partialUpdatedNetflow
            .timeFirstSeen(UPDATED_TIME_FIRST_SEEN)
            .duration(UPDATED_DURATION)
            .srcIp(UPDATED_SRC_IP)
            .tos(UPDATED_TOS)
            .bpp(UPDATED_BPP)
            .flows(UPDATED_FLOWS);

        restNetflowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNetflow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNetflow))
            )
            .andExpect(status().isOk());

        // Validate the Netflow in the database
        List<Netflow> netflowList = netflowRepository.findAll();
        assertThat(netflowList).hasSize(databaseSizeBeforeUpdate);
        Netflow testNetflow = netflowList.get(netflowList.size() - 1);
        assertThat(testNetflow.getDateFirstSeen()).isEqualTo(DEFAULT_DATE_FIRST_SEEN);
        assertThat(testNetflow.getTimeFirstSeen()).isEqualTo(UPDATED_TIME_FIRST_SEEN);
        assertThat(testNetflow.getDuration()).isEqualByComparingTo(UPDATED_DURATION);
        assertThat(testNetflow.getProtocol()).isEqualTo(DEFAULT_PROTOCOL);
        assertThat(testNetflow.getSrcIp()).isEqualTo(UPDATED_SRC_IP);
        assertThat(testNetflow.getDstIp()).isEqualTo(DEFAULT_DST_IP);
        assertThat(testNetflow.getFlags()).isEqualTo(DEFAULT_FLAGS);
        assertThat(testNetflow.getTos()).isEqualTo(UPDATED_TOS);
        assertThat(testNetflow.getPacketNo()).isEqualTo(DEFAULT_PACKET_NO);
        assertThat(testNetflow.getBytes()).isEqualTo(DEFAULT_BYTES);
        assertThat(testNetflow.getPps()).isEqualTo(DEFAULT_PPS);
        assertThat(testNetflow.getBps()).isEqualTo(DEFAULT_BPS);
        assertThat(testNetflow.getBpp()).isEqualTo(UPDATED_BPP);
        assertThat(testNetflow.getFlows()).isEqualTo(UPDATED_FLOWS);
    }

    @Test
    @Transactional
    void fullUpdateNetflowWithPatch() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        int databaseSizeBeforeUpdate = netflowRepository.findAll().size();

        // Update the netflow using partial update
        Netflow partialUpdatedNetflow = new Netflow();
        partialUpdatedNetflow.setId(netflow.getId());

        partialUpdatedNetflow
            .dateFirstSeen(UPDATED_DATE_FIRST_SEEN)
            .timeFirstSeen(UPDATED_TIME_FIRST_SEEN)
            .duration(UPDATED_DURATION)
            .protocol(UPDATED_PROTOCOL)
            .srcIp(UPDATED_SRC_IP)
            .dstIp(UPDATED_DST_IP)
            .flags(UPDATED_FLAGS)
            .tos(UPDATED_TOS)
            .packetNo(UPDATED_PACKET_NO)
            .bytes(UPDATED_BYTES)
            .pps(UPDATED_PPS)
            .bps(UPDATED_BPS)
            .bpp(UPDATED_BPP)
            .flows(UPDATED_FLOWS);

        restNetflowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNetflow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNetflow))
            )
            .andExpect(status().isOk());

        // Validate the Netflow in the database
        List<Netflow> netflowList = netflowRepository.findAll();
        assertThat(netflowList).hasSize(databaseSizeBeforeUpdate);
        Netflow testNetflow = netflowList.get(netflowList.size() - 1);
        assertThat(testNetflow.getDateFirstSeen()).isEqualTo(UPDATED_DATE_FIRST_SEEN);
        assertThat(testNetflow.getTimeFirstSeen()).isEqualTo(UPDATED_TIME_FIRST_SEEN);
        assertThat(testNetflow.getDuration()).isEqualByComparingTo(UPDATED_DURATION);
        assertThat(testNetflow.getProtocol()).isEqualTo(UPDATED_PROTOCOL);
        assertThat(testNetflow.getSrcIp()).isEqualTo(UPDATED_SRC_IP);
        assertThat(testNetflow.getDstIp()).isEqualTo(UPDATED_DST_IP);
        assertThat(testNetflow.getFlags()).isEqualTo(UPDATED_FLAGS);
        assertThat(testNetflow.getTos()).isEqualTo(UPDATED_TOS);
        assertThat(testNetflow.getPacketNo()).isEqualTo(UPDATED_PACKET_NO);
        assertThat(testNetflow.getBytes()).isEqualTo(UPDATED_BYTES);
        assertThat(testNetflow.getPps()).isEqualTo(UPDATED_PPS);
        assertThat(testNetflow.getBps()).isEqualTo(UPDATED_BPS);
        assertThat(testNetflow.getBpp()).isEqualTo(UPDATED_BPP);
        assertThat(testNetflow.getFlows()).isEqualTo(UPDATED_FLOWS);
    }

    @Test
    @Transactional
    void patchNonExistingNetflow() throws Exception {
        int databaseSizeBeforeUpdate = netflowRepository.findAll().size();
        netflow.setId(count.incrementAndGet());

        // Create the Netflow
        NetflowDTO netflowDTO = netflowMapper.toDto(netflow);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNetflowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, netflowDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(netflowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Netflow in the database
        List<Netflow> netflowList = netflowRepository.findAll();
        assertThat(netflowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNetflow() throws Exception {
        int databaseSizeBeforeUpdate = netflowRepository.findAll().size();
        netflow.setId(count.incrementAndGet());

        // Create the Netflow
        NetflowDTO netflowDTO = netflowMapper.toDto(netflow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNetflowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(netflowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Netflow in the database
        List<Netflow> netflowList = netflowRepository.findAll();
        assertThat(netflowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNetflow() throws Exception {
        int databaseSizeBeforeUpdate = netflowRepository.findAll().size();
        netflow.setId(count.incrementAndGet());

        // Create the Netflow
        NetflowDTO netflowDTO = netflowMapper.toDto(netflow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNetflowMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(netflowDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Netflow in the database
        List<Netflow> netflowList = netflowRepository.findAll();
        assertThat(netflowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNetflow() throws Exception {
        // Initialize the database
        netflowRepository.saveAndFlush(netflow);

        int databaseSizeBeforeDelete = netflowRepository.findAll().size();

        // Delete the netflow
        restNetflowMockMvc
            .perform(delete(ENTITY_API_URL_ID, netflow.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Netflow> netflowList = netflowRepository.findAll();
        assertThat(netflowList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
