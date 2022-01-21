package com.sylvain.safetyAlerts.controller;

import com.sylvain.safetyAlerts.models.Person;
import com.sylvain.safetyAlerts.service.IPersonService;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PersonController {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PersonController.class);
    @Autowired
    private IPersonService personService;

    @PostMapping(path = "person")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPerson(@RequestBody @Valid Person person) {
        personService.createPerson(person);
    }

    @PutMapping(path = "person")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePerson(@RequestBody @Valid Person person) {
        personService.updatePerson(person);
    }

    @DeleteMapping(path = "person")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deletePerson(@RequestBody @Valid Person person) {
        personService.deletePerson(person);
    }

}
