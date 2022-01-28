package com.sylvain.safetyAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.sylvain.safetyAlerts.exception.DataAlreadyExistException;
import com.sylvain.safetyAlerts.exception.DataNotFoundException;
import com.sylvain.safetyAlerts.models.MedicalRecord;
import com.sylvain.safetyAlerts.service.IMedicalRecordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordsControllerTest {

    @Mock
    MedicalRecord medicalRecord = new MedicalRecord();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IMedicalRecordService medicalrecordService;

    String firstName = "Jhon";
    String lastName = "Doe";
    String birthdate = "12/07/1999";

    @Test
    void createMedicalRecordValid() throws Exception {

        ObjectMapper obm = new ObjectMapper();
        ObjectNode jsonMedicalRecord = obm.createObjectNode();

        jsonMedicalRecord.set("firstName", TextNode.valueOf(firstName));
        jsonMedicalRecord.set("lastName", TextNode.valueOf(lastName));
        jsonMedicalRecord.set("birthdate", TextNode.valueOf(birthdate));


        mockMvc.perform(MockMvcRequestBuilders.post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMedicalRecord.toString()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void createFireStationInvalid() throws Exception {

        ObjectMapper obm = new ObjectMapper();
        ObjectNode jsonMedicalRecord = obm.createObjectNode();

        // Le firstName est vide
        jsonMedicalRecord.set("firstName", TextNode.valueOf(""));
        jsonMedicalRecord.set("lastName", TextNode.valueOf(lastName));

        // Donc on s'attend à une requete de type isBadRequest
        mockMvc.perform(MockMvcRequestBuilders.post("/medicalRecord").contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMedicalRecord.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createMedicalrecordWhenMedicalrecordAlreadyExist() throws Exception {

        // Mock de MedicalrecordService et on lui dit de renvoyer l'exception
        // DataAlreadExist
        // quand on lui demande de renvoyer un Medicalrecord existant

        Mockito.doThrow(DataAlreadyExistException.class).when(medicalrecordService).createMedicalRecord(Mockito.any());

        ObjectMapper obm = new ObjectMapper();
        ObjectNode jsonMedicalrecord = obm.createObjectNode();

        jsonMedicalrecord.set("firstName", TextNode.valueOf(firstName));
        jsonMedicalrecord.set("lastName", TextNode.valueOf(lastName));

        mockMvc.perform(MockMvcRequestBuilders.post("/Medicalrecord")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonMedicalrecord.toString()))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateMedicalrecordValid() throws Exception {

        ObjectMapper obm = new ObjectMapper();
        ObjectNode jsonMedicalrecord = obm.createObjectNode();


        jsonMedicalrecord.set("firstName", TextNode.valueOf("John"));
        jsonMedicalrecord.set("lastName", TextNode.valueOf("Boyd"));
        jsonMedicalrecord.set("birthdate", TextNode.valueOf(birthdate));

        mockMvc.perform(MockMvcRequestBuilders.put("/Medicalrecord").contentType(MediaType.APPLICATION_JSON)
                .content(jsonMedicalrecord.toString()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateMedicalrecordInvalid() throws Exception {

        ObjectMapper obm = new ObjectMapper();
        ObjectNode jsonMedicalrecord = obm.createObjectNode();

        jsonMedicalrecord.set("firstName", TextNode.valueOf(""));
        jsonMedicalrecord.set("lastName", TextNode.valueOf(""));

        mockMvc.perform(MockMvcRequestBuilders.put("/Medicalrecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMedicalrecord.toString()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateMedicalrecordWhenMedicalrecordNotFound() throws Exception {

        Mockito.doThrow(DataNotFoundException.class).when(medicalrecordService)
                .updateMedicalRecord(Mockito.any());

        ObjectMapper obm = new ObjectMapper();
        ObjectNode jsonMedicalrecord = obm.createObjectNode();

        jsonMedicalrecord.set("firstName", TextNode.valueOf(firstName));
        jsonMedicalrecord.set("lastName", TextNode.valueOf(lastName));

        mockMvc.perform(MockMvcRequestBuilders.put("/Medicalrecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMedicalrecord.toString()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteMedicalrecordValid() throws Exception {

        ObjectMapper obm = new ObjectMapper();
        ObjectNode jsonMedicalrecord = obm.createObjectNode();

        jsonMedicalrecord.set("firstName", TextNode.valueOf(firstName));
        jsonMedicalrecord.set("lastName", TextNode.valueOf(lastName));

        mockMvc.perform(MockMvcRequestBuilders.delete("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMedicalrecord.toString()))
                .andExpect(MockMvcResultMatchers.status().isResetContent());
    }

}
