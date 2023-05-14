package tuvarna.ticket_center_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tuvarna.ticket_center_server.models.PurchasedTicket;

import java.util.List;

public interface PurchasedTicketRepository extends JpaRepository<PurchasedTicket, Long> {

    @Query("SELECT p FROM PURCHASED_TICKETS p WHERE p.ticket.ticketKind.event.id = ?1 AND p.ticket.ticketKind.name = ?2")
    List<PurchasedTicket> getPurchasedTickets(Long eventId, String ticketKindName);

    @Query("SELECT p FROM PURCHASED_TICKETS p WHERE p.ticket.ticketKind.event.id = ?1")
    List<PurchasedTicket> getPurchasedTicketsForEvent(Long id);

    @Query("SELECT p FROM PURCHASED_TICKETS p WHERE p.ticket.id = ?1")
    PurchasedTicket findByTicket(Long id);
}
