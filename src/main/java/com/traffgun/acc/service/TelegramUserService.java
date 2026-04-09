package com.traffgun.acc.service;

import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.TelegramUser;
import com.traffgun.acc.model.EmployeeRole;
import com.traffgun.acc.repository.EmployeeRepository;
import com.traffgun.acc.repository.TelegramUserRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TelegramUserService {
    private final TelegramUserRepository repository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public void registerManager(Long chatId, String name) {
        Employee found = employeeRepository.findByNameAndActiveIsTrue(name)
                .orElseThrow(() -> new IllegalArgumentException("Manager name not found"));

        repository.findByChatId(chatId).ifPresent(repository::delete);

        TelegramUser user = TelegramUser.builder()
                .chatId(chatId)
                .role(EmployeeRole.MANAGER)
                .managerId(found.getId())
                .build();
        repository.save(user);
    }

    @Transactional
    public void registerTechManager(Long chatId) {
        repository.findByChatId(chatId).ifPresent(repository::delete);

        TelegramUser user = TelegramUser.builder()
                .chatId(chatId)
                .role(EmployeeRole.TECH_MANAGER)
                .build();
        repository.save(user);
    }

    @Transactional(readOnly = true)
    public List<TelegramUser> findAllByRole(EmployeeRole role) {
        return repository.findAllByRole(role);
    }

    @Transactional(readOnly = true)
    public List<TelegramUser> findByRoleAndManagerId(EmployeeRole role, Long userId) {
        return repository.findAllByRoleAndManagerId(role, userId);
    }

    @Transactional(readOnly = true)
    public List<TelegramUser> findByRoleAndManagerIdIn(EmployeeRole role, Set<Long> userIds) {
        return repository.findAllByRoleAndManagerIdIn(role, userIds);
    }

    @Transactional
    public void registerOffersManager(long chatId, @NotBlank String name) {
        Employee found = employeeRepository.findByNameAndActiveIsTrue(name)
                .orElseThrow(() -> new IllegalArgumentException("Manager name not found"));

        repository.findByChatId(chatId).ifPresent(repository::delete);

        TelegramUser user = TelegramUser.builder()
                .chatId(chatId)
                .role(EmployeeRole.OFFERS_MANAGER)
                .managerId(found.getId())
                .build();
        repository.save(user);
    }

    @Transactional(readOnly = true)
    public List<TelegramUser> findAllByManagers(List<Employee> list) {
        return repository.findAllByManagerIdIn(list.stream().map(Employee::getId).toList());
    }

    @Transactional
    public void registerTaskUser(long chatId, @NotBlank String name) {
        Employee found = employeeRepository.findByNameAndActiveIsTrue(name)
                .orElseThrow(() -> new IllegalArgumentException("Manager name not found"));

        repository.findByChatId(chatId).ifPresent(repository::delete);

        TelegramUser user = TelegramUser.builder()
                .chatId(chatId)
                .role(EmployeeRole.MANAGER)
                .managerId(found.getId())
                .build();
        repository.save(user);
    }
}
