package learn.field_agent.domain;

import learn.field_agent.data.AgentRepository;
import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Agent;
import learn.field_agent.models.Alias;
import org.springframework.stereotype.Service;

@Service
public class AliasService {
    private final AliasRepository aliasRepository;
    private final AgentRepository agentRepository;


    public AliasService(AliasRepository aliasRepository, AgentRepository agentRepository) {
        this.aliasRepository = aliasRepository;
        this.agentRepository = agentRepository;
    }

    public Result<Alias> add(Alias alias){
        Result<Alias> result = validate(alias);
        if(!result.isSuccess()) {
            return result;
        }

        result.setPayload(aliasRepository.add(alias));

        return result;

    }

    public Result<Alias> update(Alias alias) {
        Result<Alias> result = validate(alias);
        if (!result.isSuccess()) {
            return result;
        }
        if (alias.getAliasId() <= 0) {
            result.addMessage("aliasId cannot be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if(!aliasRepository.update(alias)) {
            result.addMessage(String.format("aliasId: %s, not found", alias.getAliasId()), ResultType.NOT_FOUND);
        }



        return result;
    }

    public boolean deleteById(int aliasId) {
        return aliasRepository.deleteById(aliasId);
    }

    private Result<Alias> validate(Alias alias) {
        Result<Alias> result = new Result<>();

        if(Validations.isNullOrBlank(alias.getName())) {
            result.addMessage("name is required", ResultType.INVALID);
        }
        Agent agent = agentRepository.findById(alias.getAgentId());
        if(agent == null) {
            result.addMessage("agent does not exist", ResultType.INVALID);
            return result;
        }
        if(isDuplicate(alias, agent)){
            result.addMessage("name and persona is already exist", ResultType.INVALID);
        }


        return result;
    }

    private boolean isDuplicate(Alias alias, Agent agent){
        for(Alias existingAlias : agent.getAliases()){
            if(existingAlias.getAliasId() == alias.getAliasId()) {
                continue;
            }
            if(existingAlias.getName().equalsIgnoreCase(alias.getName())){
                if(existingAlias.getPersona() == null && alias.getPersona() == null) {
                    return true;
                } else if(existingAlias.getPersona() != null && existingAlias.getPersona().equalsIgnoreCase(alias.getPersona())){
                    return true;
                }
            }
        }
        return false;
    }


}
