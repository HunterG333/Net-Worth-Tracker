package com.Greer.Financial_Tracker.services;

import com.Greer.Financial_Tracker.model.FinancesEntity;
import com.Greer.Financial_Tracker.model.PersonEntity;

import java.util.List;

public interface FinanceService {

    FinancesEntity save(FinancesEntity financesEntity);

    List<FinancesEntity> findAllRecordsWithUserId(Long userId);

    FinancesEntity getRecentFinanceEntity(PersonEntity personEntity);

}
