package com.traffgun.acc.repository;

import com.traffgun.acc.entity.TelegramUser;
import com.traffgun.acc.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    List<TelegramUser> findAllByRole(Role role);

    List<TelegramUser> findAllByRoleAndManagerId(Role role, Long managerId);

    Optional<TelegramUser> findByChatId(Long chatId);
}
