package com.cherkashin.telegramBot.service.command;

import com.cherkashin.telegramBot.config.bot.VacancyBot;
import com.cherkashin.telegramBot.model.entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandRule {

    SendMessage execute(Update update, VacancyBot bot, User user);

}
