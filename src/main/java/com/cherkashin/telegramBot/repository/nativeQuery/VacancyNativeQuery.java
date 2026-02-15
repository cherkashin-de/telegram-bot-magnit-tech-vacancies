package com.cherkashin.telegramBot.repository.nativeQuery;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyNativeQuery {

    @Query(
            value = """
                            SELECT DISTINCT t.field as field
                            FROM (SELECT data ->> 'technologies' as field from vacancies) t
                            where t.field is not null
                            ORDER BY t.field;
                    """
            , nativeQuery = true
    )
    List<String> getDistinctTechnologies();


}
