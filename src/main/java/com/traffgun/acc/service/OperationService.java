package com.traffgun.acc.service;

import com.traffgun.acc.dto.operation.CreateOperationRequest;
import com.traffgun.acc.dto.operation.OperationFilter;
import com.traffgun.acc.dto.operation.UpdateOperationRequest;
import com.traffgun.acc.entity.*;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.model.history.HistoryType;
import com.traffgun.acc.model.history.OperationCreatedHistoryBody;
import com.traffgun.acc.model.history.OperationDeletedHistoryBody;
import com.traffgun.acc.model.history.OperationUpdatedHistoryBody;
import com.traffgun.acc.repository.*;
import com.traffgun.acc.specification.OperationSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class OperationService {

    private final OperationRepository operationRepository;
    private final HistoryRepository historyRepository;
    private final CategoryRepository categoryRepository;
    private final BoardRepository boardRepository;
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public Page<Operation> findAll(OperationFilter filter) {
        Specification<Operation> spec = (root, query, cb) -> cb.conjunction();

        spec = spec
                .and(OperationSpecification.hasBoardId(filter.getBoardId()))
                .and(OperationSpecification.hasType(filter.getType()))
                .and(OperationSpecification.hasCategoryIds(filter.getCategoryIds()))
                .and(OperationSpecification.hasCommentLike(filter.getComment()))
                .and(OperationSpecification.hasDateAfter(filter.getStartDate()))
                .and(OperationSpecification.hasDateBefore(filter.getEndDate()));

        Sort sort = Sort.by(Sort.Direction.fromString(filter.getDirection()), filter.getSortBy());
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);

        return operationRepository.findAll(spec, pageable);
    }

    @Transactional
    public void deleteById(Long id) throws IllegalAccessException {
        Operation operation = operationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        operationRepository.deleteById(operation.getId());

        historyRepository.save(History.builder()
                .employee(employeeRepository.findByUserAndProjectAndActiveIsTrue(userService.getCurrentUser(), operation.getProject()).orElseThrow())
                .type(HistoryType.OPERATION)
                .body(new OperationDeletedHistoryBody(operation.getBoard().getName(), operation.getCategory().getName(), operation.getOperationType().name()))
                .project(operation.getProject())
                .build()
        );
    }

    @Transactional(readOnly = true)
    public Optional<Operation> findById(Long id) {
        return operationRepository.findById(id);
    }

    @Transactional
    public Operation create(CreateOperationRequest request) throws IllegalAccessException {
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new EntityNotFoundException(request.getCategoryId()));
        Board board = boardRepository.findById(request.getBoardId()).orElseThrow(() -> new EntityNotFoundException(request.getBoardId()));
        Project project = projectRepository.findById(request.getProjectId()).orElseThrow(() -> new EntityNotFoundException(request.getProjectId()));
        Operation saved = operationRepository.save(Operation.builder()
                .amount(request.getAmount())
                .category(category)
                .board(board)
                .comment(request.getComment())
                .operationType(request.getOperationType())
                .date(request.getDate())
                .project(project)
                .build());

        historyRepository.save(History.builder()
                .employee(employeeRepository.findByUserAndProjectAndActiveIsTrue(userService.getCurrentUser(), saved.getProject()).orElseThrow())
                .type(HistoryType.OPERATION)
                .body(new OperationCreatedHistoryBody(board.getName(), category.getName(), request.getOperationType().name()))
                .project(project)
                .build()
        );

        return saved;
    }

    @Transactional
    public Operation update(Operation operation, UpdateOperationRequest request) throws IllegalAccessException {
        operation.setComment(request.getComment());
        operation.setAmount(request.getAmount());
        operation.setOperationType(request.getOperationType());

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException(request.getCategoryId()));

        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new EntityNotFoundException(request.getBoardId()));

        operation.setCategory(category);
        operation.setBoard(board);
        operation.setDate(request.getDate());

        Operation saved = operationRepository.save(operation);

        historyRepository.save(History.builder()
                .employee(employeeRepository.findByUserAndProjectAndActiveIsTrue(userService.getCurrentUser(), operation.getProject()).orElseThrow())
                .type(HistoryType.OPERATION)
                .body(new OperationUpdatedHistoryBody(board.getName(), category.getName(), request.getOperationType().name()))
                .project(operation.getProject())
                .build()
        );

        return saved;
    }
}
