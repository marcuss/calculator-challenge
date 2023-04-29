package pro.marcuss.calculator.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.marcuss.calculator.domain.UserBalance;
import pro.marcuss.calculator.repository.UserBalanceRepository;
import pro.marcuss.calculator.service.UserBalanceService;
import pro.marcuss.calculator.service.dto.UserBalanceDTO;
import pro.marcuss.calculator.service.mapper.UserBalanceMapper;

/**
 * Service Implementation for managing {@link UserBalance}.
 */
@Service
public class UserBalanceServiceImpl implements UserBalanceService {

    private final Logger log = LoggerFactory.getLogger(UserBalanceServiceImpl.class);

    private final UserBalanceRepository userBalanceRepository;

    private final UserBalanceMapper userBalanceMapper;

    public UserBalanceServiceImpl(UserBalanceRepository userBalanceRepository, UserBalanceMapper userBalanceMapper) {
        this.userBalanceRepository = userBalanceRepository;
        this.userBalanceMapper = userBalanceMapper;
    }

    @Override
    public UserBalanceDTO save(UserBalanceDTO userBalanceDTO) {
        log.debug("Request to save UserBalance : {}", userBalanceDTO);
        UserBalance userBalance = userBalanceMapper.toEntity(userBalanceDTO);
        userBalance = userBalanceRepository.save(userBalance);
        return userBalanceMapper.toDto(userBalance);
    }

    @Override
    public UserBalanceDTO update(UserBalanceDTO userBalanceDTO) {
        log.debug("Request to update UserBalance : {}", userBalanceDTO);
        UserBalance userBalance = userBalanceMapper.toEntity(userBalanceDTO);
        userBalance = userBalanceRepository.save(userBalance);
        return userBalanceMapper.toDto(userBalance);
    }

    @Override
    public Optional<UserBalanceDTO> partialUpdate(UserBalanceDTO userBalanceDTO) {
        log.debug("Request to partially update UserBalance : {}", userBalanceDTO);

        return userBalanceRepository
            .findById(userBalanceDTO.getId())
            .map(existingUserBalance -> {
                userBalanceMapper.partialUpdate(existingUserBalance, userBalanceDTO);

                return existingUserBalance;
            })
            .map(userBalanceRepository::save)
            .map(userBalanceMapper::toDto);
    }

    @Override
    public List<UserBalanceDTO> findAll() {
        log.debug("Request to get all UserBalances");
        return userBalanceRepository.findAll().stream().map(userBalanceMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<UserBalanceDTO> findOne(String id) {
        log.debug("Request to get UserBalance : {}", id);
        return userBalanceRepository.findById(id).map(userBalanceMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete UserBalance : {}", id);
        userBalanceRepository.deleteById(id);
    }
}
