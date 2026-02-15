package com.cherkashin.telegramBot.service;

import com.cherkashin.telegramBot.model.entity.User;
import com.cherkashin.telegramBot.repository.VacancyRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.cherkashin.telegramBot.constant.Supportive.Commands.*;
import static com.cherkashin.telegramBot.constant.Supportive.Commands.CallbackQuery.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReplyKeyboardService {

    Environment env;
    VacancyRepository vacancyRepository;

    public ReplyKeyboard getReplyKeyboardForNewUser() {
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        keyboardRows.add(buildOneButton(VACANCIES_GET_ALL));
        keyboardRows.add(buildOneButton(SUBSCRIBE_ON_VACANCIES));
        keyboardRows.add(buildOneButton(UNSUBSCRIBE_VACANCIES));

        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .build();
    }

    public ReplyKeyboard getReplyKeyboard(User user) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(buildOneButton(VACANCIES_GET_ALL));

        if (!user.getIsNotificationOn()) keyboardRows.add(buildOneButton(SUBSCRIBE_ON_VACANCIES));
        else keyboardRows.add(buildOneButton(SETTING_NOTIFICATION));

        keyboardRows.add(buildOneButton(UNSUBSCRIBE_VACANCIES));

        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .build();
    }

    public InlineKeyboardMarkup getReplyKeyboardForVacancies(User user) {
        int page = user.getData().getPageVacancies();
        int pageSize = env.getProperty("app.max_size_page_vacancies", Integer.class);

        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        if (page != 0)
            keyboardRows.add(buildOneInlineButton(REQUEST_PREVIOUS_LIST_VACANCIES));

        if (page * pageSize < vacancyRepository.countAllByActiveTrue() - pageSize)
            keyboardRows.add(buildOneInlineButton(REQUEST_NEXT_LIST_VACANCIES));

        keyboardRows.add(buildOneInlineButton(BACK_TO_HOME));

        return InlineKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .build();
    }

    public InlineKeyboardMarkup getReplyKeyboardForNotifications(User user) {
        int page = user.getData().getPageTechnologies();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        //todo: rewrite to Redis
        List<String> technologies = vacancyRepository.getDistinctTechnologies();
        int pageSize = page == 0 ? 5 : (page + 1) * 5;

        for (int i = page * 5; i < pageSize; i++) {
            if (i >= technologies.size()) continue;

            var technology = technologies.get(i);
            if (user.getData().getNotifications().contains(technology))
                technology = technology + " (уже подписаны)";

            keyboardRows.add(buildOneTechItemInlineButton(technology));
        }

        if (page != 0)
            keyboardRows.add(buildOneInlineButton(PREVIOUS_LIST_TECHNOLOGIES));

        if (page * 5 < vacancyRepository.getDistinctTechnologies().size() - 5)
            keyboardRows.add(buildOneInlineButton(CONTINUE_LIST_TECHNOLOGIES));

        keyboardRows.add(buildOneInlineButton(BACK_TO_HOME));

        return InlineKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .build();
    }

    private List<InlineKeyboardButton> buildOneTechItemInlineButton(String text) {
        return List.of(
                InlineKeyboardButton.builder()
                        .text(text)
                        .callbackData("%s%s".formatted(ITEM_TECHNOLOGIES, text))
                        .build()
        );
    }

    private List<InlineKeyboardButton> buildOneInlineButton(String text) {
        return List.of(
                InlineKeyboardButton.builder()
                        .text(text)
                        .callbackData(text)
                        .build()
        );
    }

    private KeyboardRow buildOneButton(String text) {
        return new KeyboardRow(
                List.of(
                        KeyboardButton.builder()
                                .text(text)
                                .build()
                )
        );
    }

}
