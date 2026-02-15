package com.cherkashin.telegramBot.model.entity;

import com.cherkashin.telegramBot.model.Supportive;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_event", nullable = false)
    private String typeEvent;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "message", nullable = false)
    private Supportive.DTO.VacancyInformation data;

    @Column(name = "status")
    private Integer status;
}
