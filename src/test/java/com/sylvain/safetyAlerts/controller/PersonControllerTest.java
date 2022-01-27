package com.sylvain.safetyAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.sylvain.safetyAlerts.exception.DataAlreadyExistException;
import com.sylvain.safetyAlerts.service.IPersonService;
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
public class PersonControllerTest {
    @Mock
    PersonController personController = new PersonController();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IPersonService iPersonService;

    String firstName = "John";
    String lastName = "Doe";
    String address = "124 rue de gambetta";
    String city = "Bordeaux";
    String zip = "33000";
    String phone = "666-123-456";
    String email = "john@gmail.com";

    @Test
    void createPersonValid() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonPerson = objectMapper.createObjectNode();
        jsonPerson.set("firstName", TextNode.valueOf(firstName));
        jsonPerson.set("lastName", TextNode.valueOf(lastName));

        mockMvc.perform(MockMvcRequestBuilders.post("/person") // requete
                .contentType(MediaType.APPLICATION_JSON).content(jsonPerson.toString())) // contenu
                .andExpect(MockMvcResultMatchers.status().isCreated()); // test
    }

    @Test
    void createPersonInvalid() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonPerson = objectMapper.createObjectNode();
        jsonPerson.set("firstName", TextNode.valueOf(firstName));
        jsonPerson.set("lastName", TextNode.valueOf(""));

        mockMvc.perform(MockMvcRequestBuilders.post("/person") // requete
                .contentType(MediaType.APPLICATION_JSON).content(jsonPerson.toString())) // contenu
                .andExpect(MockMvcResultMatchers.status().isBadRequest()); // test
    }

    @Test
    void createPersonAlreadyExist() throws Exception {

        Mockito.doThrow(DataAlreadyExistException.class).when(iPersonService).createPerson(Mockito.any());
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonPerson = objectMapper.createObjectNode();
        jsonPerson.set("firstName", TextNode.valueOf(firstName));
        jsonPerson.set("lastName", TextNode.valueOf("lastName"));

        mockMvc.perform(MockMvcRequestBuilders.post("/person") // requete
                .contentType(MediaType.APPLICATION_JSON).content(jsonPerson.toString())) // contenu
                .andExpect(MockMvcResultMatchers.status().isConflict()); // test
    }

    @Test
    void updatePersonValid() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonPerson = objectMapper.createObjectNode();
        jsonPerson.set("firstName", TextNode.valueOf(firstName));
        jsonPerson.set("lastName", TextNode.valueOf(lastName));

        mockMvc.perform(MockMvcRequestBuilders.put("/person") //requete
                .contentType(MediaType.APPLICATION_JSON).content(jsonPerson.toString())) // contenu
                .andExpect(MockMvcResultMatchers.status().isNoContent()); // test
    }

    @Test
    void updatePersonInvalid() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonPerson = objectMapper.createObjectNode();
        jsonPerson.set("firstName", TextNode.valueOf(""));
        jsonPerson.set("lasName", TextNode.valueOf(""));

        mockMvc.perform(MockMvcRequestBuilders.put("/person")//requete
                .contentType(MediaType.APPLICATION_JSON).content(jsonPerson.toString())) // contenu
                .andExpect(MockMvcResultMatchers.status().isBadRequest()); // test
    }


    @Test
    void deletePersonValid() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonPerson = objectMapper.createObjectNode();
        jsonPerson.set("firstName", TextNode.valueOf(firstName));
        jsonPerson.set("lastName", TextNode.valueOf(lastName));

        mockMvc.perform(MockMvcRequestBuilders.delete("/person")//requete
                .contentType(MediaType.APPLICATION_JSON).content(jsonPerson.toString())) // contenu
                .andExpect(MockMvcResultMatchers.status().isResetContent()); // test
    }

    @Test
    void deletePersonInvalid() throws Exception {
        // on mock IPersonService on lui dit de renvoyer l'exception
        // DataAlreadyExist uniquement quand on lui demande de creer une personne
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonPerson = objectMapper.createObjectNode();
        jsonPerson.set("firstName", TextNode.valueOf(""));
        jsonPerson.set("lastName", TextNode.valueOf(""));

        mockMvc.perform(MockMvcRequestBuilders.delete("/person")//requete
                        .contentType(MediaType.APPLICATION_JSON).content(jsonPerson.toString())) // contenu
                .andExpect(MockMvcResultMatchers.status().isBadRequest()); // test
    }
}
