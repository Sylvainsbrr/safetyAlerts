package com.sylvain.safetyAlerts.service;

import com.sylvain.safetyAlerts.dao.MedicalRecordDao;
import com.sylvain.safetyAlerts.dao.PersonDao;
import com.sylvain.safetyAlerts.dto.ChildAlertDTO;
import com.sylvain.safetyAlerts.dto.PersonInfoDTO;
import com.sylvain.safetyAlerts.exception.DataAlreadyExistException;
import com.sylvain.safetyAlerts.exception.DataNotFoundException;
import com.sylvain.safetyAlerts.models.MedicalRecord;
import com.sylvain.safetyAlerts.models.Person;
import com.sylvain.safetyAlerts.repository.DataRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc

public class PersonServiceTest {
    @Autowired
    IPersonService personService;

    @MockBean
    DataRepository dataRepository;

    @MockBean
    PersonDao personDao;

    @MockBean
    MedicalRecordDao medicalRecordDao;

    Person jill = new Person("jhony", "doe", "add1", "Washinton DC", "12345", "1234", "jhony@mail.com");
    Person chris = new Person("tom", "poe", "add2", "newyork", "72300", "1435", "tom@mail.com");
    Person obama = new Person("Barack", "obama", "WhiteHouse", "Washinton DC", "1232111","06755" , "obama@mohamed.com");

    List<String> medication = List.of("lsd,lean");
    List<String> allergies = List.of("polen, girls");

    MedicalRecord medicalrecordObama = new MedicalRecord("Barack", "obama", "03/06/1984", medication, allergies);

    String city = "add1";

    @Test
    public void createNoneExistingNPersonTest() {

        // Given
        List<Person> persons = new ArrayList<>();

        // When
        Mockito.when(dataRepository.getAllPersons()).thenReturn(persons);

        // then
        Assertions.assertThat(personService.createPerson(chris));

        verify(personDao, Mockito.times(1)).createPerson((chris));

    }

    @Test
    public void createExistingNPersonTest() throws Exception {

        // Given
        List<Person> persons = new ArrayList<>();
        persons.add(chris);

        // When
        Mockito.when(dataRepository.getAllPersons()).thenReturn(persons);

        // then
        try {
            Assertions.assertThat(personService.createPerson(chris));
            verify(personDao, Mockito.times(0)).createPerson(any());
        } catch (DataAlreadyExistException eExp) {
            assert (eExp.getMessage().contains("existe déja"));
        }

    }

    @Test
    public void updateExistingPersonTest() throws Exception {

        // when
        Mockito.when(personDao.updatePerson(any(Person.class))).thenReturn(true);

        // then
        Assertions.assertThat(personService.updatePerson(chris));

        verify(personDao, Mockito.times(1)).updatePerson((chris));

    }

    @Test
    public void updateNoneExistingPersonTest() throws Exception {

        // when
        Mockito.when(personDao.updatePerson(any(Person.class))).thenReturn(false);

        // THEN
        // On crée un personne qui existe
        try {
            Assertions.assertThat(personService.updatePerson(chris));
            verify(personDao, Mockito.times(1)).updatePerson(any());
        } catch (DataNotFoundException eExp) {
            assert (eExp.getMessage().contains("n'existe pas"));
        }
    }

    @Test
    public void deleteExistingPersonTest() {
        // when
        Mockito.when(personDao.deletePerson(any(Person.class))).thenReturn(true);
        Assertions.assertThat(personService.deletePerson(chris));

        verify(personDao, Mockito.times(1)).deletePerson((chris));
    }

    @Test
    public void deleteNoneExistingPersonTest() throws Exception {

        Mockito.when(personDao.deletePerson(any(Person.class))).thenReturn(false);
        try {
            Assertions.assertThat(personService.deletePerson(chris));
            verify(personDao, Mockito.times(1)).deletePerson(any());
        } catch (DataNotFoundException eExp) {
            assert (eExp.getMessage().contains("n'existe pas"));
        }
    }

    @Test
    public void getValidCommunityEmailTest() throws Exception {

        // Given
        Mockito.when(dataRepository.getPersonByCity(city)).thenReturn(List.of(chris, jill));
        // when
        Collection<String> emails = personService.getCommunityEmail(city);
        // then
        assertThat(emails).containsExactlyInAnyOrderElementsOf(List.of(chris.getEmail(), jill.getEmail()));
    }

    @Test
    public void getChildAlertTest() throws Exception {
        // Préparation du jeu de tests
        List<Person> persons = new ArrayList<Person>();
        persons.add(chris);
        Mockito.when(dataRepository.getPersonByAddress("newyork")).thenReturn(persons);

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setAllergies(allergies);
        medicalRecord.setMedications(medication);
        medicalRecord.setFirstName("tom");
        medicalRecord.setLastName("poe");
        medicalRecord.setBirthdate("28/05/2013");
        Mockito.when(dataRepository.getMedicalRecordByFirstNameAndLastName("tom","poe")).thenReturn(medicalRecord);

        List<Person> familyMember = new ArrayList<Person>();
        Mockito.when(dataRepository.getFamilyMemberByLastName("poe")).thenReturn(familyMember);

        // Appel de la méthode à tester
        List<ChildAlertDTO> result = personService.getChildAlert("newyork");

        // Vérifier les données du résultat
        org.junit.jupiter.api.Assertions.assertEquals(result.size(), 1);

        ChildAlertDTO childAlertDTO = result.get(0);
        org.junit.jupiter.api.Assertions.assertEquals(childAlertDTO.getAge(), 9);
        org.junit.jupiter.api.Assertions.assertEquals(childAlertDTO.getFistName(), "tom");
        org.junit.jupiter.api.Assertions.assertEquals(childAlertDTO.getLastName(),"poe");
        org.junit.jupiter.api.Assertions.assertEquals(childAlertDTO.getFamilyMember().size(), 0);

    }

    @Test
    public void getPersonTest() throws Exception {
        //Given
        medicalrecordObama.setBirthdate(LocalDate.now().minusYears(30).format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        Mockito.when(dataRepository.getPersonByLastNameAndFirsName(obama.getLastName(),obama.getFirstName())).thenReturn(List.of(obama));
        Mockito.when(dataRepository.getMedicalRecordByFirstNameAndLastName(obama.getFirstName(), obama.getLastName())).thenReturn(medicalrecordObama);
        //when
        List<PersonInfoDTO> infos = personService.getPersonInfo(obama.getFirstName(), obama.getLastName());
        //then
        assertThat(infos).hasSize(1);
        PersonInfoDTO infoObama = infos.get(0);
        assertThat(infoObama.getAge()).isEqualTo(30);
        assertThat(infoObama.getFirstName()).isEqualTo("Barack");
        assertThat(infoObama.getLastName()).isEqualTo("obama");
        assertThat(infoObama.getEmail()).isEqualTo("obama@mohamed.com");
        assertThat(infoObama.getAddress()).isEqualTo("WhiteHouse");
        assertThat(infoObama.getAllergies()).isEqualTo(medicalrecordObama.getAllergies());
        assertThat(infoObama.getMedications()).isEqualTo(medicalrecordObama.getMedications());
    }
}
