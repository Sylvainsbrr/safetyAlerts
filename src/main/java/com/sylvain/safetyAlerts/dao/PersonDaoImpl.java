package com.sylvain.safetyAlerts.dao;

import com.sylvain.safetyAlerts.models.Person;
import com.sylvain.safetyAlerts.repository.DataRepository;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonDaoImpl implements  PersonDao{
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PersonDaoImpl.class);

    @Autowired
    private DataRepository dataRepository;

    @Override
    public boolean createPerson(Person person) {
        // Ajout d'une personne en memoire java
        dataRepository.getAllPersons().add(person);
        logger.info("createPerson : Une personne a été ajouter a la liste des personnes");
        // Application des changements avec un commit
        dataRepository.commit();
        logger.info("createperson: La personne a bien été ajouter");
        return true;
    }

    @Override
    public boolean updatePerson(Person person) {
        if (dataRepository.getAllPersons().remove(person)) {
            this.createPerson(person);
            return true;
        }
        return false;
    }

    @Override
    public boolean deletePerson(Person person) {
        // On supprime la personne en memoire
        boolean result = dataRepository.getAllPersons().remove(person);
        // Application des changements avec un commit
        dataRepository.commit();
        logger.info("deleteperson: la personne a bien été suprimmé0");
        return result;

    }

}
