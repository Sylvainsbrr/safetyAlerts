package com.sylvain.safetyAlerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sylvain.safetyAlerts.exception.DataRepositoryException;
import com.sylvain.safetyAlerts.models.Database;
import com.sylvain.safetyAlerts.models.FireStation;
import com.sylvain.safetyAlerts.models.MedicalRecord;
import com.sylvain.safetyAlerts.models.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DataRepository {
    // permet de mapper le json en objet java.
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static Database database;
    private String JSON_FILE = "data.json";
    private static final Logger logger = LogManager.getLogger(DataRepository.class);
    // on commit dans le main et non dans les tests
    private Boolean commit = true;

    public DataRepository() throws IOException {
        this.init();
    }

    public Database getDatabase() {
        return database;
    }

    public static void setDatabase(Database database) {
        DataRepository.database = database;
    }

    public Boolean getCommit() {
        return commit;
    }

    public void setCommit(Boolean commit) {
        this.commit = commit;
    }

    // On charge tous qu'il y a dans le JSON dans un Objet java : database
    public void init() {
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(JSON_FILE)) {
            database = objectMapper.readerFor(Database.class).readValue(inputStream);
            logger.info("OK-ouverture fichier json");
        } catch (FileNotFoundException fileNotFoundException) {
            logger.info("KO-fichier json introuvable");
            throw new DataRepositoryException("KO-fichier json introuvable" + JSON_FILE, fileNotFoundException);
        } catch (IOException ioException) {
            logger.info("KO-fichier corrompu");
            throw new DataRepositoryException("KO-fichier corrompu" + JSON_FILE, ioException);
        }
    }

    // On sauvegarde tout les changements de database dans le JSON
    public void commit() {
        if (commit) {
            // recuperer le path du JSON
            URL url = ClassLoader.getSystemResource(JSON_FILE);
            // On ouvre un flux d'ecriture vers le fichier JSOn
            try (OutputStream outputStream = new FileOutputStream(url.getFile())) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, database);
                logger.info("OK - Fichier JSON mise à jour: " + JSON_FILE);

            } catch (FileNotFoundException fnfe) {

                logger.info("KO - FILE_NOT_FOUND : " + JSON_FILE);
                throw new DataRepositoryException("KO - FILE_NOT_FOUND : ", fnfe);

            } catch (IOException ioe) {

                logger.info("KO - I/O ERREUR : " + JSON_FILE);
                throw new DataRepositoryException("KO - I/O ERREUR : ", ioe);

            }
        }
    }

    /**
     * @return Retourne la list de toute les personnes
     */
    public List<Person> getAllPersons() {
        return database.getPersons();
    }

    /**
     * @param
     * @return Retourne la list de toute les firestation
     */
    public List<FireStation> getListFireStation() {
        return database.getFirestations();
    }

    /**
     * @param
     * @return Retourne la list de toute les medicalRecords
     */
    public List<MedicalRecord> getListMedicalRecord() {
        return database.getMedicalrecords();
    }


    public List<Person> getPersonByCity(String city) {
        return database.getPersons().stream().filter(person -> person.getCity().equalsIgnoreCase(city)).collect(Collectors.toList());
    }

    public List<Person> getPersonByAddress(String address) {
        return database.getPersons().stream().filter(person -> person.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());
    }

    public List<Person> getFamilyMemberByLastName(String lastName) {
        return database.getPersons().stream().filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    public List<Person> getPersonByLastNameAndFirsName(String lastName, String firstName) {
        return database.getPersons().stream().filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    public MedicalRecord getMedicalRecordByFirstNameAndLastName(String firstName, String lastName) {
        MedicalRecord medicalRecordResult = new MedicalRecord();
        for (MedicalRecord medicalRecord : database.getMedicalrecords()) {
            if (medicalRecord.getFirstName().equalsIgnoreCase(firstName)
                    && medicalRecord.getLastName().equalsIgnoreCase(lastName)) {
                medicalRecordResult = medicalRecord;
            }
        }
        return medicalRecordResult;
    }

    public FireStation getStationByAddress(String address) {
        for (FireStation firestation : database.getFirestations()) {
            if (firestation.getAddress().equalsIgnoreCase(address)) {
                return firestation;
            }
        }
        return null;

    }


    public Person getPersonByName(String firstName, String lastName) {
        Person personResult = new Person();
        for (Person person : database.getPersons()) {
            if (person.getFirstName().equalsIgnoreCase(firstName) && person.getLastName().equalsIgnoreCase(lastName)) {
                personResult = person;
            }
        }
        return personResult;
    }
    public List<FireStation> getAllStation() {
        return database.getFirestations();
    }

    public List<FireStation> getFirestationByStation(String firestation) {

        return database.getFirestations().stream()
                .filter(fireStation -> fireStation.getStation().equalsIgnoreCase(firestation))
                .collect(Collectors.toList());

    }

    public List<String> getListFireStation(List<String> stationNumber) {
        return database.getFirestations().stream()
                .filter(fireStation -> stationNumber.contains(fireStation.getStation())).map(FireStation::getAddress)
                .collect(Collectors.toList());
    }




}
