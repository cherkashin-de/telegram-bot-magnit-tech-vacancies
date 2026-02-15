package com.cherkashin.telegramBot.service.bot;

import com.cherkashin.telegramBot.config.bot.VacancyBot;
import com.cherkashin.telegramBot.model.entity.User;
import com.cherkashin.telegramBot.service.UserService;
import com.cherkashin.telegramBot.service.command.CommandRule;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static com.cherkashin.telegramBot.constant.Supportive.Commands.CallbackQuery.ITEM_TECHNOLOGIES;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CallbackQueryService {

    ApplicationContext context;
    UserService userService;

    protected void handler(Update update, VacancyBot bot) throws TelegramApiException {
        if (!update.hasCallbackQuery())
            return;

        CallbackQuery callbackQuery = update.getCallbackQuery();
        Optional<User> userOptional = userService.getUserByLogin(callbackQuery.getMessage().getChat().getUserName());
        if (userOptional.isEmpty())
            return;

        User user = userOptional.get();
        String command = modifyCommand(callbackQuery.getData());

        CommandRule rule = (CommandRule) context.getBean(command);
        SendMessage sendMessage = rule.execute(update, bot, user);

        if (sendMessage != null) {
            userService.save(user);
            bot.sendMessage(user, sendMessage, update.getCallbackQuery().getData());
        }
    }

    private String modifyCommand(String command) {
        if (command.contains(ITEM_TECHNOLOGIES))
            command = ITEM_TECHNOLOGIES;

        return command;
    }

}
