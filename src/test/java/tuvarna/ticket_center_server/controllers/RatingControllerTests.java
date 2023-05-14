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
import tuvarna.ticket_center_common.models.RatingModel;
import tuvarna.ticket_center_server.TicketCenterApplication;
import tuvarna.ticket_center_server.models.EventUser;
import tuvarna.ticket_center_server.models.Rating;
import tuvarna.ticket_center_server.services.EventUserService;
import tuvarna.ticket_center_server.services.LogService;
import tuvarna.ticket_center_server.services.RatingService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RatingController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = TicketCenterApplication.class)
public class RatingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    @MockBean
    private EventUserService eventUserService;

    @MockBean
    private LogService logService;


    @Test
    public void testGetRatings() throws Exception {
        // Mocking the EventUserService response
        EventUser eventUser = new EventUser();
        when(eventUserService.findByEmail(any(String.class))).thenReturn(eventUser);

        // Mocking the RatingService response
        List<RatingModel> ratings = new ArrayList<>();
        when(ratingService.getUserRatings(any(Long.class))).thenReturn(ratings);

        // Sending a GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/ratings/{email}", "test@example.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestStatus").value("SUCCESS"));
    }

    @Test
    public void testAddRating() throws Exception {
        // Mocking the EventUserService response for ratedUser
        EventUser ratedUser = new EventUser();
        when(eventUserService.findByEmail(any(String.class))).thenReturn(ratedUser);

        // Mocking the EventUserService response for ratingUser
        EventUser ratingUser = new EventUser();
        when(eventUserService.findByEmail(any(String.class))).thenReturn(ratingUser);

        // Mocking the RatingService response
        when(ratingService.save(any(Rating.class))).thenReturn(true);

        // Sending a POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rating\": 5, \"comment\": \"Great\", \"distributorEmail\": \"test@example.com\", \"ratingUserEmail\": \"user@example.com\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestStatus").value("SUCCESS"));
    }
}
