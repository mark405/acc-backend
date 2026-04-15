package com.traffgun.acc.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.traffgun.acc.entity.*;
import com.traffgun.acc.model.EmployeeRole;
import com.traffgun.acc.model.TicketStatus;
import com.traffgun.acc.model.TicketType;
import com.traffgun.acc.repository.EmployeeRepository;
import com.traffgun.acc.repository.TicketRepository;
import com.traffgun.acc.service.TelegramUserService;
import com.traffgun.acc.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TicketBot {

    private final UserService userService;
    private final TelegramBot bot;
    private final TelegramUserService telegramUserService;
    private final TicketRepository ticketRepository;

    private final Map<Long, Session> sessions = new ConcurrentHashMap<>();

    public TicketBot(UserService userService,
                     TelegramUserService telegramUserService,
                     TicketRepository ticketRepository,
                     @Value("${telegram.bot.token}") String botToken) {

        this.userService = userService;
        this.telegramUserService = telegramUserService;
        this.ticketRepository = ticketRepository;
        this.bot = new TelegramBot(botToken);
    }

    @PostConstruct
    public void initBot() {
        bot.setUpdatesListener(updates -> {
            try {
                return handleUpdates(updates);
            } catch (Exception e) {
                log.error("Error handling updates: {}", e.getMessage(), e);
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        });

        log.info("Telegram bot started");
    }

    private void handleAuthFlow(long chatId, String text) {
        Session session = sessions.get(chatId);

        if (session == null || session.step == Step.NONE) {
            sendMessage(chatId, "Type /start first.");
            return;
        }

        switch (session.step) {

            case WAIT_LOGIN -> {
                session.login = text;
                session.step = Step.WAIT_PASSWORD;

                sendMessage(chatId, "Now send password:");
            }

            case WAIT_PASSWORD -> {
                String login = session.login;

                Optional<User> user = userService.findByUsername(login);

                if (user.isEmpty()) {
                    sendMessage(chatId, "Invalid credentials. Try /start again.");
                    sessions.remove(chatId);
                    return;
                }

                if (!userService.checkPassword(text, user.get().getPassword())) {
                    sendMessage(chatId, "Invalid password. Try /start again.");
                    sessions.remove(chatId);
                    return;
                }

                TelegramUser tgUser = TelegramUser.builder()
                        .chatId(chatId)
                        .userId(user.get().getId())
                        .build();

                telegramUserService.save(tgUser);

                sendMessage(chatId, "Login successful. You are now linked.");

                sessions.remove(chatId);
            }
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage request = new SendMessage(chatId, text);

        SendResponse response = bot.execute(request);

        if (!response.isOk()) {
            log.error("Failed to send message to {}: {}", chatId, response.description());
        }
    }

    private int handleUpdates(List<Update> updates) {
        for (Update update : updates) {
            try {
                if (update.message() == null || update.message().text() == null) continue;
                if (update.message().from() != null && update.message().from().isBot()) continue;

                long chatId = update.message().chat().id();
                String text = update.message().text();

                if (text.equalsIgnoreCase("/start")) {
                    handleStart(chatId);
                    continue;
                }

                handleAuthFlow(chatId, text);

            } catch (Exception e) {
                log.error("Error handling update", e);
            }
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void handleStart(long chatId) {
        sessions.put(chatId, new Session());
        sessions.get(chatId).step = Step.WAIT_LOGIN;

        sendMessage(chatId, "Send your login:");
    }

    public void notifyNewTicket(Ticket savedTicket) {
        Set<Long> users = savedTicket.getAssignedTo().stream()
                .map(Employee::getUserId)
                .collect(Collectors.toSet());

        String message = buildNewTicketMessage(savedTicket);

        telegramUserService.findByUserIds(users).forEach(user -> {
            sendMessage(user.getChatId(), message);
        });
    }

    private String buildNewTicketMessage(Ticket t) {
        return """
            🆕 Новий тікет

            🔢 №%d
            📁 Проєкт: %s
            👤 Створив: %s
            🏷 Тип: %s
            📌 Статус: %s
            """
                .formatted(
                        t.getId(),
                        t.getProject().getName(),
                        t.getCreatedBy().getName(),
                        t.getType(),
                        t.getStatus()
                );
    }

    private String buildNewTicketCommentMessage(Ticket t, TicketComment comment) {
        return """
        💬 Новий коментар у тікеті

        🔢 №%d
        📁 Проєкт: %s
        👤 Автор коментаря: %s
        📝 Коментар:
        %s
        """
                .formatted(
                        t.getId(),
                        t.getProject().getName(),
                        comment.getCreatedBy().getName(),
                        comment.getText()
                );
    }

    @Scheduled(fixedRate = 5400000) // 1.5 hours in ms
    public void notifyAboutOpenedTasks() {
        var tickets = ticketRepository.findAllByTypeAndStatusIn(
                TicketType.TASK,
                List.of(TicketStatus.OPENED, TicketStatus.IN_PROGRESS)
        );

        Map<Long, List<Ticket>> grouped = tickets.stream()
                .filter(t -> t.getAssignedTo() != null && !t.getAssignedTo().isEmpty())
                .flatMap(ticket ->
                        ticket.getAssignedTo().stream()
                                .map(employee -> Map.entry(employee.getUserId(), ticket))
                )
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));

        Map<Long, TelegramUser> tgUsers = telegramUserService.findByUserIds(grouped.keySet()).stream().collect(Collectors.toMap(TelegramUser::getUserId, user -> user));

        grouped.forEach((userId, userTickets) -> {
            var tgUserOpt = tgUsers.get(userId);
            if (tgUserOpt == null) {
                log.warn("No telegram linked for user {}", userId);
                return;
            }

            Long chatId = tgUserOpt.getChatId();

            String message = buildMessage(userTickets);

            sendMessage(chatId, message);
        });
    }

    private String buildMessage(List<Ticket> tickets) {
        StringBuilder sb = new StringBuilder();

        sb.append("📌 Ваші активні задачі:\n\n");

        for (Ticket t : tickets) {
            sb.append("• №").append(t.getId()).append("\n")
                    .append("  Проєкт: ").append(t.getProject().getName()).append("\n")
                    .append("  Створив: ").append(t.getCreatedBy().getName()).append("\n")
                    .append("\n");
        }

        return sb.toString();
    }

    public void notifyNewComment(Ticket ticket, TicketComment saved) {
        var user = saved.getCreatedBy().getUserId();
        Set<Long> users = ticket.getAssignedTo().stream()
                .map(Employee::getUserId)
                .filter(id -> !id.equals(user))
                .collect(Collectors.toSet());

        if (!Objects.equals(user, ticket.getCreatedBy().getUserId())) {
            users.add(ticket.getCreatedBy().getUserId());
        }

        String message = buildNewTicketCommentMessage(ticket, saved);

        telegramUserService.findByUserIds(users).forEach(us -> {
            sendMessage(us.getChatId(), message);
        });
    }

    public void notifyNewStatus(Ticket saved) {
        Set<Long> users = Set.of(saved.getCreatedBy().getUserId());
        String message = buildTicketStatusChangedMessage(saved);

        telegramUserService.findByUserIds(users).forEach(user -> {
            sendMessage(user.getChatId(), message);
        });
    }

    private String buildTicketStatusChangedMessage(Ticket t) {
        return """
        🔄 Статус тікета змінено

        🔢 №%d
        📁 Проєкт: %s
        ✅ Стало: %s
        """
                .formatted(
                        t.getId(),
                        t.getProject().getName(),
                        t.getStatus()
                );
    }

    private enum Step {
        NONE,
        WAIT_LOGIN,
        WAIT_PASSWORD
    }

    private static class Session {
        Step step = Step.NONE;
        String login;
    }
}