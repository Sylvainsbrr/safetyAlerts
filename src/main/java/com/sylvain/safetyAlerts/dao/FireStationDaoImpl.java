package com.sylvain.safetyAlerts.dao;

import com.sylvain.safetyAlerts.models.FireStation;
import com.sylvain.safetyAlerts.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FireStationDaoImpl implements FireStationDao {
    @Autowired
    private DataRepository dataRepository;

    @Override
    public boolean createFireStation(FireStation fireStation) {
        // Ajout d'une nouvelle fireStation en memoire java
        boolean result = dataRepository.getAllStation().add(fireStation);
        // Applications des changements dans le json avec un commit
        dataRepository.commit();
        return result;
    }

    @Override
    public boolean updateFireStation(FireStation fireStation) {
        for (FireStation fs : dataRepository.getAllStation()) {

            if (fs.getAddress().contentEquals(fireStation.getAddress())) {
                boolean result = deleteFireStation(fs);
                if (result) {
                    result = createFireStation(fireStation);
                    dataRepository.commit();
                    return result;
                }
            }
        }
        return false;
    }

    @Override
    public boolean deleteFireStation(FireStation fireStation) {
        List<FireStation> fireStationListDeleted = new ArrayList<>();
        // Suppression par station et adresse
        if (!"".equals(fireStation.getStation()) && (fireStation.getStation() != null) && (!"".equals(fireStation.getAddress()) && (fireStation.getAddress() != null))) {
            for (FireStation fs : dataRepository.getDatabase().getFirestations()) {
                if (fs.getStation().contentEquals(fireStation.getStation()) && (fs.getAddress().contentEquals(fireStation.getAddress()))) {
                    fireStationListDeleted.add(fs);
                }
            }
        } else {
            // Suppression par adresse
            if (!"".equals(fireStation.getAddress()) && (fireStation.getAddress() != null)) {
                for (FireStation fs : dataRepository.getDatabase().getFirestations()) {
                    if (fs.getAddress().contentEquals(fireStation.getAddress())) {
                        fireStationListDeleted.add(fs);
                    }
                }
            }
            // Suppression par station
            if (!"".equals(fireStation.getStation()) && (fireStation.getStation() != null)) {
                for (FireStation fs : dataRepository.getDatabase().getFirestations()) {
                    if (fs.getStation().contentEquals(fireStation.getStation())) {
                        fireStationListDeleted.add(fs);
                    }
                }
            }
        }
        // On supprime la fireStation en memoire
        boolean result = dataRepository.getDatabase().getFirestations().removeAll(fireStationListDeleted);
        // Application des changements dans le json avec un commit
        dataRepository.commit();
        return result;
    }
}
