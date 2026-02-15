package com.cherkashin.telegramBot.model.entity;

import com.cherkashin.telegramBot.model.Supportive;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "login")
    private String login;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "notification_on")
    private Boolean isNotificationOn;

    @Column(name = "data")
    @JdbcTypeCode(SqlTypes.JSON)
    private Supportive.DTO.Notification.Data data;

}
