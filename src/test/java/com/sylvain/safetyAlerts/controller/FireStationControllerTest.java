package com.sylvain.safetyAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.sylvain.safetyAlerts.exception.DataAlreadyExistException;
import com.sylvain.safetyAlerts.exception.DataNotFoundException;
import com.sylvain.safetyAlerts.exception.InvalidArgumentException;
import com.sylvain.safetyAlerts.service.IFireStationService;
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
public class FireStationControllerTest {
    @Mock
    FireStationController fireStationController = new FireStationController();

    @Autowired
    MockMvc mockMvc;

    String StationTest = "11";
    String AddressTest = "10 rue jean duvert";

    @MockBean
    IFireStationService firestationService;

    @Test
    void createFireStationValid() throws Exception {
        ObjectMapper obm = new ObjectMapper();
        ObjectNode jsonFireStation = obm.createObjectNode();

        jsonFireStation.set("station", TextNode.valueOf(StationTest));// Attribution de la caserne StationTest
        jsonFireStation.set("address", TextNode.valueOf(AddressTest));// Attribution de l'adresse AddressTest

        mockMvc.perform(MockMvcRequestBuilders.post("/firestation")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonFireStation.toString()))
            .andExpect(MockMvcResultMatchers.status().isCreated()); // Verification qu'elle est bien créer
    }

    @Test
    void createFireStationInvalid() throws Exception {

        ObjectMapper obm = new ObjectMapper();
        ObjectNode jsonFireStation = obm.createObjectNode();

        //GIven
        jsonFireStation.set("station", TextNode.valueOf(""));
        jsonFireStation.set("address", TextNode.valueOf(AddressTest));

        Mockito.doThrow(InvalidArgumentException.class)
                .when(firestationService).createFireStation(Mockito.any());
        //When
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFireStation.toString()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());// On vérfie qu'on a un mauvais status de requete
    }

    @Test
    void createFireStationWhenFireStationAlreadyExist() throws Exception {

        ObjectMapper obm = new ObjectMapper();
        ObjectNode jsonFireStation = obm.createObjectNode();

        //Given
        jsonFireStation.set("station", TextNode.valueOf(StationTest));
        jsonFireStation.set("address", TextNode.valueOf(AddressTest));

        Mockito.doThrow(DataAlreadyExistException.class)
                .when(firestationService).createFireStation(Mockito.any());
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFireStation.toString()))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    void updateFireStationValid() throws Exception {

        ObjectMapper obm = new ObjectMapper();
        ObjectNode jsonFireStation = obm.createObjectNode();

        // GIVEN

        jsonFireStation.set("station", TextNode.valueOf(StationTest));
        jsonFireStation.set("address", TextNode.valueOf(AddressTest));

        mockMvc.perform(MockMvcRequestBuilders.put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFireStation.toString()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    // @Test
    void updateFireStationInvalid() throws Exception {

        ObjectMapper obm = new ObjectMapper();
        ObjectNode jsonFireStation = obm.createObjectNode();

        // GIVEN

        // jsonFireStation.set("station", TextNode.valueOf(""));
        // jsonFireStation.set("address", TextNode.valueOf(""));

        // WHEN
        // THEN
        mockMvc.perform(MockMvcRequestBuilders.put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFireStation.toString()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateFireStationWhenFireStationNotFound() throws Exception {

        Mockito.doThrow(DataNotFoundException.class).when(firestationService)
                .updateFireStation(Mockito.any());

        // GIVEN

        ObjectMapper obm = new ObjectMapper();
        ObjectNode jsonFireStation = obm.createObjectNode();

        // WHEN
        // THEN

        jsonFireStation.set("station", TextNode.valueOf(StationTest));
        jsonFireStation.set("address", TextNode.valueOf(AddressTest));

        mockMvc.perform(MockMvcRequestBuilders.put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFireStation.toString()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // --------------- SUPPRESSION DE CASERNES -----------------

    @Test
    void deleteFireStationValid() throws Exception {

        ObjectMapper obm = new ObjectMapper();
        ObjectNode jsonFireStation = obm.createObjectNode();

        // GIVEN

        jsonFireStation.set("station", TextNode.valueOf(StationTest));
        jsonFireStation.set("address", TextNode.valueOf(AddressTest));

        // WHEN
        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFireStation.toString()))
                .andExpect(MockMvcResultMatchers.status().isResetContent());
    }

    void deleteFireStationWhenIsNotFound() throws Exception{

        ObjectMapper obm = new ObjectMapper();
        ObjectNode jsonFireStation = obm.createObjectNode();

        jsonFireStation.set("station", TextNode.valueOf(StationTest));
        jsonFireStation.set("address", TextNode.valueOf(AddressTest));

        mockMvc.perform(MockMvcRequestBuilders.delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFireStation.toString()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
}
