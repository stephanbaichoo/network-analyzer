package com.tattea.analyzer.service;

import com.tattea.analyzer.domain.*; // for static metamodels
import com.tattea.analyzer.domain.Netflow;
import com.tattea.analyzer.repository.NetflowRepository;
import com.tattea.analyzer.service.criteria.NetflowCriteria;
import com.tattea.analyzer.service.dto.NetflowDTO;
import com.tattea.analyzer.service.mapper.NetflowMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Netflow} entities in the database.
 * The main input is a {@link NetflowCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NetflowDTO} or a {@link Page} of {@link NetflowDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NetflowQueryService extends QueryService<Netflow> {

    private final Logger log = LoggerFactory.getLogger(NetflowQueryService.class);

    private final NetflowRepository netflowRepository;

    private final NetflowMapper netflowMapper;

    public NetflowQueryService(NetflowRepository netflowRepository, NetflowMapper netflowMapper) {
        this.netflowRepository = netflowRepository;
        this.netflowMapper = netflowMapper;
    }

    /**
     * Return a {@link List} of {@link NetflowDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NetflowDTO> findByCriteria(NetflowCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Netflow> specification = createSpecification(criteria);
        return netflowMapper.toDto(netflowRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NetflowDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NetflowDTO> findByCriteria(NetflowCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Netflow> specification = createSpecification(criteria);
        return netflowRepository.findAll(specification, page).map(netflowMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NetflowCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Netflow> specification = createSpecification(criteria);
        return netflowRepository.count(specification);
    }

    /**
     * Function to convert {@link NetflowCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Netflow> createSpecification(NetflowCriteria criteria) {
        Specification<Netflow> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Netflow_.id));
            }
            if (criteria.getDateFirstSeen() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFirstSeen(), Netflow_.dateFirstSeen));
            }
            if (criteria.getTimeFirstSeen() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTimeFirstSeen(), Netflow_.timeFirstSeen));
            }
            if (criteria.getDuration() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDuration(), Netflow_.duration));
            }
            if (criteria.getProtocol() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProtocol(), Netflow_.protocol));
            }
            if (criteria.getSrcIp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSrcIp(), Netflow_.srcIp));
            }
            if (criteria.getDstIp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDstIp(), Netflow_.dstIp));
            }
            if (criteria.getFlags() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFlags(), Netflow_.flags));
            }
            if (criteria.getTos() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTos(), Netflow_.tos));
            }
            if (criteria.getPacketNo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPacketNo(), Netflow_.packetNo));
            }
            if (criteria.getBytes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBytes(), Netflow_.bytes));
            }
            if (criteria.getPps() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPps(), Netflow_.pps));
            }
            if (criteria.getBps() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBps(), Netflow_.bps));
            }
            if (criteria.getBpp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBpp(), Netflow_.bpp));
            }
            if (criteria.getFlows() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFlows(), Netflow_.flows));
            }
        }
        return specification;
    }
}
