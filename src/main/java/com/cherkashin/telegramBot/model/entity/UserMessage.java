package com.cherkashin.telegramBot.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users_message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "message_send")
    private String messageSend;

    @Column(name = "message_get")
    private String messageGet;

}
