package pro.marcuss.calculator.service.impl;

import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pro.marcuss.calculator.domain.Operation;
import pro.marcuss.calculator.domain.User;
import pro.marcuss.calculator.domain.enumeration.Operator;
import pro.marcuss.calculator.repository.OperationRepository;
import pro.marcuss.calculator.repository.UserRepository;
import pro.marcuss.calculator.service.OperationService;
import pro.marcuss.calculator.service.dto.OperationDTO;
import pro.marcuss.calculator.service.mapper.OperationMapper;

/**
 * Service Implementation for managing {@link Operation}.
 */
@Service
public class OperationServiceImpl implements OperationService {

    private final Logger log = LoggerFactory.getLogger(OperationServiceImpl.class);

    private final OperationRepository operationRepository;

    private final OperationMapper operationMapper;

    private final CacheManager cacheManager;

    public OperationServiceImpl(OperationRepository operationRepository,
                                OperationMapper operationMapper,
                                CacheManager cacheManager) {
        this.operationRepository = operationRepository;
        this.operationMapper = operationMapper;
        this.cacheManager = cacheManager;
    }

    @Override
    public OperationDTO save(OperationDTO operationDTO) {
        log.debug("Request to save Operation : {}", operationDTO);
        Operation operation = operationMapper.toEntity(operationDTO);
        operation = operationRepository.save(operation);
        clearUserCaches(operation.getOperator());
        return operationMapper.toDto(operation);
    }

    @Override
    public OperationDTO update(OperationDTO operationDTO) {
        log.debug("Request to update Operation : {}", operationDTO);
        Operation operation = operationMapper.toEntity(operationDTO);
        operation = operationRepository.save(operation);
        clearUserCaches(operation.getOperator());
        return operationMapper.toDto(operation);
    }

    @Override
    public Optional<OperationDTO> partialUpdate(OperationDTO operationDTO) {
        log.debug("Request to partially update Operation : {}", operationDTO);

        Optional<OperationDTO> updated = operationRepository
            .findById(operationDTO.getId())
            .map(existingOperation -> {
                operationMapper.partialUpdate(existingOperation, operationDTO);

                return existingOperation;
            })
            .map(operationRepository::save)
            .map(operationMapper::toDto);
        clearUserCaches(updated.get().getOperator());
        return updated;
    }

    @Override
    public Page<OperationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Operations");
        return operationRepository.findAll(pageable).map(operationMapper::toDto);
    }

    @Override
    public Optional<OperationDTO> findOne(String id) {
        log.debug("Request to get Operation : {}", id);
        return operationRepository.findById(id).map(operationMapper::toDto);
    }

    @Override
    public Optional<OperationDTO> findOneByOperator(Operator operator){
        log.debug("Request to get Operation by operator: {}", operator);
        return operationRepository.findOneByOperator(operator).map(operationMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Operation : {}", id);
        Operator toDelete = operationRepository.findById(id).get().getOperator();
        operationRepository.deleteById(id);
        clearUserCaches(toDelete);
    }

    private void clearUserCaches(Operator operator) {
        Objects.requireNonNull(cacheManager.getCache(OperationRepository.COST_BY_OPERATOR_CACHE)).evict(operator);
    }
}
