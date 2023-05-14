package tuvarna.ticket_center_server.services;

import tuvarna.ticket_center_common.models.EventModel;
import tuvarna.ticket_center_common.models.PurchaseTicketModel;
import tuvarna.ticket_center_common.models.TicketModel;
import tuvarna.ticket_center_server.data.EventStatistic;
import tuvarna.ticket_center_server.models.AssignedDistributor;
import tuvarna.ticket_center_server.models.Event;
import tuvarna.ticket_center_server.models.EventUser;
import tuvarna.ticket_center_server.models.PurchasedTicket;

import java.util.List;

public interface EventService {

    boolean createEvent(EventUser organizer, EventModel eventModel);

    List<EventModel> getEvents(EventUser eventUser);

    Event findById(Long eventId);

    List<TicketModel> getTickets(Long eventId, String ticketKindName);

    boolean checkInTicket(Long ticketId);

    boolean checkOutTicket(Long ticketId);

    PurchasedTicket purchaseTicket(PurchaseTicketModel ticketModel, EventUser eventUser);

    List<EventModel> extractAssignedEvents(List<AssignedDistributor> assignedEvents);

    List<Event> getEvents();

    EventStatistic getEventStatistic(Long eventId);
}
