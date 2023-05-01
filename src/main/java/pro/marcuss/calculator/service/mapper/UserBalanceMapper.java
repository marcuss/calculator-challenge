package pro.marcuss.calculator.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pro.marcuss.calculator.domain.User;
import pro.marcuss.calculator.domain.UserBalance;
import pro.marcuss.calculator.service.dto.UserBalanceDTO;
import pro.marcuss.calculator.service.dto.UserDTO;

/**
 * Mapper for the entity {@link UserBalance} and its DTO {@link UserBalanceDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserBalanceMapper extends EntityMapper<UserBalanceDTO, UserBalance> {
    @Mapping(target = "user.login", source = "userLogin")
    UserBalanceDTO toDto(UserBalance s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserId(User user);
}
