package com.cherkashin.telegramBot.service;

import com.cherkashin.telegramBot.config.bot.VacancyBot;
import com.cherkashin.telegramBot.model.Supportive;
import com.cherkashin.telegramBot.model.entity.Notification;
import com.cherkashin.telegramBot.model.entity.User;
import com.cherkashin.telegramBot.repository.NotificationRepository;
import com.cherkashin.telegramBot.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static com.cherkashin.telegramBot.constant.Supportive.Notification.ADD_NEW_VACANCY;
import static com.cherkashin.telegramBot.constant.Supportive.Notification.CLOSE_VACANCY;
import static com.cherkashin.telegramBot.model.Supportive.DTO.VacancyInformation.getDescription;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {

    VacancyBot bot;
    Environment env;
    UserRepository userRepository;
    NotificationRepository notificationRepository;

    public void addNotification(String type, Supportive.DTO.VacancyInformation data) {
        notificationRepository.save(Notification.builder()
                .typeEvent(type)
                .data(data)
                .status(0)
                .build());
    }

    public void notifyUsers() {
        List<Notification> notifications = notificationRepository.findByStatus(0);
        if (notifications.isEmpty())
            return;

        List<User> users = userRepository.findByIsNotificationOnTrue();
        if (users.isEmpty())
            return;

        String url = env.getProperty("app.magnit_tech_vacancy_url");
        StringBuilder sBuilder = new StringBuilder();

        notifications.forEach(notification -> {
            sBuilder.append(getTextByEventType(notification, url) + "\n");
            notification.setStatus(1);
        });
        notificationRepository.saveAll(notifications);

        if (sBuilder.isEmpty())
            return;

        users.forEach(user -> {
            if (user.getIsNotificationOn())
                try {
                    bot.sendMessage(user,
                            SendMessage.builder()
                                    .chatId(user.getChatId())
                                    .text(sBuilder.toString())
                                    .parseMode("HTML")
                                    .build(),
                            null);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
        });
    }

    private String getTextByEventType(Notification notification, String url) {
        String text = null;
        if (notification.getTypeEvent().equals(ADD_NEW_VACANCY))
            text = "Появилась новая вакансия \n%s"
                    .formatted(getDescription(notification.getData(), url, null));

        if (notification.getTypeEvent().equals(CLOSE_VACANCY))
            text = "Пропала вакансия \n%s"
                    .formatted(getDescription(notification.getData(), url, null));

        return text;
    }

}
