package com.tattea.analyzer.repository;

import com.tattea.analyzer.domain.Trap;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Trap entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrapRepository extends JpaRepository<Trap, Long> {}
