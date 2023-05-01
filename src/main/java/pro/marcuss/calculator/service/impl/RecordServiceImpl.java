package pro.marcuss.calculator.service.impl;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pro.marcuss.calculator.domain.Record;
import pro.marcuss.calculator.repository.RecordRepository;
import pro.marcuss.calculator.service.*;
import pro.marcuss.calculator.service.dto.OperationDTO;
import pro.marcuss.calculator.service.dto.RecordDTO;
import pro.marcuss.calculator.service.dto.UserBalanceDTO;
import pro.marcuss.calculator.service.mapper.RecordMapper;

import java.time.Instant;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Record}.
 */
@Service
public class RecordServiceImpl implements RecordService {

    private final Logger log = LoggerFactory.getLogger(RecordServiceImpl.class);

    private final RecordRepository recordRepository;

    private final RecordMapper recordMapper;

    private final UserService userService;

    private final OperationService operationCosts;

    private final UserBalanceService userBalanceService;

    private final CacheHelperService cacheHelperService;

    private final RandomStringService randomStringService;

    public RecordServiceImpl(RecordRepository recordRepository,
                             RecordMapper recordMapper,
                             UserService userService,
                             OperationService operationService,
                             UserBalanceService userBalanceService,
                             CacheHelperService cacheHelperService,
                             RandomStringService randomStringService) {
        this.recordRepository = recordRepository;
        this.recordMapper = recordMapper;
        this.userService = userService;
        this.operationCosts = operationService;
        this.userBalanceService = userBalanceService;
        this.cacheHelperService = cacheHelperService;
        this.randomStringService = randomStringService;
    }

    @Override
    public RecordDTO save(RecordDTO recordDTO) {
        log.debug("Request to save Record : {}", recordDTO);
        if (recordDTO.getUserLogin() == null) {
            throw new RuntimeException("No user login associated to operation execution");
        }
        recordDTO.setActive(true);
        recordDTO.setDate(Instant.now());

        Optional<UserBalanceDTO> balanceDTO = calculateNewUserBalance(recordDTO);

        String lastOperationResponse = "0";
        Record lastUserRecord = recordRepository //Try to fetch from a cacheable repository call
            .findFirstByUserLoginAndActiveIsTrueOrderByDateDesc(recordDTO.getUserLogin());
        if (lastUserRecord != null && lastUserRecord.getOperationResponse() != null) {
            lastOperationResponse = lastUserRecord.getOperationResponse();
        }

        String updatedOperationResponse = executeOperation(lastOperationResponse, recordDTO);
        recordDTO.setOperationResponse(updatedOperationResponse);
        Record newUserRecord = recordRepository.save(recordMapper.toEntity(recordDTO));
        updateCachesAndBalances(recordDTO, balanceDTO);
        return recordMapper.toDto(newUserRecord);
    }

    private Optional<UserBalanceDTO> calculateNewUserBalance(RecordDTO recordDTO) {
        Optional<OperationDTO> operationDTO = operationCosts.findOneByOperator(recordDTO.getOperation());
        if (operationDTO.isEmpty()) {
            throw new RuntimeException("Operation cost not found");
        }

        Optional<UserBalanceDTO> balanceDTO = userBalanceService.findUserBalanceByUserLogin(recordDTO.getUserLogin());
        if (balanceDTO.isEmpty()) {
            throw new RuntimeException("Missing balance registry for User: " + recordDTO.getUserLogin());
        }
        recordDTO.setUserBalance(balanceDTO.get().getBalance() - operationDTO.get().getCost());
        return balanceDTO;
    }

    private String executeOperation(String lastOperationResponse, RecordDTO recordDTO) {
        double lastResponseNumeric = 0;
        if (NumberUtils.isCreatable(lastOperationResponse)) {
            lastResponseNumeric = Double.parseDouble(lastOperationResponse);
        }
        switch (recordDTO.getOperation()) {
            case ADD:
                return String.valueOf(lastResponseNumeric + recordDTO.getAmount());
            case SUBSTRACT:
                return String.valueOf(lastResponseNumeric - recordDTO.getAmount());
            case MULTIPLY:
                return String.valueOf(lastResponseNumeric * recordDTO.getAmount());
            case DIVIDE:
                return String.valueOf(lastResponseNumeric / recordDTO.getAmount());
            case SQROOT:
                return String.valueOf(Math.sqrt(lastResponseNumeric));
            case RANDOM_STRING:
                return randomStringService.getRandomDouble();
            default:
                return lastOperationResponse;
        }
    }

    @Async //Run in background in prod, but in dev runs synchronously
    public void updateCachesAndBalances(RecordDTO recordDTO, Optional<UserBalanceDTO> balanceDTO) {
        try {
            log.debug("Request to update Record caches for: {}", recordDTO);
            cacheHelperService.updateLastOperationByLogin(recordDTO.getUserLogin(), recordMapper.toEntity(recordDTO));
            if (balanceDTO.isPresent()) {
                balanceDTO.get().setBalance(recordDTO.getUserBalance());
                userBalanceService.save(balanceDTO.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public RecordDTO update(RecordDTO recordDTO) {
        log.debug("Request to update Record : {}", recordDTO);
        Record record = recordMapper.toEntity(recordDTO);
        record = recordRepository.save(record);
        return recordMapper.toDto(record);
    }

    @Override
    public Optional<RecordDTO> partialUpdate(RecordDTO recordDTO) {
        log.debug("Request to partially update Record : {}", recordDTO);

        return recordRepository
            .findById(recordDTO.getId())
            .map(existingRecord -> {
                recordMapper.partialUpdate(existingRecord, recordDTO);

                return existingRecord;
            })
            .map(recordRepository::save)
            .map(recordMapper::toDto);
    }

    @Override
    public Page<RecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Records");
        return recordRepository.findAllByActiveIsTrueOrderByDateDesc(pageable).map(recordMapper::toDto);
    }

    @Override
    public Optional<RecordDTO> findOne(String id) {
        log.debug("Request to get Record : {}", id);
        return recordRepository.findById(id).map(recordMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Record : {}", id);
        Optional<RecordDTO> toDelete = findOne(id);
        if (toDelete.isPresent()) {
            RecordDTO toDeleteDTO = toDelete.get();
            toDeleteDTO.setActive(false);
            update(toDeleteDTO);
        }
    }
}
