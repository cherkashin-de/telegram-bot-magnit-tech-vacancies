package com.cherkashin.telegramBot.service.command;

import com.cherkashin.telegramBot.config.bot.VacancyBot;
import com.cherkashin.telegramBot.model.entity.User;
import com.cherkashin.telegramBot.repository.VacancyRepository;
import com.cherkashin.telegramBot.service.ReplyKeyboardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.cherkashin.telegramBot.constant.Supportive.Commands.CallbackQuery.CONTINUE_LIST_TECHNOLOGIES;

@Component(CONTINUE_LIST_TECHNOLOGIES)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CallbackQueryContinueListTechnologies implements CommandRule {

    VacancyRepository vacancyRepository;
    ReplyKeyboardService replyKeyboardService;

    @Override
    public SendMessage execute(Update update, VacancyBot bot, User user) {
        int pageTechnologies = user.getData().getPageTechnologies();

        if (pageTechnologies * 5 < vacancyRepository.getDistinctTechnologies().size())
            user.getData().setPageTechnologies(pageTechnologies + 1);

        return SendMessage.builder()
                .chatId(user.getChatId())
                .replyMarkup(replyKeyboardService.getReplyKeyboardForNotifications(user))
                .text("Выберите технологии, на которые хотите подписаться")
                .build();
    }
}
