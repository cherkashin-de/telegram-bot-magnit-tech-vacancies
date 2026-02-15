package com.cherkashin.telegramBot.repository;

import com.cherkashin.telegramBot.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByIsNotificationOnTrue();

    Optional<User> findByLogin(String login);

}
