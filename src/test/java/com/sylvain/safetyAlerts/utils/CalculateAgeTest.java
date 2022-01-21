package com.sylvain.safetyAlerts.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class CalculateAgeTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void Calculate() throws Exception {
        int age;
        String ageParam = "24/08/2003";
        age = CalculateAge.getAge(ageParam);
        assertEquals(18, age);
        System.out.println(age);
    }

    @Test
    void Mineur() throws Exception{
        boolean age;
        String ageParam = "24/08/2011";
        age = CalculateAge.isMineur(ageParam);
        assertEquals(true, age);
    }

}
