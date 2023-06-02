package learn.field_agent.domain;

import learn.field_agent.data.AgentRepository;
import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Agent;
import learn.field_agent.models.Alias;
import learn.field_agent.models.Location;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AliasServiceTest {

    @MockBean
    AliasRepository repository;

    @MockBean
    AgentRepository agentRepository;

    @Autowired
    AliasService service;


    @Test
    void shouldNotAddWhenInvalid() {

        Alias mockOut = makeAlias();
        mockOut.setAliasId(3);
        when(repository.add(any())).thenReturn(mockOut);

        Alias alias = makeAlias();
        alias.setName(null);

        Result<Alias> actual = service.add(alias);
        assertNull(actual.getPayload());
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().stream().anyMatch(m -> m.equals("name is required")));
        assertTrue(actual.getMessages().stream().anyMatch(m -> m.equals("agent does not exist")));

    }


    @Test
    void shouldNotAddDuplicateNameAndPersona() {
        Alias alias = makeAlias();

        Agent mockAgent = makeAgent();
        mockAgent.setAliases(List.of(makeAlias()));
        when(agentRepository.findById(1)).thenReturn(mockAgent);

        Result<Alias> actual = service.add(alias);

        assertNull(actual.getPayload());
        assertEquals(ResultType.INVALID, actual.getType());

    }


    @Test
    void shouldAdd() {
        Alias alias = makeAlias();
        Alias mockOut = makeAlias();
        mockOut.setAliasId(3);

        Agent mockAgent = makeAgent();
        when(agentRepository.findById(1)).thenReturn(mockAgent);
        when(repository.add(alias)).thenReturn(mockOut);

        Result<Alias> actual = service.add(alias);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    private Alias makeAlias() {
        Alias alias = new Alias();
        alias.setName("Nutmeg");
        alias.setAgentId(1);
        return alias;
    }

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