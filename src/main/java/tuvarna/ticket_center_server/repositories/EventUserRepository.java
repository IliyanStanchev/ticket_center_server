package tuvarna.ticket_center_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tuvarna.ticket_center_common.enumerables.RoleTypes;
import tuvarna.ticket_center_server.models.EventUser;

import java.util.List;

@Repository
public interface EventUserRepository extends JpaRepository<EventUser, Long> {

    @Query("SELECT u FROM EVENT_USERS u WHERE u.user.email = ?1")
    EventUser findByEmail(String email);

    @Query("SELECT u FROM EVENT_USERS u WHERE u.user.role.roleType = ?1")
    List<EventUser> getUsersByRole(RoleTypes roleType);
}
