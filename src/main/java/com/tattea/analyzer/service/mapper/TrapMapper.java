package com.tattea.analyzer.service.mapper;

import com.tattea.analyzer.domain.Trap;
import com.tattea.analyzer.service.dto.TrapDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Trap} and its DTO {@link TrapDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrapMapper extends EntityMapper<TrapDTO, Trap> {}
