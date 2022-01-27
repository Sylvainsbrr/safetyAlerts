package com.sylvain.safetyAlerts.controller;

import com.sylvain.safetyAlerts.models.MedicalRecord;
import com.sylvain.safetyAlerts.service.IMedicalRecordService;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class MedicalRecordController {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PersonController.class);

    @Autowired
    private IMedicalRecordService medicalRecordService;

    // Creation d'un medicalRecord
    @PostMapping(path = "medicalRecord")
    @ResponseStatus(HttpStatus.CREATED)
    public void createFireStation(@RequestBody @Valid MedicalRecord medicalRecord) {
        logger.info("createFireStation : appel du controller medicalRecord");
        medicalRecordService.createMedicalRecord(medicalRecord);
    }


    // Mise a jour d'un medicalRecord
    @PutMapping(path = "medicalRecord")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMedicalRecord(@RequestBody @Valid MedicalRecord medicalRecord) {
        logger.info("updateMedicalRecord : appel du controller medicalRecord");
        medicalRecordService.updateMedicalRecord(medicalRecord);
    }

    // Suppression d'un medicalRecord
    @DeleteMapping(path = "medicalRecord")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deleteMedicalRecord(@RequestBody @Valid MedicalRecord medicalRecord) {
        logger.info("deleteMedicalRecord : appel du controller medicalRecord");
        medicalRecordService.deleteMedicalRecord(medicalRecord);
    }

}
