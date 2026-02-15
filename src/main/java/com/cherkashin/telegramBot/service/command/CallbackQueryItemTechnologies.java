package com.cherkashin.telegramBot.service.command;

import com.cherkashin.telegramBot.config.bot.VacancyBot;
import com.cherkashin.telegramBot.model.entity.User;
import com.cherkashin.telegramBot.service.ReplyKeyboardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.cherkashin.telegramBot.constant.Supportive.Commands.CallbackQuery.ITEM_TECHNOLOGIES;

@Component(ITEM_TECHNOLOGIES)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CallbackQueryItemTechnologies implements CommandRule {

    ReplyKeyboardService replyKeyboardService;

    @Override
    public SendMessage execute(Update update, VacancyBot bot, User user) {
        String command = update.getCallbackQuery().getData();
        String technology = StringUtils.right(command,
                command.length() - ITEM_TECHNOLOGIES.length());

        List<String> notifications = user.getData().getNotifications();
        SendMessage sendMessage = null;

        //Небольшой костыль, простите
        if (technology.contains("(уже подписаны)")) {
            technology = StringUtils.left(technology, technology.length() - "(уже подписаны)".length()).strip();
            notifications.remove(technology);

            sendMessage = SendMessage.builder()
                    .chatId(user.getChatId())
                    .text("Вы успешно отписались от %s".formatted(technology))
                    .replyMarkup(replyKeyboardService.getReplyKeyboardForNotifications(user))
                    .build();
        } else {
            if (!notifications.contains(technology)) {
                notifications.add(technology);

                sendMessage = SendMessage.builder()
                        .chatId(user.getChatId())
                        .text("Вы успешно подписались на %s".formatted(technology))
                        .replyMarkup(replyKeyboardService.getReplyKeyboardForNotifications(user))
                        .build();
            }
        }
        return sendMessage;
    }
}
