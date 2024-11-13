package com.forge.sslsj.service;// package: com.example.demo.service

import com.forge.sslsj.mapper.PersonMapper;
import com.forge.sslsj.model.Person;
import com.forge.sslsj.payload.PersonRequest;
import com.forge.sslsj.payload.PersonResponse;
import com.forge.sslsj.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PersonService {

    private final PersonRepository personRepository;

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    public List<PersonResponse> getAllPersons() {
        return personRepository.findAll().stream()
                .map(personMapper::personToPersonResponse)
                .collect(Collectors.toList());
    }

    public Optional<PersonResponse> getPersonById(Long id) {
        return personRepository.findById(id)
                .map(personMapper::personToPersonResponse);
    }

    public PersonResponse createPerson(PersonRequest personRequest) {
        Person person = personMapper.personRequestToPerson(personRequest);
        return personMapper.personToPersonResponse(personRepository.save(person));
    }

    public PersonResponse updatePerson(Long id, PersonRequest personRequest) {
        Person person = personRepository.findById(id).orElseThrow(() -> new RuntimeException("Person not found"));
        person.setFirstName(personRequest.getFirstName());
        person.setLastName(personRequest.getLastName());
        person.setEmail(personRequest.getEmail());
        person.setPhone(personRequest.getPhone());
        person.setAddress(personRequest.getAddress());
        person.setCity(personRequest.getCity());
        person.setState(personRequest.getState());
        person.setCountry(personRequest.getCountry());
        person.setPostalCode(personRequest.getPostalCode());
        Person updatedPerson = personRepository.save(person);
        return personMapper.personToPersonResponse(updatedPerson);
    }

    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }
}