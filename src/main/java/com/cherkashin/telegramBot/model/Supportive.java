package com.cherkashin.telegramBot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

public enum Supportive {
    ;

    public static class Response {
        public record Vacancies(
                @JsonProperty("success") Boolean status,
                DTO.MetaInformation meta,
                List<DTO.Result> results
        ) {
        }
    }

    public static class DTO {

        public static class Notification {

            @lombok.Data
            @NoArgsConstructor
            @AllArgsConstructor
            @Builder
            public static class Data {
                private List<String> notifications;
                private Integer pageTechnologies;
                private Integer pageVacancies;
            }

        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        @EqualsAndHashCode(callSuper = false)
        @ToString
        public static class VacancyInformation {
            @EqualsAndHashCode.Include
            private Long id;
            private String title;
            private String location;
            private String direction;
            private String speciality;
            private String workFormat;
            private String technologies;

            public static String getDescription(VacancyInformation vacancyInformation, String url, Integer indexRow) {
                if (indexRow == null)
                    return vacancyInformation.getTitle() + "\n" +
                            "Формат работы: " + vacancyInformation.getWorkFormat() + "\n" +
                            "Депортамен: " + vacancyInformation.getDirection() + "\n" +
                            "<a href=\"%s\">ПОСМОТРЕТЬ ВАКАНСИЮ</a>".formatted(url + vacancyInformation.getId()) + "\n" + "\n";
                ;

                return indexRow + ") " + vacancyInformation.getTitle() + "\n" +
                        "Формат работы: " + vacancyInformation.getWorkFormat() + "\n" +
                        "Депортамен: " + vacancyInformation.getDirection() + "\n" +
                        "<a href=\"%s\">ПОСМОТРЕТЬ ВАКАНСИЮ</a>".formatted(url + vacancyInformation.getId()) + "\n" + "\n";
            }
        }

        public record Result(
                Long id,
                @JsonProperty("foreign_id") Long fId,
                String title,
                String location,
                Direction direction,
                Speciality speciality,
                @JsonProperty("work_formats") List<WorkFormat> workFormats,
                List<Technology> technologies) {
        }

        public record Direction(
                @JsonProperty("foreign_id") Integer id,
                String name
        ) {
        }

        public record Speciality(
                @JsonProperty("foreign_id") Integer id,
                String name
        ) {
        }

        public record WorkFormat(
                @JsonProperty("foreign_id") Integer id,
                String name,
                @JsonIgnore Object pivot
        ) {
        }

        public record Technology(
                @JsonProperty("foreign_id") Integer id,
                String name,
                Pivot pivot
        ) {
        }

        public record Pivot(
                @JsonProperty("vacancy_id") Integer id,
                @JsonProperty("technology_id") Integer idT,
                String name
        ) {
        }


        public record MetaInformation(
                Integer from,
                Integer to,
                @JsonProperty("per_page") Integer perPage,
                @JsonProperty("current_page") Integer currentPage,
                @JsonProperty("has_more_pages") Boolean hasMorePage,
                Integer total,
                @JsonProperty("total_without_params") Integer totalWithoutParam) {
        }
    }

}
