package com.sylvain.safetyAlerts.controller;

import com.sylvain.safetyAlerts.models.FireStation;
import com.sylvain.safetyAlerts.service.IFireStationService;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class FireStationController {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PersonController.class);


    @Autowired
    private IFireStationService fireStationService;

    // Creation d'une station
    @PostMapping(path = "firestation")
    @ResponseStatus(HttpStatus.CREATED)
    public void createFireStation(@RequestBody @Valid FireStation firestation) {
        logger.info("createFireStation : creation avec le controller firestation");
        fireStationService.createFireStation(firestation);
    }

    // Mise a jour d'une station
    @PutMapping(path = "firestation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateFireStation(@RequestBody @Valid FireStation firestation) {
        logger.info("updateFireStation : mise a jour avec le controller firestation");
        fireStationService.updateFireStation(firestation);
    }

    // Suppression d'une station
    @DeleteMapping(path = "firestation")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deleteFireStation(@RequestBody @Valid FireStation firestation) {
        logger.info("deleteFireStation : delete avec le controller firestation");
        fireStationService.deleteFireStation(firestation);
    }
}
