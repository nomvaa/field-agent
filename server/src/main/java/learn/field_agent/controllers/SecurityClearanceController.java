package learn.field_agent.controllers;

import learn.field_agent.domain.AgencyService;
import learn.field_agent.domain.Result;
import learn.field_agent.domain.SecurityClearanceService;
import learn.field_agent.models.AgencyAgent;
import learn.field_agent.models.SecurityClearance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/security")
public class SecurityClearanceController {

    private final SecurityClearanceService service;
    private final AgencyService agencyService;


    public SecurityClearanceController(SecurityClearanceService service, AgencyService agencyService) {
        this.service = service;
        this.agencyService = agencyService;
    }

    @GetMapping
    public List<SecurityClearance> findAll(){
        return service.findAll();
    }

    @GetMapping("{securityClearanceId}")
    public SecurityClearance findById(@PathVariable int securityClearanceId) {
        return service.findById(securityClearanceId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody SecurityClearance securityClearance){
        Result<SecurityClearance> result = service.add(securityClearance);
        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }

    @PutMapping("/{securityClearanceId}")
    public ResponseEntity<Object> update(@PathVariable int securityClearanceId, @RequestBody SecurityClearance securityClearance) {
        if(securityClearanceId != securityClearance.getSecurityClearanceId()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<SecurityClearance> result = service.update(securityClearance);
        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{securityClearanceId}")
    public ResponseEntity<Object> deleteById(@PathVariable int securityClearanceId){
        Result<Void> result = service.deleteById(securityClearanceId);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

}
