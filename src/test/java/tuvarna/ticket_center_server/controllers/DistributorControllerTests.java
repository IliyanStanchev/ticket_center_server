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
import tuvarna.ticket_center_common.enumerables.RoleTypes;
import tuvarna.ticket_center_common.enumerables.UserStatuses;
import tuvarna.ticket_center_server.TicketCenterApplication;
import tuvarna.ticket_center_server.models.*;
import tuvarna.ticket_center_server.services.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = DistributorController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = TicketCenterApplication.class)
public class DistributorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventUserService eventUserService;

    @MockBean
    private RatingService ratingService;

    @MockBean
    private AssignedDistributorService assignedDistributorService;

    @MockBean
    private EventService eventService;

    @MockBean
    private LogService logService;

    @Test
    public void testGetDistributors() throws Exception {

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setStatus(UserStatuses.CONFIRMED);

        EventUser eventUser = new EventUser();
        eventUser.setUser(user);
        eventUser.setFirstName("test");
        eventUser.setLastName("test");

        // Mocking the EventUserService response
        List<EventUser> distributors = new ArrayList<>();
        distributors.add(eventUser);
        when(eventUserService.getDistributors()).thenReturn(distributors);

        // Mocking the RatingService response
        when(ratingService.getUserRatings(any(Long.class))).thenReturn(new ArrayList<>());

        // Sending a GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/distributors"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestStatus").value("SUCCESS"));
    }

    @Test
    public void testGetAssignedDistributors() throws Exception {

        EventUser eventUser = new EventUser();
        eventUser.setFirstName("test");
        eventUser.setLastName("test");
        eventUser.setUser(new User("test@gmail.com", "test", LocalDateTime.now(), LocalDateTime.now(), new Role(1L, RoleTypes.DISTRIBUTOR), UserStatuses.CONFIRMED));

        AssignedDistributor assignedDistributor = new AssignedDistributor();
        assignedDistributor.setDistributor(eventUser);
        assignedDistributor.setEvent(new Event());

        // Mocking the AssignedDistributorService response
        List<AssignedDistributor> distributors = new ArrayList<>();
        distributors.add(assignedDistributor);
        when(assignedDistributorService.getAssignedDistributors(any(Long.class))).thenReturn(distributors);

        // Mocking the RatingService response
        when(ratingService.getUserRatings(any(Long.class))).thenReturn(new ArrayList<>());

        // Sending a GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/distributors/assigned/{eventId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestStatus").value("SUCCESS"));
    }

    @Test
    public void testGetFreeDistributors() throws Exception {

        EventUser eventUser = new EventUser();
        eventUser.setFirstName("test");
        eventUser.setLastName("test");
        eventUser.setUser(new User("test@gmail.com", "test", LocalDateTime.now(), LocalDateTime.now(), new Role(1L, RoleTypes.DISTRIBUTOR), UserStatuses.CONFIRMED));

        AssignedDistributor assignedDistributor = new AssignedDistributor();
        assignedDistributor.setDistributor(eventUser);
        assignedDistributor.setEvent(new Event());

        // Mocking the EventUserService response
        List<EventUser> distributors = new ArrayList<>();
        distributors.add(eventUser);
        when(eventUserService.getDistributors()).thenReturn(distributors);

        // Mocking the AssignedDistributorService response
        List<AssignedDistributor> assignedDistributors = new ArrayList<>();
        assignedDistributors.add(assignedDistributor);
        when(assignedDistributorService.getAssignedDistributors(any(Long.class))).thenReturn(assignedDistributors);

        // Mocking the RatingService response
        when(ratingService.getUserRatings(any(Long.class))).thenReturn(new ArrayList<>());

        // Sending a GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/distributors/free/{eventId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestStatus").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestCode").value("DISTRIBUTORS_GET_FREE"));
    }

    @Test
    public void testAssignDistributor() throws Exception {
        // Mocking the EventUserService response
        EventUser distributor = new EventUser();
        distributor.setUser(new User("test@gmail.com", "test", LocalDateTime.now(), LocalDateTime.now(), new Role(1L, RoleTypes.DISTRIBUTOR), UserStatuses.CONFIRMED));
        when(eventUserService.findByEmail(any(String.class))).thenReturn(distributor);
        // Mocking the EventService response
        Event event = new Event();
        when(eventService.findById(any(Long.class))).thenReturn(event);

        // Mocking the AssignedDistributorService response
        when(assignedDistributorService.assign(any(EventUser.class), any(Event.class))).thenReturn(true);

        // Sending a POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/distributors/assign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"distributorEmail\": \"test@example.com\", \"eventId\": 1}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestStatus").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestCode").value("DISTRIBUTORS_ASSIGN"));
    }

    @Test
    public void testGetDistributor() throws Exception {
        // Mocking the EventUserService response
        EventUser distributor = new EventUser();
        distributor.setUser(new User());
        when(eventUserService.findByEmail(any(String.class))).thenReturn(distributor);

        // Mocking the RatingService response
        when(ratingService.getUserRatings(any(Long.class))).thenReturn(new ArrayList<>());

        // Sending a GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/distributors/{email}", "test@example.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestStatus").value("SUCCESS"));
    }
}
