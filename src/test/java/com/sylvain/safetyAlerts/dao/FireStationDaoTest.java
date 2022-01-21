package com.sylvain.safetyAlerts.dao;

import com.sylvain.safetyAlerts.models.FireStation;
import com.sylvain.safetyAlerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FireStationDaoTest {
    FireStation fireStation = new FireStation("place gambetta","17");
    FireStation fireStationAddress = new FireStation("place gambetta","");
    FireStation fireStationStation = new FireStation("","17");

    @Autowired
    FireStationDaoImpl firestationDao;

    @Autowired
    DataRepository dataRepository;

    @BeforeEach
    void init(){
        dataRepository.init();
        dataRepository.setCommit(false);
    }

    @Test
    void createFireStation() {
        assertThat(firestationDao.createFireStation(fireStation)).isTrue();
        assertThat(dataRepository.getAllStation().contains(fireStation)).isTrue();
    }

    @Test
    void updateFireStation() {
        assertThat(firestationDao.updateFireStation(fireStation)).isFalse();
        firestationDao.createFireStation(fireStation);
        assertThat(firestationDao.updateFireStation(fireStation)).isTrue();

    }

    @Test
    void deleteFireStation() {
        assertThat(firestationDao.createFireStation(fireStation)).isTrue();
        assertThat(dataRepository.getAllStation().contains(fireStation)).isTrue();
        assertThat(firestationDao.deleteFireStation(fireStation)).isTrue();
        assertThat(dataRepository.getAllStation().contains(fireStation)).isFalse();
    }

    @Test
    void deleteFireStationAddressNull() {
        assertThat(firestationDao.createFireStation(fireStation)).isTrue();
        assertThat(dataRepository.getAllStation().contains(fireStation)).isTrue();
        assertThat(firestationDao.deleteFireStation(fireStationAddress)).isTrue();
        assertThat(dataRepository.getAllStation().contains(fireStation)).isFalse();
    }

    @Test
    void deleteFireStationStationNull() {
        assertThat(firestationDao.createFireStation(fireStation)).isTrue();
        assertThat(dataRepository.getAllStation().contains(fireStation)).isTrue();
        assertThat(firestationDao.deleteFireStation(fireStationStation)).isTrue();
        assertThat(dataRepository.getAllStation().contains(fireStation)).isFalse();
    }
}
