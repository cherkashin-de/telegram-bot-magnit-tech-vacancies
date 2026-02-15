package com.cherkashin.telegramBot.service.command;

import com.cherkashin.telegramBot.config.bot.VacancyBot;
import com.cherkashin.telegramBot.model.Supportive;
import com.cherkashin.telegramBot.model.entity.User;
import com.cherkashin.telegramBot.service.ReplyKeyboardService;
import com.cherkashin.telegramBot.service.VacancyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.cherkashin.telegramBot.constant.Supportive.Commands.VACANCIES_GET_ALL;

@Component(VACANCIES_GET_ALL)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VacanciesGetAll implements CommandRule {

    Environment env;
    VacancyService vacancyService;
    ReplyKeyboardService replyKeyboardService;

    @Override
    public SendMessage execute(Update update, VacancyBot bot, User user) {
        int pageVacancies = user.getData().getPageVacancies();
        int pageSize = env.getProperty("app.max_size_page_vacancies", Integer.class);

        int indexStart = pageVacancies * pageSize;
        indexStart = indexStart + 1;

        List<Supportive.DTO.VacancyInformation> information = vacancyService.getPageOfVacanciesInformation(pageVacancies, pageSize);
        String description = vacancyService.getDescriptionOfVacancies(information, indexStart);

        return SendMessage.builder()
                .chatId(user.getChatId())
                .replyMarkup(replyKeyboardService.getReplyKeyboardForVacancies(user))
                .text("Наши вакансии: \n" + description)
                .parseMode("HTML")
                .build();
    }
}
