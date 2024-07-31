package com.Greer.Financial_Tracker.repository;

import com.Greer.Financial_Tracker.model.FinancesEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceRepository extends CrudRepository<FinancesEntity, Long> {

    //might need to inject hql??
    @Query(value = "SELECT * FROM \"Financial-Tracker\".finances WHERE person_id = ?1", nativeQuery = true)
    List<FinancesEntity> findAllRecordsWithUserId(Long userId);

    @Query(value = "SELECT * FROM \"Financial-Tracker\".finances WHERE person_id = ?1 ORDER BY id DESC LIMIT 1;", nativeQuery = true)
    FinancesEntity findRecentEntityWithId(Long id);
}
