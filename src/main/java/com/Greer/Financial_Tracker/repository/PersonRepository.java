package com.Greer.Financial_Tracker.repository;

import com.Greer.Financial_Tracker.model.PersonEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<PersonEntity, Long> {

    public Optional<PersonEntity> findByEmail(String email);
}
