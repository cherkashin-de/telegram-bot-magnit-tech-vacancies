package com.cherkashin.telegramBot.service.mapper;

import com.cherkashin.telegramBot.model.Supportive;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface toVacancyInformation {

    toVacancyInformation INSTANCE = Mappers.getMapper(toVacancyInformation.class);

    @Mapping(
            target = "technologies",
            expression = "java(dto.technologies() == null || dto.technologies().isEmpty() " +
                    "? null " +
                    ": dto.technologies().get(0).name())"
    )
    @Mapping(
            target = "workFormat",
            expression = "java(dto.workFormats() == null || dto.workFormats().isEmpty() ? null : dto.workFormats().get(0).name())"
    )
    @Mapping(
            target = "direction",
            expression = "java(dto.direction() == null ? null : dto.direction().name())"
    )
    @Mapping(
            target = "speciality",
            expression = "java(dto.speciality() == null ? null : dto.speciality().name())"
    )
    Supportive.DTO.VacancyInformation toEntity(Supportive.DTO.Result dto);

}
