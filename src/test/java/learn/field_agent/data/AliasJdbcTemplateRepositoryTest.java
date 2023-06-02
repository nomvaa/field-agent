package learn.field_agent.data;

import learn.field_agent.models.Alias;
import learn.field_agent.models.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AliasJdbcTemplateRepositoryTest {

    final static int NEXT_ALIAS_ID = 2;
    @Autowired
    AliasJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }


    @Test
    void shouldAdd() {
        Alias alias = new Alias(0, "Bob", null, 1);
        Alias actual = repository.add(alias);
        assertNotNull(actual);
        assertEquals(NEXT_ALIAS_ID, actual.getAliasId());
    }

    @Test
    void shouldUpdate(){

    }

    @Test
    void shouldDelete(){

    }
}