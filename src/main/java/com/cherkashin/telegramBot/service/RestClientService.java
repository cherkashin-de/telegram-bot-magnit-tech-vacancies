package com.cherkashin.telegramBot.service;

import com.cherkashin.telegramBot.model.Supportive;
import com.cherkashin.telegramBot.service.mapper.toVacancyInformation;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestClientService {

    Environment env;
    ObjectMapper objectMapper;
    RestTemplate restTemplate;

    public List<Supportive.DTO.VacancyInformation> getVacanciesOfMagnitTech() {
        AtomicBoolean hasPage = new AtomicBoolean(true);
        AtomicInteger page = new AtomicInteger(1);
        List<Supportive.DTO.VacancyInformation> vacancyInformation = new ArrayList<>();
        final String url = env.getProperty("app.magnit_tech_vacancies_url");

        while (hasPage.get()) {
            URI uri = UriComponentsBuilder.fromUriString(url)
                    .queryParam("page", page)
                    .build().toUri();

            Supportive.Response.Vacancies vacancies = restTemplate.execute(uri, HttpMethod.GET, null,
                    response -> executeResponse(response, hasPage, page));

            if (vacancies != null)
                vacancyInformation.addAll(
                        vacancies.results().stream()
                                .map(toVacancyInformation.INSTANCE::toEntity)
                                .toList()
                );
        }
        return vacancyInformation;
    }

    private Supportive.Response.Vacancies executeResponse(ClientHttpResponse response,
                                                          AtomicBoolean hasPage,
                                                          AtomicInteger page) throws IOException {

        var vacancies = objectMapper.readValue(response.getBody(), Supportive.Response.Vacancies.class);
        if (!vacancies.meta().hasMorePage())
            hasPage.set(false);

        page.incrementAndGet();

        return vacancies;
    }

}
