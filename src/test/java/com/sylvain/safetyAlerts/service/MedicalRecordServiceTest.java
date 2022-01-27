package com.sylvain.safetyAlerts.service;

import com.sylvain.safetyAlerts.dao.MedicalRecordDao;
import com.sylvain.safetyAlerts.exception.DataAlreadyExistException;
import com.sylvain.safetyAlerts.exception.DataNotFoundException;
import com.sylvain.safetyAlerts.exception.InvalidArgumentException;
import com.sylvain.safetyAlerts.models.MedicalRecord;
import com.sylvain.safetyAlerts.models.Person;
import com.sylvain.safetyAlerts.repository.DataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordServiceTest {
    @Autowired
    IMedicalRecordService medicalRecordService;

    @MockBean
    DataRepository dataRepository;

    @MockBean
    MedicalRecordDao medicalRecordDaoMock;

    @MockBean
    DataNotFoundException dataNotFoundException;

    @MockBean
    InvalidArgumentException invalidArgumentException;

    Person personBoyd = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512",
            "jaboyd@email.com");

    Person personNoMedicalRecord = new Person("Tom", "Poe", "3 place gambetta", "Paris", "59800", "066666", "tom@mail.com");

    Person personChild = new Person("Roger", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512",
            "jaboyd@email.com");

    List<String> medications = new ArrayList<String>(Arrays.asList("a", "b", "c", "d"));
    List<String> allergies = new ArrayList<String>(Arrays.asList("e", "f", "g", "h"));

    MedicalRecord medicalrecordBoyd = new MedicalRecord("Roger", "Boyd", "03/06/1984", medications, allergies);
    MedicalRecord medicalrecordChild = new MedicalRecord("John", "Boyd", "09/06/2017", medications, allergies);
    MedicalRecord medicalrecordUnknown = new MedicalRecord("Tom", "Poe", "03/01/1984", medications, allergies);
    MedicalRecord medicalrecordEmpty = new MedicalRecord("", "", "03/01/1984", medications, allergies);

    @Test
    void createExistingMedicalRecord() throws Exception {

        // GIVEN
        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        medicalRecordList.add(medicalrecordBoyd);
        medicalRecordList.add(medicalrecordChild);

        // WHEN
        Mockito.when(dataRepository.getListMedicalRecord()).thenReturn(medicalRecordList);

        // THEN
        try {
            medicalRecordService.createMedicalRecord(medicalrecordBoyd);
            verify(medicalRecordDaoMock, Mockito.times(0)).createMedicalRecord(medicalrecordBoyd);
        } catch (DataAlreadyExistException eExp) {
            assert (eExp.getMessage().contains("ou le medicalRecord existe "));
        }

    }

    @Test
    void createNonExistingMedicalRecord() throws Exception {

        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        medicalRecordList.add(medicalrecordBoyd);

        Mockito.when(dataRepository.getListMedicalRecord()).thenReturn(medicalRecordList);
        Mockito.when(dataRepository.getPersonByName(medicalrecordChild.getFirstName(), medicalrecordChild.getLastName())).thenReturn(personChild);

        medicalRecordService.createMedicalRecord(medicalrecordChild);
        verify(medicalRecordDaoMock, Mockito.times(1)).createMedicalRecord(medicalrecordChild);

    }

    @Test
    void updateExistingMedicalRecord() throws Exception {
        Mockito.when(medicalRecordDaoMock.updateMedicalRecord(any(MedicalRecord.class))).thenReturn(true);
        medicalRecordService.updateMedicalRecord(medicalrecordBoyd);
        verify(medicalRecordDaoMock, Mockito.times(1)).updateMedicalRecord(medicalrecordBoyd);
    }

    @Test
    void updateNonExistingMedicalRecord() throws Exception {
        Mockito.when(medicalRecordDaoMock.updateMedicalRecord(any(MedicalRecord.class))).thenReturn(false);
        try {
            medicalRecordService.updateMedicalRecord(medicalrecordBoyd);
            verify(medicalRecordDaoMock, Mockito.times(0)).updateMedicalRecord(medicalrecordBoyd);
        } catch (DataNotFoundException dnfe) {
            assert (dnfe.getMessage().contains(" n'existe pas "));
        }
    }

    @Test
    void deleteExistingMedicalRecord() throws Exception {
        Mockito.when(medicalRecordDaoMock.deleteMedicalRecord(any(MedicalRecord.class))).thenReturn(true);
        medicalRecordService.deleteMedicalRecord(medicalrecordBoyd);
        verify(medicalRecordDaoMock, Mockito.times(1)).deleteMedicalRecord(medicalrecordBoyd);
    }

    @Test
    void deleteNonExistingMedicalRecord() throws Exception {
        Mockito.when(medicalRecordDaoMock.deleteMedicalRecord(any(MedicalRecord.class))).thenReturn(false);
        try {
            medicalRecordService.deleteMedicalRecord(medicalrecordBoyd);
            verify(medicalRecordDaoMock, Mockito.times(2)).deleteMedicalRecord(medicalrecordBoyd);
        } catch (DataNotFoundException dnfe) {
            assert (dnfe.getMessage().contains(" n'a pas de dossier medical "));
        }
    }
}
