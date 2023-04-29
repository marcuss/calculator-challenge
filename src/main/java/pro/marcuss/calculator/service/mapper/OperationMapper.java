package pro.marcuss.calculator.service.mapper;

import org.mapstruct.*;
import pro.marcuss.calculator.domain.Operation;
import pro.marcuss.calculator.service.dto.OperationDTO;

/**
 * Mapper for the entity {@link Operation} and its DTO {@link OperationDTO}.
 */
@Mapper(componentModel = "spring")
public interface OperationMapper extends EntityMapper<OperationDTO, Operation> {}
