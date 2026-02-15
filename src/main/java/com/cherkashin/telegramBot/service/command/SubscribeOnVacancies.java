package com.cherkashin.telegramBot.service.command;

import com.cherkashin.telegramBot.config.bot.VacancyBot;
import com.cherkashin.telegramBot.model.entity.User;
import com.cherkashin.telegramBot.service.ReplyKeyboardService;
import com.cherkashin.telegramBot.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.cherkashin.telegramBot.constant.Supportive.Commands.SUBSCRIBE_ON_VACANCIES;

@Component(SUBSCRIBE_ON_VACANCIES)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscribeOnVacancies implements CommandRule {

    UserService userService;
    ReplyKeyboardService replyKeyboardService;

    @Override
    public SendMessage execute(Update update, VacancyBot bot, User user) {
        userService.turnOnNotification(user);
        return SendMessage.builder()
                .chatId(user.getChatId())
                .replyMarkup(replyKeyboardService.getReplyKeyboardForNotifications(user))
                .text("Вы подписались на новые вакансии, перейдите в настройки, чтобы выбрать конкретный стек")
                .build();
    }

}
