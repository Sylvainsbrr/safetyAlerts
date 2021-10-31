package com.sylvain.safetyAlerts.service;

import com.sylvain.safetyAlerts.dto.CoverageDTO;
import com.sylvain.safetyAlerts.dto.FireDTO;
import com.sylvain.safetyAlerts.dto.FoyerDTO;
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
public class FireStationServiceIMPL implements IFireStationService {

    @Autowired
    DataRepository dataRepository;
    @Autowired
    IPersonService personService;

    @Override
    public boolean createFireStation(FireStation fireStation) {
        return false;
    }

    @Override
    public boolean updateFireStation(FireStation fireStation) {
        return false;
    }

    @Override
    public boolean deleteFireStation(FireStation fireStation) {
        return false;
    }

    @Override
    public List<String> getPhoneByStation(String firestation) {
        List<String> phones = new ArrayList<String>();
        for (FireStation fireStation : dataRepository.getFirestationByStation(firestation)) {
            for (Person person : dataRepository.getAllPersons()) {
                if (person.getAddress().equalsIgnoreCase(fireStation.getAddress())) {
                    phones.add(person.getPhone());
                }
            }
        }
        return phones;
    }

    @Override
    public List<CoverageDTO> getCoverageByFireStation(String stationNumber) {
        List<CoverageDTO> coverageDTOs = new ArrayList<CoverageDTO>();
        List<FireStation> firestations = dataRepository.getFirestationByStation(stationNumber);
        int adultCount = 0;
        int childCount = 0;
        for (FireStation firestation : firestations) {
            List<Person> persons = dataRepository.getPersonByAddress(firestation.getAddress());
            for (Person person : persons) {
                CoverageDTO coverageDTO = new CoverageDTO();
                coverageDTO.setFirstName(person.getFirstName());
                coverageDTO.setLastname(person.getLastName());
                coverageDTO.setAddress(person.getAddress());
                coverageDTO.setPhone(person.getPhone());
                MedicalRecord medicalRecord = dataRepository
                        .getMedicalRecordByFirstNameAndLastName(person.getFirstName(), person.getLastName());
                int age = CalculateAge.getAge(medicalRecord.getBirthdate());
                if (age <= 18) {
                    childCount++;
                } else {
                    adultCount++;
                }
                coverageDTO.setNombreAdulte(adultCount);
                coverageDTO.setNombreEnfant(childCount);
                coverageDTOs.add(coverageDTO);
            }
        }

        return coverageDTOs;
    }

    @Override
    public List<FoyerDTO> getFoyerByFireStation(List<String> stationNumber) {
        List<FoyerDTO> foyerDTOs = new ArrayList<FoyerDTO>();
        List<String> listAddress = dataRepository.getListFireStation(stationNumber);
        for (String address : listAddress) {
            FoyerDTO foyerDTO = new FoyerDTO();
            List<FireDTO> fireDTOs = personService.getPersonByAddress(address);
            foyerDTO.setAddress(address);
            foyerDTO.setFirePerson(fireDTOs);
            foyerDTOs.add(foyerDTO);
        }
        return foyerDTOs;
    }
}
