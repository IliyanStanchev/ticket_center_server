package tuvarna.ticket_center_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tuvarna.ticket_center_server.models.Ticket;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {


    @Query("SELECT t FROM TICKETS t WHERE t.ticketKind.id = ?1")
    List<Ticket> getTickets(Long ticketKindId);

    @Query("SELECT t FROM TICKETS t WHERE t.ticketKind.event.id = ?1 AND t.ticketKind.name = ?2")
    List<Ticket> getFreeTickets(Long eventId, String ticketKindName);
}
