package com.sylvain.safetyAlerts.service;

import com.sylvain.safetyAlerts.dao.FireStationDao;
import com.sylvain.safetyAlerts.dao.FireStationDaoImpl;
import com.sylvain.safetyAlerts.dto.CoverageDTO;
import com.sylvain.safetyAlerts.dto.FireDTO;
import com.sylvain.safetyAlerts.dto.FoyerDTO;
import com.sylvain.safetyAlerts.exception.DataAlreadyExistException;
import com.sylvain.safetyAlerts.exception.DataNotFoundException;
import com.sylvain.safetyAlerts.exception.InvalidArgumentException;
import com.sylvain.safetyAlerts.models.FireStation;
import com.sylvain.safetyAlerts.models.MedicalRecord;
import com.sylvain.safetyAlerts.models.Person;
import com.sylvain.safetyAlerts.repository.DataRepository;
import com.sylvain.safetyAlerts.utils.CalculateAge;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FireStationServiceIMPL implements IFireStationService {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(FireStationDaoImpl.class);

    @Autowired
    DataRepository dataRepository;
    @Autowired
    IPersonService personService;
    @Autowired
    FireStationDao fireStationDao;

    @Override
    public boolean createFireStation(FireStation fireStation) {
        // Verification que la fireStation n'existe pas deja dans la DAO(datarepository)
        if (!StringUtils.isEmpty(fireStation.getStation()) && !StringUtils.isEmpty(fireStation.getAddress())) {

            if (!dataRepository.getAllStation().contains(fireStation)) {
                fireStationDao.createFireStation(fireStation);
                return true;
            } else {
                throw new DataAlreadyExistException("La fireStation " + fireStation.getStation() + " " + fireStation.getAddress() + "existe déja");
            }
        }
        throw new InvalidArgumentException(" fireStation non creer" + fireStation.getStation() + " " + fireStation.getAddress() + " adresse ou station vide ");
    }

    @Override
    public boolean updateFireStation(FireStation fireStation) {
        if (!fireStationDao.updateFireStation(fireStation)) {
            throw new DataNotFoundException("La fireStation " + fireStation.getStation() + " " + fireStation.getAddress() + "n'existe pas");
        }
        return true;
    }

    @Override
    public boolean deleteFireStation(FireStation fireStation) {
        if (!fireStationDao.deleteFireStation(fireStation)) {
            throw new DataNotFoundException("La fireStation " + fireStation.getStation() + " " + fireStation.getAddress() + "n'existe pas");
        }
        return true;
    }


    // Récupération des numéros en fonction de la station
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


    // Methode pour l'url qui recupere les personnes corcernes par une caserne et le nombre d'adultes et d'enfants
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

    // Methode pour l'url qui recupere les foyers corcernes par la caserne avec un regroupement par adresse et infos de chaque habitants
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
