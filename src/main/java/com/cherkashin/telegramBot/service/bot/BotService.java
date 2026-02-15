package com.cherkashin.telegramBot.service.bot;

import com.cherkashin.telegramBot.config.bot.VacancyBot;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BotService {

    Environment env;
    MessageHandlerService messageHandlerService;
    CallbackQueryService callbackQueryService;

    public void onUpdateReceived(Update update, VacancyBot bot) throws TelegramApiException {
        messageHandlerService.handler(update, bot);
        callbackQueryService.handler(update, bot);
    }

    public String getBotUsername() {
        return env.getProperty("app.telegram.name");
    }
}
