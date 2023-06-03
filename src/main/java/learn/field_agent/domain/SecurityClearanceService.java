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
            result.addMessage("securityClearnanceId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        securityClearance = securityClearanceRepository.add(securityClearance);
        result.setPayload(securityClearance);
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

    public boolean deleteById(int securityClearanceId){

        return securityClearanceRepository.deleteById(securityClearanceId);
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
//            if(existing.getSecurityClearanceId() == securityClearance.getSecurityClearanceId()){
//                return true;
//            }
        }
        return false;
    }


}
