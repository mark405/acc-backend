package com.traffgun.acc.service;

import com.traffgun.acc.entity.TelegramUser;
import com.traffgun.acc.repository.EmployeeRepository;
import com.traffgun.acc.repository.TelegramUserRepository;
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

    @Transactional
    public void save(TelegramUser tgUser) {
        repository.save(tgUser);
    }

    @Transactional(readOnly = true)
    public List<TelegramUser> findByUserIds(Set<Long> users) {
        return repository.findByUserIdIn(users);
    }
}
