package com.tattea.analyzer.repository;

import com.tattea.analyzer.domain.Host;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Host entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HostRepository extends JpaRepository<Host, Long> {
    Optional<Host> findFirstByIpAddress(String ip);
}
