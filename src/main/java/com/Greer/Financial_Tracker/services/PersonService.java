package com.Greer.Financial_Tracker.services;

import com.Greer.Financial_Tracker.model.PersonEntity;

import java.util.List;
import java.util.Optional;


public interface PersonService {

    PersonEntity save(PersonEntity personEntity);

    List<PersonEntity> findAll();

    Optional<PersonEntity> findById(long id);

    Optional<PersonEntity> findByEmail(String email);
}
