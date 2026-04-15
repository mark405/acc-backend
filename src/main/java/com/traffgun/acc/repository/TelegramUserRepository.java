package com.traffgun.acc.repository;

import com.traffgun.acc.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    List<TelegramUser> findByUserIdIn(Collection<Long> userIds);
}
