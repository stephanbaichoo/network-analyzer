package com.tattea.analyzer.repository;

import com.tattea.analyzer.domain.Port;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Port entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PortRepository extends JpaRepository<Port, Long> {}
