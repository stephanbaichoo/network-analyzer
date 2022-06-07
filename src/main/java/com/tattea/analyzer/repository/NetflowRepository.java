package com.tattea.analyzer.repository;

import com.tattea.analyzer.domain.Netflow;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Netflow entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NetflowRepository extends JpaRepository<Netflow, Long>, JpaSpecificationExecutor<Netflow> {}
