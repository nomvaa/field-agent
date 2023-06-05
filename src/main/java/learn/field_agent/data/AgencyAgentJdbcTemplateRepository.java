package learn.field_agent.data;

import learn.field_agent.data.mappers.AgencyAgentMapper;
import learn.field_agent.models.AgencyAgent;
import learn.field_agent.models.SecurityClearance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AgencyAgentJdbcTemplateRepository implements AgencyAgentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SecurityClearanceRepository securityClearanceRepository;

    public AgencyAgentJdbcTemplateRepository(JdbcTemplate jdbcTemplate, SecurityClearanceRepository securityClearanceRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.securityClearanceRepository = securityClearanceRepository;
    }

    @Override
    public SecurityClearance findBySecurityClearanceId(int securityClearanceId){
        return securityClearanceRepository.findById(securityClearanceId);
    }

    @Override
    public List<AgencyAgent> findAll() {
        final String sql = "select agency_id, agent_id, identifier, security_clearance_id, activation_date, is_active "
                + "from agency_agent;";
        return jdbcTemplate.query(sql, new AgencyAgentMapper());
    }

    @Override
    public boolean add(AgencyAgent agencyAgent) {

        final String sql = "insert into agency_agent (agency_id, agent_id, identifier, security_clearance_id, "
                + "activation_date, is_active) values "
                + "(?,?,?,?,?,?);";

        return jdbcTemplate.update(sql,
                agencyAgent.getAgencyId(),
                agencyAgent.getAgent().getAgentId(),
                agencyAgent.getIdentifier(),
                agencyAgent.getSecurityClearance().getSecurityClearanceId(),
                agencyAgent.getActivationDate(),
                agencyAgent.isActive()) > 0;
    }

    @Override
    public boolean update(AgencyAgent agencyAgent) {

        final String sql = "update agency_agent set "
                + "identifier = ?, "
                + "security_clearance_id = ?, "
                + "activation_date = ?, "
                + "is_active = ? "
                + "where agency_id = ? and agent_id = ?;";

        return jdbcTemplate.update(sql,
                agencyAgent.getIdentifier(),
                agencyAgent.getSecurityClearance().getSecurityClearanceId(),
                agencyAgent.getActivationDate(),
                agencyAgent.isActive(),
                agencyAgent.getAgencyId(),
                agencyAgent.getAgent().getAgentId()) > 0;

    }

    @Override
    public boolean deleteByKey(int agencyId, int agentId) {

        final String sql = "delete from agency_agent "
                + "where agency_id = ? and agent_id = ?;";

        return jdbcTemplate.update(sql, agencyId, agentId) > 0;
    }
}
