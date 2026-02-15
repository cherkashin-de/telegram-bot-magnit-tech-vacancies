package com.cherkashin.telegramBot.repository;

import com.cherkashin.telegramBot.model.entity.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {



}
