package com.tattea.analyzer.service.mapper;

import com.tattea.analyzer.domain.Host;
import com.tattea.analyzer.service.dto.HostDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Host} and its DTO {@link HostDTO}.
 */
@Mapper(componentModel = "spring")
public interface HostMapper extends EntityMapper<HostDTO, Host> {}
