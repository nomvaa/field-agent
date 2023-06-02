package learn.field_agent.data;

import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceJdbcTemplateRepositoryTest {

    final static int NEXT_ID = 5;

    @Autowired
    SecurityClearanceJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindById() {
        SecurityClearance secret = new SecurityClearance(1, "Secret");
        SecurityClearance topSecret = new SecurityClearance(2, "Top Secret");

        SecurityClearance actual = repository.findById(1);
        assertEquals(secret, actual);

        actual = repository.findById(2);
        assertEquals(topSecret, actual);

        actual = repository.findById(NEXT_ID + 1);
        assertEquals(null, actual);
    }

    @Test
    void shouldFindAllSecurityClearance() {
        List<SecurityClearance> securityClearances = repository.findAll();
        assertNotNull(securityClearances);
        assertTrue(securityClearances.size() > 0);
    }

    @Test
    void shouldAddSecurityClearance() {
        SecurityClearance securityClearance = makeSecurityClearance();
        SecurityClearance actual = repository.add(securityClearance);
        assertNotNull(actual);
        assertEquals(NEXT_ID, actual.getSecurityClearanceId());

    }

    private SecurityClearance makeSecurityClearance() {
        SecurityClearance securityClearance = new SecurityClearance();
        securityClearance.setName("Bubbles");
        return securityClearance;
    }


}