package com.cherkashin.telegramBot.config.bot;

import com.cherkashin.telegramBot.model.entity.User;
import com.cherkashin.telegramBot.service.UserService;
import com.cherkashin.telegramBot.service.bot.BotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@Slf4j
public class VacancyBot extends TelegramLongPollingBot {

    private final BotService botService;
    private final UserService userService;

    public VacancyBot(Environment env, BotService botService, UserService userService) {
        super(env.getProperty("app.telegram.token"));
        this.botService = botService;
        this.userService = userService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            botService.onUpdateReceived(update, this);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public String getBotUsername() {
        return botService.getBotUsername();
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    public void sendMessage(User user, SendMessage sendMessage, String text) throws TelegramApiException {
        userService.saveUserMessage(user, sendMessage, text);
        execute(sendMessage);
    }

}
