package com.traffgun.acc.service;

import com.traffgun.acc.entity.History;
import com.traffgun.acc.model.history.HistoryType;
import com.traffgun.acc.repository.HistoryRepository;
import com.traffgun.acc.specification.HistorySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;

    @Transactional
    public History create(History history) {
        return historyRepository.save(history);
    }

    @Transactional(readOnly = true)
    public Page<History> findAll(Long projectId, String name, HistoryType type, String sortBy, String direction, int page, int size) {
        Sort sort = Sort.by(sortBy);
        if ("desc".equalsIgnoreCase(direction)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<History> spec = (root, query, cb) -> cb.conjunction();

        spec = spec
                .and(HistorySpecification.hasProjectId(projectId))
                .and(HistorySpecification.hasType(type))
                .and(HistorySpecification.hasName(name));

        return historyRepository.findAll(spec, pageable);
    }
}
