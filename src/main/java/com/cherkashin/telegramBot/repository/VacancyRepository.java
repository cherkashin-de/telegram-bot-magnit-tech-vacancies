package com.cherkashin.telegramBot.repository;

import com.cherkashin.telegramBot.model.entity.Vacancy;
import com.cherkashin.telegramBot.repository.nativeQuery.VacancyNativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long>, VacancyNativeQuery {

    Integer countAllByActiveTrue();

    List<Vacancy> findAllByActiveTrueOrderByIdDesc();

    Page<Vacancy> findAllByActiveTrueOrderByIdAsc(Pageable pageable);

}
