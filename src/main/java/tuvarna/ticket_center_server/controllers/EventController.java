package tuvarna.ticket_center_server.controllers;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tuvarna.ticket_center_common.models.*;
import tuvarna.ticket_center_common.requests.RequestCodes;
import tuvarna.ticket_center_common.requests.RequestStatuses;
import tuvarna.ticket_center_common.responses.CommonResponse;
import tuvarna.ticket_center_server.enumerables.LogLevels;
import tuvarna.ticket_center_server.exceptions.InternalErrorResponseStatusException;
import tuvarna.ticket_center_server.models.AssignedDistributor;
import tuvarna.ticket_center_server.models.EventUser;
import tuvarna.ticket_center_server.models.PurchasedTicket;
import tuvarna.ticket_center_server.services.*;

import java.util.List;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventUserService eventUserService;

    @Autowired
    private AssignedDistributorService assignedDistributorService;

    @Autowired
    private LogService logService;

    @Autowired
    private MailService mailService;

    @PostMapping("/events")
    @Transactional
    public ResponseEntity<CommonResponse> createEvent(@RequestBody EventModel eventModel) {

        EventUser organizer = eventUserService.findByEmail(eventModel.getOrganizerEmail());
        if (organizer == null) {
            logService.log(LogLevels.ERROR, "EventService::createEvent", "", "EventServiceImpl::createEvent");
            throw new InternalErrorResponseStatusException();
        }

        if (!eventService.createEvent(organizer, eventModel)) {
            logService.log(LogLevels.ERROR, "EventService::createEvent", "", "EventServiceImpl::createEvent");
            throw new InternalErrorResponseStatusException();
        }

        return ResponseEntity.ok(new CommonResponse(RequestCodes.EVENTS_POST, RequestStatuses.SUCCESS, null));
    }

    @GetMapping(value = "events/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getEvents(@PathVariable String email) {

        EventUser eventUser = eventUserService.findByEmail(email);
        if (eventUser == null) {
            logService.log(LogLevels.ERROR, "EventUserService::findByEmail", email, "EventController::getEvents");
            throw new InternalErrorResponseStatusException();
        }

        List<AssignedDistributor> assignedEvents = assignedDistributorService.getAssignedEvents(eventUser.getId());
        List<EventModel> events = eventService.extractAssignedEvents(assignedEvents);

        return ResponseEntity.ok(new CommonResponse(RequestCodes.EVENTS_GET_ASSIGNED, RequestStatuses.SUCCESS, new EventsWrapper(events)));
    }

    @GetMapping(value = "events/tickets/{eventId}/{ticketKindName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getTickets(@PathVariable Long eventId, @PathVariable String ticketKindName) {

        List<TicketModel> tickets = eventService.getTickets(eventId, ticketKindName);

        return ResponseEntity.ok(new CommonResponse(RequestCodes.EVENTS_TICKETS_GET, RequestStatuses.SUCCESS, new TicketsWrapper(tickets)));
    }

    @PostMapping(value = "events/tickets/check-in/{ticketId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> checkInTicket(@PathVariable Long ticketId) {

        if (!eventService.checkInTicket(ticketId)) {
            logService.log(LogLevels.ERROR, "EventService::checkInTicket", ticketId.toString(), "EventController::checkInTicket");
            return ResponseEntity.ok().body(new CommonResponse(RequestCodes.EVENTS_TICKETS_CHECK_IN, RequestStatuses.FAILURE, "Ticket already checked in."));
        }

        return ResponseEntity.ok(new CommonResponse(RequestCodes.EVENTS_TICKETS_CHECK_IN, RequestStatuses.SUCCESS, null));
    }

    @DeleteMapping(value = "events/tickets/check-out/{ticketId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> checkOutTicket(@PathVariable Long ticketId) {

        if (!eventService.checkOutTicket(ticketId)) {
            logService.log(LogLevels.ERROR, "EventService::checkOutTicket", ticketId.toString(), "EventController::checkOutTicket");
            throw new InternalErrorResponseStatusException();
        }

        return ResponseEntity.ok(new CommonResponse(RequestCodes.EVENTS_TICKETS_CHECK_OUT, RequestStatuses.SUCCESS, null));
    }

    @PostMapping(value = "events/tickets/purchase", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<CommonResponse> purchaseTicket(@RequestBody PurchaseTicketModel ticketModel) {

        EventUser eventUser = eventUserService.findByEmail(ticketModel.getUserMail());
        if (eventUser == null) {
            logService.log(LogLevels.ERROR, "EventUserService::findByEmail", ticketModel.getUserMail(), "EventController::purchaseTicket");
            throw new InternalErrorResponseStatusException();
        }

        PurchasedTicket purchasedTicket = eventService.purchaseTicket(ticketModel, eventUser);
        if (purchasedTicket == null) {
            logService.log(LogLevels.ERROR, "EventService::purchaseTicket", ticketModel.toString(), "EventController::purchaseTicket");
            throw new InternalErrorResponseStatusException();
        }

        if (!mailService.sendTicketPurchaseMail(purchasedTicket)) {
            logService.log(LogLevels.ERROR, "MailService::sendTicketPurchaseMail", purchasedTicket.toString(), "EventController::purchaseTicket");
            throw new InternalErrorResponseStatusException();
        }

        return ResponseEntity.ok(new CommonResponse(RequestCodes.EVENTS_TICKETS_PURCHASE, RequestStatuses.SUCCESS, null));
    }

    @GetMapping(value = "events/organizer/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getOrganizerEvents(@PathVariable String email) {

        EventUser eventUser = eventUserService.findByEmail(email);
        if (eventUser == null) {
            logService.log(LogLevels.ERROR, "EventUserService::findByEmail", email, "EventController::getOrganizerEvents");
            throw new InternalErrorResponseStatusException();
        }

        List<EventModel> events = eventService.getEvents(eventUser);

        return ResponseEntity.ok(new CommonResponse(RequestCodes.EVENTS_GET, RequestStatuses.SUCCESS, new EventsWrapper(events)));
    }
}
