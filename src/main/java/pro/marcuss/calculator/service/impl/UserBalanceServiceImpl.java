package pro.marcuss.calculator.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pro.marcuss.calculator.domain.UserBalance;
import pro.marcuss.calculator.repository.UserBalanceRepository;
import pro.marcuss.calculator.service.UserBalanceService;
import pro.marcuss.calculator.service.dto.UserBalanceDTO;
import pro.marcuss.calculator.service.mapper.UserBalanceMapper;

import java.util.Objects;
import java.util.Optional;

/**
 * Service Implementation for managing {@link UserBalance}.
 */
@Service
public class UserBalanceServiceImpl implements UserBalanceService {

    private final Logger log = LoggerFactory.getLogger(UserBalanceServiceImpl.class);

    private final UserBalanceRepository userBalanceRepository;

    private final UserBalanceMapper userBalanceMapper;

    private final CacheManager cacheManager;

    public UserBalanceServiceImpl(UserBalanceRepository userBalanceRepository,
                                  UserBalanceMapper userBalanceMapper,
                                  CacheManager cacheManager) {
        this.userBalanceRepository = userBalanceRepository;
        this.userBalanceMapper = userBalanceMapper;
        this.cacheManager = cacheManager;
    }

    @Override
    public UserBalanceDTO save(UserBalanceDTO userBalanceDTO) {
        log.debug("Request to save UserBalance : {}", userBalanceDTO);
        UserBalance userBalance = userBalanceMapper.toEntity(userBalanceDTO);
        userBalance = userBalanceRepository.save(userBalance);
        updateUserCache(userBalance.getUserLogin(), userBalance);
        return userBalanceMapper.toDto(userBalance);
    }

    @Override
    public UserBalanceDTO update(UserBalanceDTO userBalanceDTO) {
        log.debug("Request to update UserBalance : {}", userBalanceDTO);
        UserBalance userBalance = userBalanceMapper.toEntity(userBalanceDTO);
        userBalance = userBalanceRepository.save(userBalance);
        updateUserCache(userBalance.getId(), userBalance);
        return userBalanceMapper.toDto(userBalance);
    }

    @Override
    public Optional<UserBalanceDTO> partialUpdate(UserBalanceDTO userBalanceDTO) {
        log.debug("Request to partially update UserBalance : {}", userBalanceDTO);

        Optional<UserBalanceDTO> updated = userBalanceRepository
            .findById(userBalanceDTO.getId())
            .map(existingUserBalance -> {
                userBalanceMapper.partialUpdate(existingUserBalance, userBalanceDTO);

                return existingUserBalance;
            })
            .map(userBalanceRepository::save)
            .map(userBalanceMapper::toDto);
        clearUserCaches(updated.get().getUserLogin());
        return updated;
    }

    @Override
    public Page<UserBalanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserBalances");
        return userBalanceRepository.findAll(pageable).map(userBalanceMapper::toDto);
    }

    @Override
    public Optional<UserBalanceDTO> findOne(String id) {
        log.debug("Request to get UserBalance : {}", id);
        return userBalanceRepository.findById(id).map(userBalanceMapper::toDto);
    }

    @Override
    public Optional<UserBalanceDTO> findUserBalanceByUserLogin(String login) {
        log.debug("Request to get UserBalance by login: {}", login);
        return userBalanceRepository.findUserBalanceByUserLogin(login).map(userBalanceMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete UserBalance : {}", id);
        userBalanceRepository.deleteById(id);
        clearUserCaches();
    }

    private void clearUserCaches() {
        Objects.requireNonNull(cacheManager.getCache(UserBalanceRepository.BALANCE_BY_USER_ID_LOGIN)).invalidate();
    }

    private void clearUserCaches(String login) {
        Objects.requireNonNull(cacheManager.getCache(UserBalanceRepository.BALANCE_BY_USER_ID_LOGIN)).evict(login);
    }

    private void updateUserCache(String login, UserBalance userBalance) {
        Objects.requireNonNull(cacheManager.getCache(UserBalanceRepository.BALANCE_BY_USER_ID_LOGIN)).put(login, Optional.of(userBalance));
    }
}
