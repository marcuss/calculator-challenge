package pro.marcuss.calculator.service.mapper;

import org.mapstruct.*;
import pro.marcuss.calculator.domain.User;
import pro.marcuss.calculator.domain.UserBalance;
import pro.marcuss.calculator.service.dto.UserBalanceDTO;
import pro.marcuss.calculator.service.dto.UserDTO;

/**
 * Mapper for the entity {@link UserBalance} and its DTO {@link UserBalanceDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserBalanceMapper extends EntityMapper<UserBalanceDTO, UserBalance> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    UserBalanceDTO toDto(UserBalance s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
