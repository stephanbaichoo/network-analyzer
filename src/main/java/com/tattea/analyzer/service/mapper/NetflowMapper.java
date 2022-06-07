package com.tattea.analyzer.service.mapper;

import com.tattea.analyzer.domain.Netflow;
import com.tattea.analyzer.service.dto.NetflowDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Netflow} and its DTO {@link NetflowDTO}.
 */
@Mapper(componentModel = "spring")
public interface NetflowMapper extends EntityMapper<NetflowDTO, Netflow> {}
