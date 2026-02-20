package com.traffgun.acc.service;

import com.traffgun.acc.entity.TelegramUser;
import com.traffgun.acc.entity.User;
import com.traffgun.acc.model.Role;
import com.traffgun.acc.repository.TelegramUserRepository;
import com.traffgun.acc.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TelegramUserService {
    private final TelegramUserRepository repository;
    private final UserRepository userRepository;

    @Transactional
    public void registerManager(Long chatId, String login) {
        User found = userRepository.findByUsername(login)
                .orElseThrow(() -> new IllegalArgumentException("Manager login not found"));

        repository.findByChatId(chatId).ifPresent(repository::delete);

        TelegramUser user = TelegramUser.builder()
                .chatId(chatId)
                .role(Role.MANAGER)
                .managerId(found.getId())
                .build();
        repository.save(user);
    }

    @Transactional
    public void registerTechManager(Long chatId) {
        repository.findByChatId(chatId).ifPresent(repository::delete);

        TelegramUser user = TelegramUser.builder()
                .chatId(chatId)
                .role(Role.TECH_MANAGER)
                .build();
        repository.save(user);
    }

    @Transactional(readOnly = true)
    public List<TelegramUser> findAllByRole(Role role) {
        return repository.findAllByRole(role);
    }

    @Transactional(readOnly = true)
    public List<TelegramUser> findByRoleAndManagerId(Role role, Long userId) {
        return repository.findAllByRoleAndManagerId(role, userId);
    }

    @Transactional(readOnly = true)
    public List<TelegramUser> findByRoleAndManagerIdIn(Role role, Set<Long> userIds) {
        return repository.findAllByRoleAndManagerIdIn(role, userIds);
    }

    public void registerOffersManager(long chatId, @NotBlank String login) {
        User found = userRepository.findByUsername(login)
                .orElseThrow(() -> new IllegalArgumentException("Manager login not found"));

        repository.findByChatId(chatId).ifPresent(repository::delete);

        TelegramUser user = TelegramUser.builder()
                .chatId(chatId)
                .role(Role.OFFERS_MANAGER)
                .managerId(found.getId())
                .build();
        repository.save(user);
    }
}
