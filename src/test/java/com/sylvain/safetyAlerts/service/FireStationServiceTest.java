package com.sylvain.safetyAlerts.service;

import com.sylvain.safetyAlerts.dao.FireStationDaoImpl;
import com.sylvain.safetyAlerts.dto.FireDTO;
import com.sylvain.safetyAlerts.dto.FoyerDTO;
import com.sylvain.safetyAlerts.exception.DataAlreadyExistException;
import com.sylvain.safetyAlerts.exception.DataNotFoundException;
import com.sylvain.safetyAlerts.exception.InvalidArgumentException;
import com.sylvain.safetyAlerts.models.FireStation;
import com.sylvain.safetyAlerts.models.MedicalRecord;
import com.sylvain.safetyAlerts.models.Person;
import com.sylvain.safetyAlerts.repository.DataRepository;
import org.junit.jupiter.api.Assertions;
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
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FireStationServiceTest {
    @Autowired
    IFireStationService fireStationService;

    @MockBean
    IPersonService personService;

    @MockBean
    FireStationDaoImpl firestationDaoMock;

    @MockBean
    DataRepository dataRepository;

    @MockBean
    DataNotFoundException dataNotFoundException;

    @MockBean
    InvalidArgumentException invalidArgumentException;

    FireStation firestation1 = new FireStation("StationUn", "1");
    FireStation firestation2 = new FireStation("StationDeux", "2");
    FireStation firestationVide = new FireStation("", "");

    Person obama = new Person("Jack", "Poe", "StationUn", "Washinton DC", "1232111", "06755", "jack@mail.com");
    Person biden = new Person("Tom", "Pie", "StationDeux", "Washinton DC", "1232111", "06754", "tom@mail.com");
    Person trump = new Person("Theo", "Pae", "Station", "Washinton DC", "1232111", "06753", "theo@mail.com");

    List<String> medication = List.of("a,b,c,d");
    List<String> allergies = List.of("t,p,e,i");

    MedicalRecord medicalrecordObama = new MedicalRecord("Jack", "Poe",
            "13/07/1999", medication, allergies);


    @Test
    public void createExistingFirestationTest() throws Exception {
        List<FireStation> listFirestation = new ArrayList<FireStation>();
        listFirestation.add(firestation1);
        Mockito.when(dataRepository.getAllStation()).thenReturn(listFirestation);
        try {
            Assertions.assertFalse(fireStationService.createFireStation(firestation1));
            verify(firestationDaoMock, Mockito.times(0)).createFireStation(firestation1);
        } catch (DataAlreadyExistException eExp) {
            assert (eExp.getMessage().contains("existe déja"));
        }
    }

    @Test
    public void createInvalidFirestationTest() throws Exception {
        List<FireStation> listFirestation = new ArrayList<FireStation>();
        listFirestation.add(firestationVide);
        Mockito.when(dataRepository.getAllStation()).thenReturn(listFirestation);
        try {
            Assertions.assertFalse(fireStationService.createFireStation(firestationVide));
            verify(firestationDaoMock, Mockito.times(0)).createFireStation(firestationVide);
        } catch (InvalidArgumentException eExp) {
            assert (eExp.getMessage().contains("adresse ou station vide"));
        }
    }


    @Test
    public void createValidFirestationTest() throws Exception {
        List<FireStation> listFirestation = new ArrayList<FireStation>();
        Mockito.when(dataRepository.getAllStation()).thenReturn(listFirestation);
        Assertions.assertTrue(fireStationService.createFireStation(firestation1));
        verify(firestationDaoMock, Mockito.times(1)).createFireStation(firestation1);
    }

    @Test
    public void updateExistingFirestationTest() throws Exception {
        Mockito.when(firestationDaoMock.updateFireStation(any(FireStation.class))).thenReturn(true);
        Assertions.assertTrue(fireStationService.updateFireStation(firestation1));
        verify(firestationDaoMock, Mockito.times(1)).updateFireStation(firestation1);
    }

    @Test
    public void updateNonExistingFirestationTest() throws Exception {
        Mockito.when(firestationDaoMock.updateFireStation(any(FireStation.class))).thenReturn(false);
        try {
            Assertions.assertFalse(fireStationService.updateFireStation(firestation1));
            verify(firestationDaoMock, Mockito.times(1)).updateFireStation(firestation1);
        } catch (DataNotFoundException dnfe) {
            assert (dnfe.getMessage().contains("n'existe pas"));
        }
    }

    @Test
    public void deleteExistingFirestationTest() throws Exception {
        Mockito.when(firestationDaoMock.deleteFireStation(any(FireStation.class))).thenReturn(true);
        Assertions.assertTrue(fireStationService.deleteFireStation(firestation1));
        verify(firestationDaoMock, Mockito.times(1)).deleteFireStation(firestation1);
    }

    @Test
    public void deleteNonExistingFirestationTest() throws Exception {
        Mockito.when(firestationDaoMock.deleteFireStation(any(FireStation.class))).thenReturn(false);
        try {
            Assertions.assertFalse(fireStationService.deleteFireStation(firestation1));
            verify(firestationDaoMock, Mockito.times(1)).deleteFireStation(firestation1);
        } catch (DataNotFoundException dnfe) {
            assert (dnfe.getMessage().contains("n'existe pas"));
        }
    }

    @Test
    public void getPhoneByStationTest() {
        List<Person> persons = new ArrayList<Person>();
        persons.add(biden);
        persons.add(obama);
        persons.add(trump);
        List<FireStation> firestations = new ArrayList<>();
        firestations.add(firestation1);
        Mockito.when(dataRepository.getAllPersons()).thenReturn(persons);
        Mockito.when(dataRepository.getFirestationByStation("1")).thenReturn(firestations);
        List<String> phones = fireStationService.getPhoneByStation("1");
        Assertions.assertEquals(phones.size(), 1);
        Assertions.assertEquals(phones.get(0), persons.get(1).getPhone());


    }

    @Test
    public void getFoyerByFireStationTest() {
        List<String> addreList = new ArrayList<String>();
        addreList.add("StationUn");
        Mockito.when(dataRepository.getListFireStation(List.of("1"))).thenReturn(addreList);
        FireDTO fireDTO = new FireDTO();
        fireDTO.setAge(9);
        fireDTO.setFirstName("thomas");
        fireDTO.setLastName("Poe");
        fireDTO.setAllergies(allergies);
        fireDTO.setMedications(medication);
        fireDTO.setPhone("1234");
        fireDTO.setStation("1");
        List<FireDTO> fireDTOs = new ArrayList<FireDTO>();
        fireDTOs.add(fireDTO);
        Mockito.when(personService.getPersonByAddress("StationUn")).thenReturn(fireDTOs);

        List<FoyerDTO> result = fireStationService.getFoyerByFireStation(List.of("1"));

        org.junit.jupiter.api.Assertions.assertEquals(result.size(), 1);
        FoyerDTO foyerDTO = result.get(0);
        org.junit.jupiter.api.Assertions.assertEquals(foyerDTO.getAddress(), "StationUn");
        org.junit.jupiter.api.Assertions.assertEquals(foyerDTO.getFirePerson(), fireDTOs);
    }
}
