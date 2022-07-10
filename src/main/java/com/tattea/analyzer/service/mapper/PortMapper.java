package com.tattea.analyzer.service.mapper;

import com.tattea.analyzer.domain.Port;
import com.tattea.analyzer.service.dto.PortDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Port} and its DTO {@link PortDTO}.
 */
@Mapper(componentModel = "spring")
public interface PortMapper extends EntityMapper<PortDTO, Port> {}
