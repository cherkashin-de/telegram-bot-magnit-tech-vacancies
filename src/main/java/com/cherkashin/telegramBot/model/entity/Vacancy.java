package com.cherkashin.telegramBot.model.entity;

import com.cherkashin.telegramBot.model.Supportive;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "vacancies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Vacancy {

    @Id
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(name = "is_activity", nullable = false)
    private Boolean active;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data")
    private Supportive.DTO.VacancyInformation data;

}
