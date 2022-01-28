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

    Person jhony = new Person("jhony", "doe", "add1", "Washinton DC", "12345", "1234", "jhony@mail.com");
    Person tom = new Person("tom", "poe", "add2", "newyork", "72300", "1435", "tom@mail.com");
    Person joe = new Person("joe", "tato", "mexico", "juarez", "74000","06755" , "joe@tato.com");

    List<String> medication = List.of("lsd,lean");
    List<String> allergies = List.of("polen, girls");

    MedicalRecord medicalrecordJoe = new MedicalRecord("joe", "tato", "03/06/1984", medication, allergies);

    String city = "add1";

    @Test
    public void createNoneExistingNPersonTest() {

        // Préparation donnees test
        List<Person> persons = new ArrayList<>();
        // When
        Mockito.when(dataRepository.getAllPersons()).thenReturn(persons);
        // Appel de la méthode à tester
        Assertions.assertThat(personService.createPerson(tom));
        // Vérifier les données du résultat
        verify(personDao, Mockito.times(1)).createPerson((tom));
    }

    @Test
    public void createExistingNPersonTest() throws Exception {
        // Préparation donnees test
        List<Person> persons = new ArrayList<>();
        persons.add(tom);
        // When
        Mockito.when(dataRepository.getAllPersons()).thenReturn(persons);
        // then
        try {
            // Appel de la méthode à tester
            Assertions.assertThat(personService.createPerson(tom));
            verify(personDao, Mockito.times(0)).createPerson(any());
        } catch (DataAlreadyExistException eExp) {
            // Vérifier que le message d'exception est le bon
            assert (eExp.getMessage().contains("existe déja"));
        }
    }

    @Test
    public void updateExistingPersonTest() throws Exception {
        Mockito.when(personDao.updatePerson(any(Person.class))).thenReturn(true);
        // Appel de la méthode à tester
        Assertions.assertThat(personService.updatePerson(tom));
        // Vérifier les données du résultat
        verify(personDao, Mockito.times(1)).updatePerson((tom));
    }

    @Test
    public void updateNoneExistingPersonTest() throws Exception {
        // when
        Mockito.when(personDao.updatePerson(any(Person.class))).thenReturn(false);
        // On crée un personne qui existe
        try {
            // Appel de la méthode à tester
            Assertions.assertThat(personService.updatePerson(tom));
            // Vérifier les données du résultat
            verify(personDao, Mockito.times(1)).updatePerson(any());
        } catch (DataNotFoundException eExp) {
            // Vérifier que le message d'exception est le bon
            assert (eExp.getMessage().contains("n'existe pas"));
        }
    }

    @Test
    public void deleteExistingPersonTest() {
        Mockito.when(personDao.deletePerson(any(Person.class))).thenReturn(true);
        Assertions.assertThat(personService.deletePerson(tom));
        verify(personDao, Mockito.times(1)).deletePerson((tom));
    }

    @Test
    public void deleteNoneExistingPersonTest() throws Exception {
        Mockito.when(personDao.deletePerson(any(Person.class))).thenReturn(false);
        try {
            // Appel de la méthode à tester
            Assertions.assertThat(personService.deletePerson(tom));
            // Vérifier les données du résultat
            verify(personDao, Mockito.times(1)).deletePerson(any());
        } catch (DataNotFoundException eExp) {
            // Vérifier que le message d'exception est le bon
            assert (eExp.getMessage().contains("n'existe pas"));
        }
    }

    @Test
    public void getValidCommunityEmailTest() throws Exception {
        Mockito.when(dataRepository.getPersonByCity(city)).thenReturn(List.of(tom, jhony));
        // Appel de la méthode à tester
        Collection<String> emails = personService.getCommunityEmail(city);
        // Vérifier les données du résultat
        assertThat(emails).containsExactlyInAnyOrderElementsOf(List.of(tom.getEmail(), jhony.getEmail()));
    }

    @Test
    public void getChildAlertTest() throws Exception {
        // Préparation donnees test
        List<Person> persons = new ArrayList<Person>();
        persons.add(tom);
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
        // Préparation donnees test
        medicalrecordJoe.setBirthdate(LocalDate.now().minusYears(30).format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        Mockito.when(dataRepository.getPersonByLastNameAndFirsName(joe.getLastName(),joe.getFirstName())).thenReturn(List.of(joe));
        Mockito.when(dataRepository.getMedicalRecordByFirstNameAndLastName(joe.getFirstName(), joe.getLastName())).thenReturn(medicalrecordJoe);
        // Appel de la méthode à tester
        List<PersonInfoDTO> infos = personService.getPersonInfo(joe.getFirstName(), joe.getLastName());
        // Vérifier les données du résultat
        assertThat(infos).hasSize(1);
        PersonInfoDTO infoObama = infos.get(0);
        assertThat(infoObama.getAge()).isEqualTo(30);
        assertThat(infoObama.getFirstName()).isEqualTo("joe");
        assertThat(infoObama.getLastName()).isEqualTo("tato");
        assertThat(infoObama.getEmail()).isEqualTo("joe@tato.com");
        assertThat(infoObama.getAddress()).isEqualTo("mexico");
        assertThat(infoObama.getAllergies()).isEqualTo(medicalrecordJoe.getAllergies());
        assertThat(infoObama.getMedications()).isEqualTo(medicalrecordJoe.getMedications());
    }
}
