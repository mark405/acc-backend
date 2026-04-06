package com.traffgun.acc.repository;

import com.traffgun.acc.entity.TelegramUser;
import com.traffgun.acc.model.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    List<TelegramUser> findAllByRole(EmployeeRole role);

    List<TelegramUser> findAllByRoleAndManagerId(EmployeeRole role, Long managerId);

    Optional<TelegramUser> findByChatId(Long chatId);

    List<TelegramUser> findAllByRoleAndManagerIdIn(EmployeeRole role, Collection<Long> managerIds);
}
