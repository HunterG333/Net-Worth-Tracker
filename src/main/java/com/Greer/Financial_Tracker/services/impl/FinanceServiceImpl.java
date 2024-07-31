package com.Greer.Financial_Tracker.services.impl;

import com.Greer.Financial_Tracker.model.FinancesEntity;
import com.Greer.Financial_Tracker.model.PersonEntity;
import com.Greer.Financial_Tracker.repository.FinanceRepository;
import com.Greer.Financial_Tracker.services.FinanceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceServiceImpl implements FinanceService {

    private FinanceRepository financeRepository;

    public FinanceServiceImpl(FinanceRepository financeRepository) {
        this.financeRepository = financeRepository;
    }

    @Override
    public FinancesEntity save(FinancesEntity financesEntity) {
        return financeRepository.save(financesEntity);
    }

    @Override
    public List<FinancesEntity> findAllRecordsWithUserId(Long userId) {
        return financeRepository.findAllRecordsWithUserId(userId);
    }

    @Override
    public FinancesEntity getRecentFinanceEntity(PersonEntity personEntity) {
        return financeRepository.findRecentEntityWithId(personEntity.getId());
    }

}
