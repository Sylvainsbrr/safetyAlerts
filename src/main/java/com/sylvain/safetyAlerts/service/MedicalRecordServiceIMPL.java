package com.sylvain.safetyAlerts.service;

import com.sylvain.safetyAlerts.dao.MedicalRecordDao;
import com.sylvain.safetyAlerts.dao.MedicalRecordDaoImpl;
import com.sylvain.safetyAlerts.exception.DataAlreadyExistException;
import com.sylvain.safetyAlerts.exception.DataNotFoundException;
import com.sylvain.safetyAlerts.models.MedicalRecord;
import com.sylvain.safetyAlerts.models.Person;
import com.sylvain.safetyAlerts.repository.DataRepository;
import org.apache.bcel.generic.DADD;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordServiceIMPL implements  IMedicalRecordService {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(MedicalRecordDaoImpl.class);

    @Autowired
    DataRepository dataRepository;
    @Autowired
    MedicalRecordDao medicalRecordDao;


    @Override
    public void createMedicalRecord(MedicalRecord medicalRecord) {
        Person person = dataRepository.getPersonByName(medicalRecord.getFirstName(), medicalRecord.getLastName());
        if (!dataRepository.getListMedicalRecord().contains(medicalRecord) && (person != null)) {

            medicalRecordDao.createMedicalRecord(medicalRecord);
        } else {
            throw new DataAlreadyExistException("La personne n'existe pas ou le medicalRecord existe ");
        }
    }

    @Override
    public void updateMedicalRecord(MedicalRecord medicalRecord) {
        if (!medicalRecordDao.updateMedicalRecord(medicalRecord)) {
            throw new DataNotFoundException("La personne " + medicalRecord.getLastName() + " "
                    + medicalRecord.getFirstName() + " n'existe pas ");
        }
    }

    @Override
    public void deleteMedicalRecord(MedicalRecord medicalRecord) {
        if (!medicalRecordDao.deleteMedicalRecord(medicalRecord)) {
            throw new DataNotFoundException("La personne " + medicalRecord.getLastName() + " "
                    + medicalRecord.getFirstName() + " n'a pas de dossier medical ");
        }
    }
}
