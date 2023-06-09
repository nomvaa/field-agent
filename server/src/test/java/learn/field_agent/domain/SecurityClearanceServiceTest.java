package learn.field_agent.domain;

import learn.field_agent.data.AgencyAgentRepository;
import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.Agency;
import learn.field_agent.models.AgencyAgent;
import learn.field_agent.models.Agent;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceServiceTest {

    @Autowired
    SecurityClearanceService service;

    @MockBean
    SecurityClearanceRepository repository;

    @MockBean
    AgencyAgentRepository agencyAgentRepository;

    @Test
    void shouldFindBubbles() {
        SecurityClearance expected = makeSecurityClearance();
        SecurityClearance arg = makeSecurityClearance();
        arg.setSecurityClearanceId(0);

        when(repository.add(arg)).thenReturn(expected);
        Result<SecurityClearance> result = service.add(arg);
        assertEquals(ResultType.SUCCESS, result.getType());

        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() {
        SecurityClearance mockOut = makeSecurityClearance();
        mockOut.setSecurityClearanceId(3);
        when(repository.add(any())).thenReturn(mockOut);

        SecurityClearance securityClearance = makeSecurityClearance();
        securityClearance.setName(null);

        Result<SecurityClearance> actual = service.add(securityClearance);
        assertNull(actual.getPayload());

    }

    @Test
    void shouldNotAddDuplicateName() {
        SecurityClearance mockClearance = makeSecurityClearance();
        SecurityClearance duplicate = makeSecurityClearance();
        duplicate.setName("Bubbles");

        mockClearance.setName(duplicate.getName());
        when(repository.findById(1)).thenReturn(mockClearance);

        Result<SecurityClearance> actual = service.add(mockClearance);
        assertNull(actual.getPayload());
    }

    @Test
    void shouldNotUpdateMissingSecurityClearance() {
        SecurityClearance securityClearance = makeSecurityClearance();
        securityClearance.setSecurityClearanceId(99);
        when(repository.update(securityClearance)).thenReturn(false);

        Result<SecurityClearance> actual = service.update(securityClearance);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldUpdate() {
        SecurityClearance securityClearance = makeSecurityClearance();
        securityClearance.setSecurityClearanceId(1);
        when(repository.update(securityClearance)).thenReturn(true);

        Result<SecurityClearance> actual = service.update(securityClearance);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldDeleteSecurityClearanceIdThatDoesNotExistInAgencyAgentDatabase(){

        Result<Void> result = service.deleteById(2);
        assertFalse(result.isSuccess());

    }

    private SecurityClearance makeSecurityClearance() {
        SecurityClearance securityClearance = new SecurityClearance();
        securityClearance.setName("Bubbles");
        return securityClearance;
    }

    private AgencyAgent makeAgencyAgent(){
        Agency agency = new Agency(0, "TEST", "Long Name Test");
        Agent agent = new Agent();
        agent.setAgentId(1);
        agent.setFirstName("Hazel");
        agent.setMiddleName("C");
        agent.setLastName("Sauven");
        agent.setDob(LocalDate.of(1954, 9, 16));
        agent.setHeightInInches(76);

        AgencyAgent agencyAgent = new AgencyAgent();
        agencyAgent.setAgent(agent);
        agencyAgent.setAgencyId(agency.getAgencyId());
        agencyAgent.setIdentifier(String.format("%s-%s",agencyAgent.getAgencyId(),agencyAgent.getAgent().getAgentId()));
        agencyAgent.setSecurityClearance(agencyAgent.getSecurityClearance());
        agencyAgent.setActivationDate(agencyAgent.getActivationDate());
        agencyAgent.setActive(agencyAgent.isActive());
        return agencyAgent;
    }

}