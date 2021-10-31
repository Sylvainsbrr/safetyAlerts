package com.sylvain.safetyAlerts.service;

import com.sylvain.safetyAlerts.models.Person;

import javax.validation.Valid;
import java.util.List;

public interface IPersonService {
    List<String> getCommunityEmail(String city);

    boolean createPerson(@Valid Person person);

    boolean updatePerson(@Valid Person person);

    boolean deletePerson(@Valid Person person);
}
