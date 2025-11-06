package com.traffgun.acc.service;

import com.traffgun.acc.dto.operation.CreateOperationRequest;
import com.traffgun.acc.dto.operation.OperationFilter;
import com.traffgun.acc.dto.operation.UpdateOperationRequest;
import com.traffgun.acc.entity.Board;
import com.traffgun.acc.entity.Category;
import com.traffgun.acc.entity.Operation;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.repository.OperationRepository;
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
    private final CategoryService categoryService;
    private final BoardService boardService;

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
    public void deleteById(Long id) {
        operationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Operation> findById(Long id) {
        return operationRepository.findById(id);
    }

    @Transactional
    public Operation create(CreateOperationRequest request) {
        return operationRepository.save(Operation.builder()
                .amount(request.getAmount())
                .category(categoryService.findById(request.getCategoryId()).orElseThrow(() -> new EntityNotFoundException(request.getCategoryId())))
                .board(boardService.findById(request.getBoardId()).orElseThrow(() -> new EntityNotFoundException(request.getBoardId())))
                .comment(request.getComment())
                .operationType(request.getOperationType())
                .build());
    }

    @Transactional
    public Operation update(Operation operation, UpdateOperationRequest request) {
        operation.setComment(request.getComment());
        operation.setAmount(request.getAmount());
        operation.setOperationType(request.getOperationType());

        Category category = categoryService.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException(request.getCategoryId()));

        Board board = boardService.findById(request.getBoardId())
                .orElseThrow(() -> new EntityNotFoundException(request.getBoardId()));

        operation.setCategory(category);
        operation.setBoard(board);
        operation.setDate(request.getDate());

        return operationRepository.save(operation);
    }
}
