package tuvarna.ticket_center_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tuvarna.ticket_center_server.models.TicketKind;

import java.util.List;

@Repository
public interface TicketKindRepository extends JpaRepository<TicketKind, Long> {

    @Query("SELECT tk FROM TICKET_KINDS tk WHERE tk.event.id = ?1")
    List<TicketKind> getTicketKinds(Long eventId);
}
