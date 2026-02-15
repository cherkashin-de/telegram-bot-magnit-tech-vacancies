package com.cherkashin.telegramBot.service;

import com.cherkashin.telegramBot.model.Supportive;
import com.cherkashin.telegramBot.model.entity.Vacancy;
import com.cherkashin.telegramBot.repository.VacancyRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cherkashin.telegramBot.model.Supportive.DTO.VacancyInformation.getDescription;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VacancyService {

    Environment env;
    RestClientService restClientService;
    VacancyRepository vacancyRepository;
    NotificationService notificationService;

    /**
     * Синхронизирует список вакансий с внешним REST API.
     *
     * <p>Получает актуальные вакансии, сравнивает их с активными записями в БД:
     * <ul>
     *   <li>деактивирует вакансии, отсутствующие в API, с созданием уведомления о закрытии</li>
     *   <li>добавляет новые вакансии и создаёт уведомление о появлении</li>
     * </ul>
     *
     * <p>Если API возвращает пустой список или null — метод завершает работу без изменений.
     */
    public void getAndUpdateVacanciesOfMagnitTech() {
        List<Supportive.DTO.VacancyInformation> vacancyInformations = restClientService.getVacanciesOfMagnitTech();
        if (vacancyInformations == null || vacancyInformations.isEmpty())
            return;

        List<Vacancy> vacancies = vacancyRepository.findAllByActiveTrueOrderByIdDesc();

        Map<Long, Vacancy> mapVacancy = vacancies.stream().collect(Collectors.toMap(Vacancy::getId, vacancy -> vacancy));
        Map<Long, Supportive.DTO.VacancyInformation> mapVacancyInformation = vacancyInformations.stream().collect(Collectors.toMap(Supportive.DTO.VacancyInformation::getId, vacancy -> vacancy));

        mapVacancy.forEach((id, vacancy) -> {
            if (!mapVacancyInformation.containsKey(id)) {
                vacancy.setActive(false);
                notificationService.addNotification(
                        com.cherkashin.telegramBot.constant.Supportive.Notification.CLOSE_VACANCY,
                        vacancy.getData());
            }
        });

        mapVacancyInformation.forEach((id, vacancyInformation) -> {
            if (!mapVacancy.containsKey(id)) {
                vacancies.add(Vacancy.builder()
                        .id(vacancyInformation.getId())
                        .active(true)
                        .data(vacancyInformation).build());

                notificationService.addNotification(
                        com.cherkashin.telegramBot.constant.Supportive.Notification.ADD_NEW_VACANCY,
                        vacancyInformation);
            }
        });

        vacancyRepository.saveAll(vacancies);
    }

    public List<Supportive.DTO.VacancyInformation> getPageOfVacanciesInformation(int page, int size) {
        return vacancyRepository.findAllByActiveTrueOrderByIdAsc(PageRequest.of(page, size)).getContent()
                .stream()
                .map(Vacancy::getData)
                .toList();
    }

    public String getDescriptionOfVacancies(List<Supportive.DTO.VacancyInformation> vacancyInformations, int indexStart) {
        StringBuilder sBuilder = new StringBuilder();
        String url = env.getProperty("app.magnit_tech_vacancy_url");
        for (var vacancyInformation : vacancyInformations)
            sBuilder.append(getDescription(vacancyInformation, url, indexStart++));

        return sBuilder.toString();
    }

}

