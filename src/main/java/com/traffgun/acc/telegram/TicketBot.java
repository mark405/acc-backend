package com.traffgun.acc.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.traffgun.acc.entity.Ticket;
import com.traffgun.acc.entity.TicketComment;
import com.traffgun.acc.entity.User;
import com.traffgun.acc.model.Role;
import com.traffgun.acc.model.TicketStatus;
import com.traffgun.acc.repository.UserRepository;
import com.traffgun.acc.service.TelegramUserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TicketBot {

    private final TelegramBot bot;
    private final TelegramUserService telegramUserService;
    private final UserRepository userRepository;

    // Tracks users who are expected to send manager login
    private final Map<Long, Role> waitingForLogin = new ConcurrentHashMap<>();

    public TicketBot(TelegramUserService telegramUserService,
                     UserRepository userRepository,
                     @Value("${telegram.bot.token}") String botToken) {
        this.telegramUserService = telegramUserService;
        this.userRepository = userRepository;
        this.bot = new TelegramBot(botToken);
    }

    @PostConstruct
    public void initBot() {
        bot.setUpdatesListener(updates -> {
            try {
                return handleUpdates(updates);
            } catch (Exception e) {
                log.error("Error handling updates: {}", e.getMessage());
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        });
        log.info("Telegram bot started");
    }

    private int handleUpdates(List<Update> updates) {
        for (Update update : updates) {
            try {

                if (update.message() == null || update.message().text() == null) continue;
                if (update.message().from() != null && update.message().from().isBot()) continue;

                long chatId = update.message().chat().id();
                String text = update.message().text();

                Role pendingRole = waitingForLogin.get(chatId);

                if (pendingRole != null) {
                    userRepository.findByUsername(text).ifPresentOrElse(
                            manager -> {
                                if (pendingRole == Role.MANAGER) {
                                    telegramUserService.registerManager(chatId, manager.getUsername());
                                } else if (pendingRole == Role.OFFERS_MANAGER) {
                                    telegramUserService.registerOffersManager(chatId, manager.getUsername());
                                }

                                sendMessage(chatId,
                                        "You are now registered as " + pendingRole +
                                                " for login: " + manager.getUsername());
                            },
                            () -> sendMessage(chatId, "Login not found, try again.")
                    );

                    waitingForLogin.remove(chatId);
                    continue;
                }

                // Step 2: Command handling
                if (text.equalsIgnoreCase("/tech_manager")) {
                    telegramUserService.registerTechManager(chatId);
                    sendMessage(chatId, "You will receive TECH_GOAL tickets updates.");
                } else if (text.equalsIgnoreCase("/offers_manager")) {
                    waitingForLogin.put(chatId, Role.OFFERS_MANAGER);
                    sendMessage(chatId, "Please enter your manager login:");
                }else if (text.equalsIgnoreCase("/manager")) {
                    waitingForLogin.put(chatId, Role.MANAGER);
                    sendMessage(chatId, "Please enter your manager login:");
                } else {
                    sendMessage(chatId, "Send /tech_manager or /manager or /offers_manager to choose your notifications.");
                }
            } catch (Exception e) {
                log.error("Error handling update: {}", e.getMessage());
            }
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void notifyNewTechTicket(Ticket ticket) {
        String creator = ticket.getCreatedBy().getUsername();
        telegramUserService.findAllByRole(Role.TECH_MANAGER).forEach(user -> {
            String message = "🆕 Новий Тікет #" + ticket.getId() + "\n" +
                    "Створив: " + escapeMarkdown(creator) + "\n\n" +
                    "Опис:\n" + escapeMarkdown(ticket.getText());
            sendMessage(user.getChatId(), message);
        });
    }

    public void notifyNewOffersTicket(Ticket ticket) {
        String creator = ticket.getCreatedBy().getUsername();
        telegramUserService.findByRoleAndManagerIdIn(
                Role.OFFERS_MANAGER,
                ticket.getAssignedTo().stream().map(User::getId).collect(Collectors.toUnmodifiableSet()))
                .forEach(user -> {
            String message = "🆕 Новий Тікет #" + ticket.getId() + "\n" +
                    "Створив: " + escapeMarkdown(creator) + "\n\n" +
                    "Опис:\n" + escapeMarkdown(ticket.getText());
            sendMessage(user.getChatId(), message);
        });
    }

    // Notify ticket creator about a new comment
    public void notifyNewComment(Long ticketId, Long userId, TicketComment comment) {
        String creator = comment.getCreatedBy().getUsername();
        telegramUserService.findByRoleAndManagerId(Role.MANAGER, userId).forEach(user -> {
            String message = "💬 Новий коментар до Тікету #" + ticketId + "\n" +
                    "Створив: " + escapeMarkdown(creator) + "\n\n" +
                    "Коментар:\n" + escapeMarkdown(comment.getText());
            sendMessage(user.getChatId(), message);
        });
    }

    public void notifyNewStatus(Ticket ticket, String username) {
        telegramUserService.findByRoleAndManagerId(Role.MANAGER, ticket.getCreatedBy().getId()).forEach(user -> {
            String message = username + " змінив статус Тікету #" + ticket.getId() + " на " + ticket.getStatus() + "\n";
            sendMessage(user.getChatId(), message);
        });
    }


    private String escapeMarkdown(String text) {
        if (text == null) return "";
        return text.replaceAll("([_\\*\\[\\]\\(\\)~`>#+\\-=|{}.!])", "\\\\$1");
    }

    private void sendMessage(long chatId, String text) {
        try {
            bot.execute(new SendMessage(chatId, text));
        } catch (Exception e) {
            log.error("Error sending message", e);
        }
    }
}