package com.cherkashin.telegramBot.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SchedulerService {

    VacancyService vacancyService;
    NotificationService notificationService;

    @Async
    @Scheduled(cron = "0 */5 * * * *")
    public void getAndUpdateVacanciesOfMagnitTech() {
        vacancyService.getAndUpdateVacanciesOfMagnitTech();
    }

    @Async
    @Scheduled(cron = "0 0 * * * *")
    public void notifyUsers() {
        notificationService.notifyUsers();
    }
}
