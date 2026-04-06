package com.traffgun.acc.service;

import com.traffgun.acc.entity.EmployeeCondition;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.repository.ConditionRepository;
import com.traffgun.acc.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConditionService {
    private final ConditionRepository conditionRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public String findByEmployeeId(Long employeeId) {
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(employeeId));

        return conditionRepository.findByEmployeeId(employeeId)
                .map(EmployeeCondition::getText)
                .orElse("");
    }

    @Transactional
    public EmployeeCondition add(Long employeeId, String text) {
        employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException(employeeId));

        return conditionRepository.findByEmployeeId(employeeId).map(condition -> {
                    condition.setText(text);
                    return condition;
                })
                .orElseGet(() -> conditionRepository.save(
                        EmployeeCondition.builder()
                                .employeeId(employeeId)
                                .text(text)
                                .build()
                ));
    }
}
