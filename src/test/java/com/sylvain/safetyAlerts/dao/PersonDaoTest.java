package com.sylvain.safetyAlerts.dao;
import static org.assertj.core.api.Assertions.assertThat;
import com.sylvain.safetyAlerts.models.Person;
import com.sylvain.safetyAlerts.repository.DataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonDaoTest {
    Person jhony = new Person("Jhony", "Doe", "raccon", "Washinton DC", "12345","54321" , "jhony@gmail.com");
    @Autowired
    PersonDaoImpl personDao;

    @Autowired
    DataRepository dataRepository;

    @BeforeEach
    void init(){
        dataRepository.init();
        dataRepository.setCommit(false);
    }

    @Test
    void createPerson() {
        Assertions.assertFalse(dataRepository.getAllPersons().contains(jhony));
        assertThat(personDao.createPerson(jhony)).isTrue();
        assertThat(dataRepository.getAllPersons().contains(jhony)).isTrue();
    }

    @Test
    void updatePerson() {
        assertThat(personDao.updatePerson(jhony)).isFalse();
        personDao.createPerson(jhony);
        assertThat(personDao.updatePerson(jhony)).isTrue();
    }


    @Test
    void deletePerson() {
        assertThat(personDao.deletePerson(jhony)).isFalse();
        assertThat(personDao.createPerson(jhony)).isTrue();
        assertThat(dataRepository.getAllPersons().contains(jhony)).isTrue();
        assertThat(personDao.deletePerson(jhony)).isTrue();
        assertThat(dataRepository.getAllPersons().contains(jhony)).isFalse();
    }
}
