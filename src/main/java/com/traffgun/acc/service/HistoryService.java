package com.traffgun.acc.service;

import com.traffgun.acc.entity.History;
import com.traffgun.acc.entity.Operation;
import com.traffgun.acc.entity.User;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.model.history.HistoryType;
import com.traffgun.acc.repository.HistoryRepository;
import com.traffgun.acc.repository.UserRepository;
import com.traffgun.acc.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;

    @Transactional
    public History create(History history) {
        return historyRepository.save(history);
    }

    @Transactional(readOnly = true)
    public Page<History> findAll(String username, HistoryType type, String sortBy, String direction, int page, int size) {
        Sort sort = Sort.by(sortBy);
        if ("desc".equalsIgnoreCase(direction)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        List<User> users = null;
        if (username != null && !username.isBlank()) {
            Specification<User> spec = (root, query, cb) -> cb.conjunction();

            spec = spec.and(UserSpecification.hasUsernameLike(username));
            users = userRepository.findAll(spec);
        }

        if (users != null && type != null) {
            return historyRepository.findByUserInAndType(users, type, pageable);
        } else if (users != null) {
            return historyRepository.findByUserIn(users, pageable);
        } else if (type != null) {
            return historyRepository.findByType(type, pageable);
        } else {
            return historyRepository.findAll(pageable);
        }
    }
}
