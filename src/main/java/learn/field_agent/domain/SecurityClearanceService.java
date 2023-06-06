package learn.field_agent.domain;

import learn.field_agent.data.AgencyAgentRepository;
import learn.field_agent.data.SecurityClearanceRepository;

import learn.field_agent.models.AgencyAgent;

import learn.field_agent.models.SecurityClearance;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityClearanceService {

    private final SecurityClearanceRepository securityClearanceRepository;
    private final AgencyAgentRepository agencyAgentRepository;

    public SecurityClearanceService(SecurityClearanceRepository securityClearanceRepository, AgencyAgentRepository agencyAgentRepository) {
        this.securityClearanceRepository = securityClearanceRepository;
        this.agencyAgentRepository = agencyAgentRepository;
    }

    public List<SecurityClearance> findAll() {
        return securityClearanceRepository.findAll();
    }

    public SecurityClearance findById(int securityClearanceId) {

        return securityClearanceRepository.findById(securityClearanceId);
    }

    public Result<SecurityClearance> add(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = validate(securityClearance);
        if (!result.isSuccess()) {
            return result;
        }

        if (securityClearance.getSecurityClearanceId() != 0) {
            result.addMessage("securityClearanceId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        result.setPayload(securityClearanceRepository.add(securityClearance));

        return result;

    }

    public Result<SecurityClearance> update(SecurityClearance securityClearance){
        Result<SecurityClearance> result = validate(securityClearance);
        if (!result.isSuccess()) {
            return result;
        }

        if(securityClearance.getSecurityClearanceId() <= 0) {
            result.addMessage("securityClearanceId must be set for `update` operation", ResultType.INVALID);
            return result;
        }
        if(!securityClearanceRepository.update(securityClearance)) {
            String msg = String.format("securityClearanceId: %s, not found", securityClearance.getSecurityClearanceId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public Result<Void> deleteById(int securityClearanceId){
        Result<Void> result = new Result<>();

        if(!result.isSuccess()){
            return result;
        }

        if(!securityClearanceRepository.deleteById(securityClearanceId)) {
            String msg = String.format("securityClearanceId: %s, not found", securityClearanceId);
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        if(isReferenced(securityClearanceId)){
            result.addMessage("Cannot delete security clearance Id that exists in Agency_agent record", ResultType.INVALID);
            return result;
        }

        return result;
    }

    private Result<SecurityClearance> validate(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = new Result<>();

        if(securityClearance == null) {
            result.addMessage("security clearance cannot be null", ResultType.INVALID);
            return result;
        }

        if(isDuplicated(securityClearance)){
            result.addMessage("security clearance name already exist", ResultType.INVALID);
        }

        if(Validations.isNullOrBlank(securityClearance.getName())){
            result.addMessage("name is required", ResultType.INVALID);
        }


        return result;
    }

    private boolean isDuplicated(SecurityClearance securityClearance){
        for(SecurityClearance existing : securityClearanceRepository.findAll()){
            if(existing.getName().equalsIgnoreCase(securityClearance.getName())){
                return true;
            }
        }
        return false;
    }

    private boolean isReferenced(int securityClearanceId){
        for(AgencyAgent o : agencyAgentRepository.findAll()){
            if(o.getSecurityClearance().getSecurityClearanceId() == securityClearanceId){
                return true;
            }
        }

        return false;
    }

}
