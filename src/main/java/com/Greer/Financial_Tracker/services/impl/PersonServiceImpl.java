package com.Greer.Financial_Tracker.services.impl;

import com.Greer.Financial_Tracker.model.PersonEntity;
import com.Greer.Financial_Tracker.repository.PersonRepository;
import com.Greer.Financial_Tracker.services.PersonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PersonServiceImpl implements PersonService {

    private PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public PersonEntity save(PersonEntity personEntity) {
        return personRepository.save(personEntity);
    }

    @Override
    public List<PersonEntity> findAll(){
        return StreamSupport.stream(personRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PersonEntity> findById(long id) {
        return personRepository.findById(id);
    }

    @Override
    public Optional<PersonEntity> findByEmail(String email) {
        return personRepository.findByEmail(email);
    }


}
