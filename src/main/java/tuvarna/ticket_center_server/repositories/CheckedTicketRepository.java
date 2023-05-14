package tuvarna.ticket_center_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tuvarna.ticket_center_server.models.CheckedTicket;

public interface CheckedTicketRepository extends JpaRepository<CheckedTicket, Long> {

    @Query("SELECT c FROM CHECKED_TICKETS c WHERE c.ticket.id = ?1")
    CheckedTicket findByTicket(Long ticketId);
}
