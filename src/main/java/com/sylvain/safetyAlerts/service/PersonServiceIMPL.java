package com.sylvain.safetyAlerts.service;

import com.sylvain.safetyAlerts.dto.ChildAlertDTO;
import com.sylvain.safetyAlerts.dto.FireDTO;
import com.sylvain.safetyAlerts.dto.PersonInfoDTO;
import com.sylvain.safetyAlerts.models.FireStation;
import com.sylvain.safetyAlerts.models.MedicalRecord;
import com.sylvain.safetyAlerts.models.Person;
import com.sylvain.safetyAlerts.repository.DataRepository;
import com.sylvain.safetyAlerts.utils.CalculateAge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonServiceIMPL implements IPersonService {
    @Autowired
    private DataRepository dataRepository;

    @Override
    public List<String> getCommunityEmail(String city) {
        List<String> emails = new ArrayList<>();
        List<Person> persons = dataRepository.getPersonByCity(city);
        for (Person person : persons) {
            emails.add(person.getEmail());
        }
        return emails;
    }

    @Override
    public List<ChildAlertDTO> getChildAlert(String address) {
        List<ChildAlertDTO> childAlertDTOs = new ArrayList<ChildAlertDTO>();
        List<Person> persons = dataRepository.getPersonByAddress(address);

        for (Person person : persons) {
            MedicalRecord medicalRecord = dataRepository.getMedicalRecordByFirstNameAndLastName(person.getFirstName(), person.getLastName());
            int age = CalculateAge.getAge(medicalRecord.getBirthdate());
            if (age <= 18) {
                ChildAlertDTO childAlertDTO = new ChildAlertDTO();
                childAlertDTO.setFistName(person.getFirstName());
                childAlertDTO.setLastName(person.getLastName());
                childAlertDTO.setAge(age);

                List<Person> familyMember = dataRepository.getFamilyMemberByLastName(person.getLastName());
                List<String> members = new ArrayList<String>();
                for (Person familyPerson : familyMember) {
                    members.add(familyPerson.getFirstName());
                }
                childAlertDTO.setFamilyMember(members);

                childAlertDTOs.add(childAlertDTO);
            }
        }
        return childAlertDTOs;
    }

    @Override
    public List<PersonInfoDTO> getPersonInfo(String firstName, String lastName) {
        List<PersonInfoDTO> personInfoDTOs = new ArrayList<PersonInfoDTO>();
        List<Person> persons = dataRepository.getPersonByLastNameAndFirsName(lastName, firstName);
        for (Person person :persons) {
            MedicalRecord medicalRecord = dataRepository.getMedicalRecordByFirstNameAndLastName(person.getFirstName(), person.getLastName());
            int age = CalculateAge.getAge(medicalRecord.getBirthdate());
            PersonInfoDTO personInfoDTO = new PersonInfoDTO();
            personInfoDTO.setFirstName(person.getFirstName());
            personInfoDTO.setLastName(person.getLastName());
            personInfoDTO.setAddress(person.getAddress());
            personInfoDTO.setAge(age);
            personInfoDTO.setEmail(person.getEmail());
            personInfoDTO.setAllergies(medicalRecord.getAllergies());
            personInfoDTO.setMedications(medicalRecord.getMedications());
            personInfoDTOs.add(personInfoDTO);
        }

        return personInfoDTOs;
    }

    @Override
    public List<FireDTO> getPersonByAddress(String address) {
        List<FireDTO> fireDTOs = new ArrayList<FireDTO>();
        List<Person> persons = dataRepository.getPersonByAddress(address);
        for (Person person :persons) {
            MedicalRecord medicalRecord = dataRepository.getMedicalRecordByFirstNameAndLastName(person.getFirstName(), person.getLastName());
            FireStation firestation = dataRepository.getStationByAddress(person.getAddress());
            int age = CalculateAge.getAge(medicalRecord.getBirthdate());
            FireDTO fireDTO = new FireDTO();
            fireDTO.setFirstName(person.getFirstName());
            fireDTO.setLastName(person.getLastName());
            fireDTO.setPhone(person.getPhone());
            fireDTO.setAge(age);
            fireDTO.setMedications(medicalRecord.getMedications());
            fireDTO.setAllergies(medicalRecord.getAllergies());
            fireDTO.setStation(firestation.getStation());
            fireDTOs.add(fireDTO);
        }

        return fireDTOs;
    }

    @Override
    public boolean createPerson(Person person) {
        return false;
    }

    @Override
    public boolean updatePerson(Person person) {
        return false;
    }

    @Override
    public boolean deletePerson(Person person) {
        return false;
    }
}
