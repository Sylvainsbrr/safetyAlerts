package com.sylvain.safetyAlerts.dao;

import com.sylvain.safetyAlerts.models.Person;

public interface PersonDao {
    boolean createPerson(Person person);

    boolean updatePerson(Person person);

    boolean deletePerson(Person person);

}
