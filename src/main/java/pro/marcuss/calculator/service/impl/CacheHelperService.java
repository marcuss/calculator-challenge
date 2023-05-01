package pro.marcuss.calculator.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import pro.marcuss.calculator.domain.Record;
import pro.marcuss.calculator.domain.UserBalance;
import pro.marcuss.calculator.repository.RecordRepository;
import pro.marcuss.calculator.repository.UserBalanceRepository;

import java.util.Objects;
import java.util.Optional;

import static pro.marcuss.calculator.repository.RecordRepository.LAST_OPERATION_RESPONSE_BY_LOGIN;

@Service
public class CacheHelperService {

    private final Logger log = LoggerFactory.getLogger(CacheHelperService.class);
    private final CacheManager cacheManager;

    private final RecordRepository recordRepository;

    public CacheHelperService(CacheManager cacheManager, RecordRepository recordRepository) {
        this.cacheManager = cacheManager;
        this.recordRepository = recordRepository;
    }

    public void updateLastOperationByLogin(String login, Record record) {
        log.debug("Attempt to update " + LAST_OPERATION_RESPONSE_BY_LOGIN + " cache. %s : %s", login, record);
        Cache cache = cacheManager.getCache(LAST_OPERATION_RESPONSE_BY_LOGIN);
        if (cache == null) {
            log.error("Failed to update" + LAST_OPERATION_RESPONSE_BY_LOGIN + " cache.");
            return;
        }
        cache.put(login, record);
    }

    void updateUserBalanceCache(String login, UserBalance userBalance) {
        Objects.requireNonNull(cacheManager.getCache(UserBalanceRepository.BALANCE_BY_USER_ID_LOGIN)).put(login, Optional.of(userBalance));
    }

    public void clearUserBalanceCache(String login) {
        Objects.requireNonNull(cacheManager.getCache(UserBalanceRepository.BALANCE_BY_USER_ID_LOGIN)).evict(login);
    }

    public void clearUserBalanceCacheFull() {
        Objects.requireNonNull(cacheManager.getCache(UserBalanceRepository.BALANCE_BY_USER_ID_LOGIN)).invalidate();
    }
}
