package com.sylvain.safetyAlerts.service;

import com.sylvain.safetyAlerts.models.Person;
import com.sylvain.safetyAlerts.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonServiceIMPL implements IPersonService {
    @Autowired
    private DataRepository dataRepository;

    @Override
    public List<String> getCommunityEmail(String city) {
        List<String> emails = new ArrayList<>();
        List<Person> persons = dataRepository.getPersonByCity(city);
        for (Person person : persons) {
            emails.add(person.getEmail());
        }
        return emails;
    }

    @Override
    public boolean createPerson(Person person) {
        return false;
    }

    @Override
    public boolean updatePerson(Person person) {
        return false;
    }

    @Override
    public boolean deletePerson(Person person) {
        return false;
    }
}
