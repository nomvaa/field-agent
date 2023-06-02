package learn.field_agent.data;

import learn.field_agent.models.Alias;
import learn.field_agent.models.Location;

public interface AliasRepository {

    Alias add(Alias alias);

    boolean update(Alias alias);

    boolean deleteById(int aliasId);
}

