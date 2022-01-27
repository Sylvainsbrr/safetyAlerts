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
    // Permet de mapper le json en objet java.
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static Database database;
    private String JSON_FILE = "data.json";
    private static final Logger logger = LogManager.getLogger(DataRepository.class);
    // If true on sauvegarde les changements sur le json
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

    // On charge toutes les donnees du JSON dans un Objet java : database et on vérifie qu'il ne soit pas corrompu ou introuvable
    public void init() {
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(JSON_FILE)) {
            database = objectMapper.readerFor(Database.class).readValue(inputStream);
            logger.info("OK-Ouverture du fichier JSON");
        } catch (FileNotFoundException fileNotFoundException) {
            logger.info("KO-Le fichier JSON est introuvable");
            throw new DataRepositoryException("KO- le fichier JSON est introuvable" + JSON_FILE, fileNotFoundException);
        } catch (IOException ioException) {
            logger.info("KO-Le fichier est corrompu");
            throw new DataRepositoryException("KO-Le fichier est corrompu" + JSON_FILE, ioException);
        }
    }

    // On save tout les changements de database dans le JSON
    public void commit() {
        if (commit) {
            // Recuperation du chemin du JSON
            URL url = ClassLoader.getSystemResource(JSON_FILE);
            try (OutputStream outputStream = new FileOutputStream(url.getFile())) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, database);
                logger.info("Fichier mise a jour OK" + JSON_FILE);

            } catch (FileNotFoundException fnfe) {
                logger.info("Erreur : Fichier introuvable " + JSON_FILE);
                throw new DataRepositoryException("KO - FILE_NOT_FOUND : ", fnfe);

            } catch (IOException ioe) {
                logger.info("Erreur : I/O exception" + JSON_FILE);
                throw new DataRepositoryException("KO - I/O ERREUR : ", ioe);

            }
        }
    }

    /**
     * @return Retourne la liste complete des personnes
     */
    public List<Person> getAllPersons() {
        return database.getPersons();
    }

    /**
     * @param
     * @return Retourne la liste complete des firestation
     */
    public List<FireStation> getListFireStation() {
        return database.getFirestations();
    }

    /**
     * @param
     * @return Retourne la liste complete des medicalRecords
     */
    public List<MedicalRecord> getListMedicalRecord() {
        return database.getMedicalrecords();
    }

    /**
     * @param
     * @return Retourne la liste complete des personnes  par villes
     */
    public List<Person> getPersonByCity(String city) {
        return database.getPersons().stream().filter(person -> person.getCity().equalsIgnoreCase(city)).collect(Collectors.toList());
    }

    /**
     * @param
     * @return Retourne la liste complete des personnes  par adresse
     */
    public List<Person> getPersonByAddress(String address) {
        return database.getPersons().stream().filter(person -> person.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());
    }

    /**
     * @param
     * @return Retourne la liste complete des personnes  par nom de famille
     */
    public List<Person> getFamilyMemberByLastName(String lastName) {
        return database.getPersons().stream().filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }
    /**
     * @param
     * @return Retourne la liste complete des personnes  par nom et prenom
     */
    public List<Person> getPersonByLastNameAndFirsName(String lastName, String firstName) {
        return database.getPersons().stream().filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    /**
     * @param
     * @return Retourne un medicalRecord  par nom et prenom
     */
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

    /**
     * @param
     * @return Retourne la liste complete des station par adresse
     */
    public FireStation getStationByAddress(String address) {
        for (FireStation firestation : database.getFirestations()) {
            if (firestation.getAddress().equalsIgnoreCase(address)) {
                return firestation;
            }
        }
        return null;

    }

    /**
     * @param
     * @return Retourne une personne avec nom et prenom
     */
    public Person getPersonByName(String firstName, String lastName) {
        Person personResult = new Person();
        for (Person person : database.getPersons()) {
            if (person.getFirstName().equalsIgnoreCase(firstName) && person.getLastName().equalsIgnoreCase(lastName)) {
                personResult = person;
            }
        }
        return personResult;
    }

    /**
     * @param
     * @return Retourne toutes les stations
     */
    public List<FireStation> getAllStation() {
        return database.getFirestations();
    }

    public List<FireStation> getFirestationByStation(String firestation) {

        return database.getFirestations().stream()
                .filter(fireStation -> fireStation.getStation().equalsIgnoreCase(firestation))
                .collect(Collectors.toList());

    }

    /**
     * @param
     * @return Retourne les firestation par numero
     */
    public List<String> getListFireStation(List<String> stationNumber) {
        return database.getFirestations().stream()
                .filter(fireStation -> stationNumber.contains(fireStation.getStation())).map(FireStation::getAddress)
                .collect(Collectors.toList());
    }




}
