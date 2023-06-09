package learn.field_agent.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import learn.field_agent.data.AgentRepository;
import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Agent;
import learn.field_agent.models.Alias;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;

@SpringBootTest
@AutoConfigureMockMvc
class AliasControllerTest {

    @MockBean
    AliasRepository repository;

    @MockBean
    AgentRepository agentRepository;

    @Autowired
    MockMvc mvc;


//    @Test
//    void addShouldReturn201() throws Exception {
//        Alias alias = new Alias(0, "Nutmeg", null, 1);
//        Alias aliasOut = new Alias(3, "Nutmeg", null, 1);
//
//        Agent agent = new Agent();
//        agent.setAgentId(1);
//
//        when(agentRepository.findById(1)).thenReturn(agent);
//        when(repository.add(any())).thenReturn(aliasOut);
//
//        ObjectMapper jsonMapper = new ObjectMapper();
//        String jsonIn = jsonMapper.writeValueAsString(alias);
//        String expectedJson = jsonMapper.writeValueAsString(alias);
//
//        var request = post("/api/alias")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonIn);
//
//        mvc.perform(request)
//                .andExpect(status().isCr)
//
//    }

    private Agent makeAgent() {
        //('Hazel','C','Sauven','1954-09-16',76),
        Agent agent = new Agent();
        agent.setAgentId(1);
        agent.setFirstName("Hazel");
        agent.setMiddleName("C");
        agent.setLastName("Sauven");
        agent.setDob(LocalDate.of(1954, 9, 16));
        agent.setHeightInInches(76);


        return agent;
    }


}