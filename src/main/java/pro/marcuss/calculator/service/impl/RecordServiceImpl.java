package pro.marcuss.calculator.service.impl;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pro.marcuss.calculator.domain.Record;
import pro.marcuss.calculator.repository.RecordRepository;
import pro.marcuss.calculator.security.SecurityUtils;
import pro.marcuss.calculator.service.OperationService;
import pro.marcuss.calculator.service.RecordService;
import pro.marcuss.calculator.service.UserBalanceService;
import pro.marcuss.calculator.service.UserService;
import pro.marcuss.calculator.service.dto.OperationDTO;
import pro.marcuss.calculator.service.dto.RecordDTO;
import pro.marcuss.calculator.service.dto.UserBalanceDTO;
import pro.marcuss.calculator.service.dto.UserDTO;
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

    private final OperationService operationService;

    private final UserBalanceService userBalanceService;

    private final CacheManager cacheManager;

    public RecordServiceImpl(RecordRepository recordRepository,
                             RecordMapper recordMapper,
                             UserService userService,
                             OperationService operationService,
                             UserBalanceService userBalanceService,
                             CacheManager cacheManager) {
        this.recordRepository = recordRepository;
        this.recordMapper = recordMapper;
        this.userService = userService;
        this.operationService = operationService;
        this.userBalanceService = userBalanceService;
        this.cacheManager = cacheManager;
    }

    @Override
    public RecordDTO save(RecordDTO recordDTO) {
        log.debug("Request to save Record : {}", recordDTO);
        final String userLogin = SecurityUtils.getCurrentUserLogin().get(); //only logged-in users get this far
        recordDTO.setActive(true);
        recordDTO.setDate(Instant.now());
        if (recordDTO.getUser() == null) {
            recordDTO.setUser(
                userService.getUserWithAuthoritiesByLogin(userLogin).map(UserDTO::new).get()
            );
        }
        Optional<OperationDTO> operationDTO = operationService.findOneByOperator(recordDTO.getOperation());
        if (operationDTO.isEmpty()) {
            throw new RuntimeException("Operation cost not found");
        }

        Optional<UserBalanceDTO> balanceDTO = Optional.empty();

        if (recordDTO.getUser() != null) {
            //get balance from a cacheable method
            balanceDTO = userBalanceService.findUserBalanceByUserLogin(recordDTO.getUser().getLogin());
        } else {
            recordDTO.setUser(userService.getUserWithAuthoritiesByLogin(userLogin).map(UserDTO::new).get());
            //get balance from a cacheable method
            balanceDTO = userBalanceService.findUserBalanceByUserLogin(recordDTO.getUser().getLogin());
        }

        if (balanceDTO.isEmpty()) {
            throw new RuntimeException("Missing balance registry for User: " + userLogin);
        }
        recordDTO.setUserBalance(balanceDTO.get().getBalance() - operationDTO.get().getCost());

        String lastOperationResponse = null;
        try {
            lastOperationResponse = (String) cacheManager.getCache(
                RecordRepository.LAST_OPERATION_RESPONSE_BY_USER).get(recordDTO.getUser().getLogin()
            ).get();
        } catch (NullPointerException e) { //TODO:missing add the current form db
        }
        recordDTO.setOperationResponse(applyOperation(lastOperationResponse, recordDTO));
        postSaveRecordWork(recordDTO, balanceDTO);
        return recordMapper.toDto(
            recordRepository.save(recordMapper.toEntity(recordDTO))
        );
    }

    private String applyOperation(String lastOperationResponse, RecordDTO recordDTO) {
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
                return "some random string";
            default:
                return lastOperationResponse;
        }
    }

    @Async
    public void postSaveRecordWork(RecordDTO recordDTO, Optional<UserBalanceDTO> balanceDTO) {
        try {
            log.debug("Request to update Record caches for: {}", recordDTO);
            cacheManager.getCache(
                RecordRepository.LAST_OPERATION_RESPONSE_BY_USER
            ).put(recordDTO.getUser().getLogin(), recordDTO.getOperationResponse());
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
