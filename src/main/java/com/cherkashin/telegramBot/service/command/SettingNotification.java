package com.cherkashin.telegramBot.service.command;

import com.cherkashin.telegramBot.config.bot.VacancyBot;
import com.cherkashin.telegramBot.model.entity.User;
import com.cherkashin.telegramBot.service.ReplyKeyboardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.cherkashin.telegramBot.constant.Supportive.Commands.SETTING_NOTIFICATION;

@Component(SETTING_NOTIFICATION)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SettingNotification implements CommandRule {

    ReplyKeyboardService replyKeyboardService;

    @Override
    public SendMessage execute(Update update, VacancyBot bot, User user) {
        return SendMessage.builder()
                .chatId(user.getChatId())
                .replyMarkup(replyKeyboardService.getReplyKeyboardForNotifications(user))
                .text("Выберите технологии, на которые хотите подписаться")
                .build();
    }

}
