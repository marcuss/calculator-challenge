package pro.marcuss.calculator.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pro.marcuss.calculator.domain.Record;
import pro.marcuss.calculator.domain.User;
import pro.marcuss.calculator.service.dto.RecordDTO;
import pro.marcuss.calculator.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Record} and its DTO {@link RecordDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecordMapper extends EntityMapper<RecordDTO, Record> {

    RecordDTO toDto(Record s);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserId(User user);
}
