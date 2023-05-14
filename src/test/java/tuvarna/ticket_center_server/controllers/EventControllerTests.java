package tuvarna.ticket_center_server.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tuvarna.ticket_center_common.models.EventModel;
import tuvarna.ticket_center_common.models.PurchaseTicketModel;
import tuvarna.ticket_center_common.models.TicketModel;
import tuvarna.ticket_center_server.TicketCenterApplication;
import tuvarna.ticket_center_server.models.AssignedDistributor;
import tuvarna.ticket_center_server.models.EventUser;
import tuvarna.ticket_center_server.models.PurchasedTicket;
import tuvarna.ticket_center_server.services.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = EventController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = TicketCenterApplication.class)
public class EventControllerTests {

    @MockBean
    private EventService eventService;

    @MockBean
    private EventUserService eventUserService;

    @MockBean
    private AssignedDistributorService assignedDistributorService;

    @MockBean
    private LogService logService;

    @MockBean
    private MailService mailService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateEvent() throws Exception {
        // Mocking the EventUserService response
        EventUser organizer = new EventUser();
        when(eventUserService.findByEmail(any(String.class))).thenReturn(organizer);

        // Mocking the EventService response
        when(eventService.createEvent(any(EventUser.class), any(EventModel.class))).thenReturn(true);

        // Sending a POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"organizerEmail\": \"test@example.com\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestStatus").value("SUCCESS"));
    }

    @Test
    public void testGetEvents() throws Exception {
        // Mocking the EventUserService response
        EventUser eventUser = new EventUser();
        when(eventUserService.findByEmail(any(String.class))).thenReturn(eventUser);

        // Mocking the AssignedDistributorService response
        List<AssignedDistributor> assignedEvents = new ArrayList<>();
        when(assignedDistributorService.getAssignedEvents(any(Long.class))).thenReturn(assignedEvents);

        // Mocking the EventService response
        List<EventModel> events = new ArrayList<>();
        when(eventService.extractAssignedEvents(anyList())).thenReturn(events);

        // Sending a GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/events/{email}", "test@example.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestStatus").value("SUCCESS"));
    }

    @Test
    public void testGetTickets() throws Exception {
        // Mocking the EventService response
        List<TicketModel> tickets = new ArrayList<>();
        when(eventService.getTickets(any(Long.class), any(String.class))).thenReturn(tickets);

        // Sending a GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/events/tickets/{eventId}/{ticketKindName}", 1, "VIP"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestStatus").value("SUCCESS"));
    }

    @Test
    public void testCheckInTicket() throws Exception {
        // Mocking the EventService response
        when(eventService.checkInTicket(any(Long.class))).thenReturn(true);

        // Sending a POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/events/tickets/check-in/{ticketId}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestStatus").value("SUCCESS"));
    }

    @Test
    public void testCheckOutTicket() throws Exception {
        // Mocking the EventService response
        when(eventService.checkOutTicket(any(Long.class))).thenReturn(true);

        // Sending a DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/events/tickets/check-out/{ticketId}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestStatus").value("SUCCESS"));
    }

    @Test
    public void testPurchaseTicket() throws Exception {
        // Mocking the EventUserService response
        EventUser eventUser = new EventUser();
        when(eventUserService.findByEmail(any(String.class))).thenReturn(eventUser);

        // Mocking the EventService response
        PurchaseTicketModel ticketModel = new PurchaseTicketModel();
        PurchasedTicket purchasedTicket = new PurchasedTicket();
        when(eventService.purchaseTicket(any(PurchaseTicketModel.class), any(EventUser.class))).thenReturn(purchasedTicket);

        // Mocking the MailService response
        when(mailService.sendTicketPurchaseMail(any(PurchasedTicket.class))).thenReturn(true);

        // Sending a POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/events/tickets/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userMail\": \"test@example.com\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestStatus").value("SUCCESS"));
    }

    @Test
    public void testGetOrganizerEvents() throws Exception {
        // Mocking the EventUserService response
        EventUser eventUser = new EventUser();
        when(eventUserService.findByEmail(any(String.class))).thenReturn(eventUser);

        // Mocking the EventService response
        List<EventModel> events = new ArrayList<>();
        when(eventService.getEvents(any(EventUser.class))).thenReturn(events);

        // Sending a GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/events/organizer/{email}", "test@example.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestStatus").value("SUCCESS"));
    }
}

