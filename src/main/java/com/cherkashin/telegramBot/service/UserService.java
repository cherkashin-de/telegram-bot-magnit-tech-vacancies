package com.cherkashin.telegramBot.service;

import com.cherkashin.telegramBot.model.Supportive;
import com.cherkashin.telegramBot.model.entity.User;
import com.cherkashin.telegramBot.model.entity.UserMessage;
import com.cherkashin.telegramBot.repository.UserMessageRepository;
import com.cherkashin.telegramBot.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    UserMessageRepository userMessageRepository;

    public void saveUserMessage(User user, SendMessage sendMessage, String messageGet) {
        userMessageRepository.save(UserMessage.builder()
                .chatId(Long.valueOf(user.getChatId()))
                .messageSend(sendMessage.getText())
                .user(user)
                .messageGet(messageGet)
                .build());
    }

    public User saveByMessage(Message message) {
        return userRepository.save(User.builder()
                .login(message.getChat().getUserName())
                .firstName(message.getChat().getFirstName())
                .lastName(message.getChat().getLastName())
                .chatId(message.getChatId())
                .isNotificationOn(false)
                .data(Supportive.DTO.Notification.Data.builder()
                        .pageVacancies(0)
                        .pageTechnologies(0)
                        .notifications(List.of())
                        .build())
                .build());
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public void turnOnNotification(User user) {
        user.setIsNotificationOn(true);
        save(user);
    }

    public void turnOffNotification(User user) {
        user.setIsNotificationOn(false);
        save(user);
    }

}
