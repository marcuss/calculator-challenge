package pro.marcuss.calculator.service.mapper;

import org.mapstruct.*;
import pro.marcuss.calculator.domain.Record;
import pro.marcuss.calculator.domain.User;
import pro.marcuss.calculator.service.dto.RecordDTO;
import pro.marcuss.calculator.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Record} and its DTO {@link RecordDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecordMapper extends EntityMapper<RecordDTO, Record> {
    @Mapping(target = "userId", source = "userId", qualifiedByName = "userId")
    RecordDTO toDto(Record s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
