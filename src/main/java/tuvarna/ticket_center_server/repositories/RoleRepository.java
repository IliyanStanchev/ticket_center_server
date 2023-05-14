package tuvarna.ticket_center_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tuvarna.ticket_center_common.enumerables.RoleTypes;
import tuvarna.ticket_center_server.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r FROM ROLES r WHERE r.roleType = ?1")
    Role findByRoleType(RoleTypes roleType);
}
