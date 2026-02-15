package com.cherkashin.telegramBot.service.bot;

import com.cherkashin.telegramBot.config.bot.VacancyBot;
import com.cherkashin.telegramBot.model.entity.User;
import com.cherkashin.telegramBot.service.ReplyKeyboardService;
import com.cherkashin.telegramBot.service.UserService;
import com.cherkashin.telegramBot.service.command.CommandRule;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageHandlerService {

    UserService userService;
    ApplicationContext context;
    ReplyKeyboardService replyKeyboardService;

    protected void handler(Update update, VacancyBot bot) throws TelegramApiException {
        if (!update.hasMessage() || update.getMessage() == null)
            return;

        Message message = update.getMessage();
        Long chatId = message.getChatId();
        Optional<User> userOptional = userService.getUserByLogin(message.getChat().getUserName());
        ReplyKeyboard replyKeyboard;
        if (userOptional.isEmpty()) {
            replyKeyboard = replyKeyboardService.getReplyKeyboardForNewUser();
            sendStartMessage(replyKeyboard, message, bot);
            return;
        }

        User user = userOptional.get();
        String command = message.getText();
        SendMessage sendMessage;

        try {
            CommandRule rule = (CommandRule) context.getBean(command);
            sendMessage = rule.execute(update, bot, user);
        } catch (Exception e) {
            sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .replyMarkup(replyKeyboardService.getReplyKeyboard(user))
                    .text("Что за команда '%s'?".formatted(message.getText()))
                    .build();
        }

        if (sendMessage != null) {
            userService.save(user);
            bot.sendMessage(user, sendMessage, message.getText());
        }
    }

    private void sendStartMessage(ReplyKeyboard replyKeyboard, Message message, VacancyBot bot) throws TelegramApiException {
        SendMessage sendMessage = SendMessage.builder()
                .text(getStartMessage()
                        .formatted(message.getChat().getFirstName()))
                .chatId(message.getChatId())
                .replyMarkup(replyKeyboard)
                .build();

        User newUser = userService.saveByMessage(message);
        bot.sendMessage(newUser, sendMessage, message.getText());
    }

    private String getStartMessage() {
        return "Добро пожаловть в чат бот, %s! Здесь Вы можете смотреть вакансии МагнитТеха!\nВыбери команду из меню";
    }

}
