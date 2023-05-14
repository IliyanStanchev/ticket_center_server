package tuvarna.ticket_center_server.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tuvarna.ticket_center_common.models.EventModel;
import tuvarna.ticket_center_common.models.PurchaseTicketModel;
import tuvarna.ticket_center_common.models.TicketKindModel;
import tuvarna.ticket_center_common.models.TicketModel;
import tuvarna.ticket_center_server.data.EventStatistic;
import tuvarna.ticket_center_server.enumerables.LogLevels;
import tuvarna.ticket_center_server.models.*;
import tuvarna.ticket_center_server.repositories.*;
import tuvarna.ticket_center_server.services.EventService;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketKindRepository ticketKindRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PurchasedTicketRepository purchasedTicketRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CheckedTicketRepository checkedTicketRepository;

    @Autowired
    private LogServiceImpl logService;

    @Override
    public boolean createEvent(EventUser organizer, EventModel eventModel) {

        Event event = eventRepository.save(new Event(eventModel, organizer));
        if (event == null) {
            logService.log(LogLevels.ERROR, "EventService::createEvent", "", "EventServiceImpl::createEvent");
            return true;
        }

        for (TicketKindModel ticketKindName : eventModel.getTicketKinds()) {

            TicketKind ticketKind = ticketKindRepository.save(new TicketKind(ticketKindName, event));
            if (ticketKind == null) {
                logService.log(LogLevels.ERROR, "TicketKindRepository::save", "", "EventServiceImpl::createEvent");
                return true;
            }

            for (TicketModel ticketModel : ticketKindName.getTickets()) {

                Ticket ticket = ticketRepository.save(new Ticket(ticketModel, ticketKind));
                if (ticket == null) {
                    logService.log(LogLevels.ERROR, "TicketRepository::save", "", "EventServiceImpl::createEvent");
                    return true;
                }
            }
        }

        return true;
    }

    @Override
    public List<EventModel> getEvents(EventUser eventUser) {

        List<Event> events = eventRepository.getEventsByOrganizer(eventUser.getId());
        return extractEvents(events);
    }

    public List<EventModel> extractEvents(List<Event> events) {

        List<EventModel> eventModels = new ArrayList<>();
        for (Event event : events) {

            EventModel eventModel = new EventModel();
            eventModel.setId(event.getId());
            eventModel.setName(event.getName());
            eventModel.setDescription(event.getDescription());
            eventModel.setStartDate(event.getStartDate().toString());
            eventModel.setEndDate(event.getEndDate().toString());
            eventModel.setStartTime(event.getStartTime().toString());
            eventModel.setEventType(event.getEventType());
            eventModel.setOrganizerEmail(event.getOrganizer().getUser().getEmail());
            eventModel.setLocation(event.getLocation());

            List<TicketKind> ticketKinds = ticketKindRepository.getTicketKinds(event.getId());
            for (TicketKind ticketKind : ticketKinds) {

                TicketKindModel ticketKindModel = new TicketKindModel();
                ticketKindModel.setName(ticketKind.getName());
                ticketKindModel.setPrice(ticketKind.getPrice());
                ticketKindModel.setDescription(ticketKind.getDescription());
                ticketKindModel.setLimitPerBuyer(ticketKind.getLimitPerBuyer());

                List<Ticket> tickets = ticketRepository.getTickets(ticketKind.getId());
                for (Ticket ticket : tickets) {

                    TicketModel ticketModel = new TicketModel();

                    if (purchasedTicketRepository.findByTicket(ticket.getId()) != null)
                        ticketModel.setPurchased("true");
                    else
                        ticketModel.setPurchased("false");

                    ticketModel.setId(ticket.getId());
                    ticketModel.setNote(ticket.getNote());

                    ticketKindModel.addTicket(ticketModel);
                }

                eventModel.addTicketKind(ticketKindModel);
            }

            eventModels.add(eventModel);
        }

        return eventModels;
    }

    @Override
    public Event findById(Long eventId) {

        return eventRepository.findById(eventId).orElse(null);
    }

    @Override
    public List<TicketModel> getTickets(Long eventId, String ticketKindName) {

        List<PurchasedTicket> purchasedTickets = purchasedTicketRepository.getPurchasedTickets(eventId, ticketKindName);
        List<Ticket> tickets = ticketRepository.getFreeTickets(eventId, ticketKindName);

        List<TicketModel> ticketModels = new ArrayList<>();
        for (Ticket ticket : tickets) {

            for (PurchasedTicket purchasedTicket : purchasedTickets) {

                if (ticket.getId() == purchasedTicket.getTicket().getId()) {
                    continue;
                }

                TicketModel ticketModel = new TicketModel();
                ticketModel.setId(ticket.getId());
                ticketModel.setNote(ticket.getNote());
                ticketModel.setPurchased("false");
                ticketModels.add(ticketModel);
            }
        }

        return ticketModels;
    }

    @Override
    public boolean checkInTicket(Long ticketId) {

        CheckedTicket checkedTicket = checkedTicketRepository.findByTicket(ticketId);
        if (checkedTicket != null) {
            return false;
        }

        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if (ticket == null) {
            logService.log(LogLevels.ERROR, "TicketRepository::findById", "", "EventServiceImpl::checkInTicket");
            return false;
        }

        if (checkedTicketRepository.save(new CheckedTicket(ticket)) == null) {
            logService.log(LogLevels.ERROR, "CheckedTicketRepository::save", "", "EventServiceImpl::checkInTicket");
            return false;
        }

        return true;
    }

    @Override
    public boolean checkOutTicket(Long ticketId) {

        CheckedTicket checkedTicket = checkedTicketRepository.findByTicket(ticketId);
        if (checkedTicket == null) {
            return false;
        }

        checkedTicketRepository.deleteById(checkedTicket.getId());
        return true;
    }

    @Override
    public PurchasedTicket purchaseTicket(PurchaseTicketModel ticketModel, EventUser eventUser) {

        Invoice invoice = invoiceRepository.save(new Invoice(ticketModel.getPrice(), ticketModel.getPurchaserMail(), ticketModel.getPurchaserName(), ticketModel.getPurchaserPhoneNumber(), eventUser));
        if (invoice == null) {
            logService.log(LogLevels.ERROR, "InvoiceRepository::save", "", "EventServiceImpl::purchaseTicket");
            return null;
        }

        Ticket ticket = ticketRepository.findById(ticketModel.getTicketId()).orElse(null);
        if (ticket == null) {
            logService.log(LogLevels.ERROR, "TicketRepository::findById", "", "EventServiceImpl::purchaseTicket");
            return null;
        }

        PurchasedTicket purchasedTicket = purchasedTicketRepository.save(new PurchasedTicket(ticket, invoice));
        if (purchasedTicket == null) {
            logService.log(LogLevels.ERROR, "PurchasedTicketRepository::save", "", "EventServiceImpl::purchaseTicket");
            return null;
        }

        return purchasedTicket;
    }

    @Override
    public List<EventModel> extractAssignedEvents(List<AssignedDistributor> assignedEvents) {

        List<Event> events = new ArrayList<>();
        for (AssignedDistributor distributor : assignedEvents) {
            if (distributor.getEvent().getStartDate().isBefore(ChronoLocalDate.from(LocalDateTime.now()))) {
                continue;
            }

            events.add(distributor.getEvent());
        }

        List<EventModel> clearedEvents = new ArrayList<>();
        List<EventModel> eventModels = extractEvents(events);
        for (EventModel eventModel : eventModels) {

            List<PurchasedTicket> purchasedTickets = purchasedTicketRepository.getPurchasedTicketsForEvent(eventModel.getId());

            if (purchasedTickets.size() != eventModel.getTotalTickets()) {
                clearedEvents.add(eventModel);
            }
        }

        return clearedEvents;
    }

    @Override
    public List<Event> getEvents() {

        return eventRepository.findAll();
    }

    @Override
    public EventStatistic getEventStatistic(Long eventId) {

        List<TicketKind> ticketKinds = ticketKindRepository.getTicketKinds(eventId);
        int soldTickets = 0;
        int totalTickets = 0;

        for (TicketKind ticketKind : ticketKinds) {
            List<PurchasedTicket> purchasedTickets = purchasedTicketRepository.getPurchasedTickets(eventId, ticketKind.getName());
            List<Ticket> tickets = ticketRepository.getTickets(ticketKind.getId());

            totalTickets += tickets.size();
            soldTickets += purchasedTickets.size();
        }

        return new EventStatistic(totalTickets, soldTickets);
    }
}
