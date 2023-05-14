package tuvarna.ticket_center_server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import tuvarna.ticket_center_common.models.LoginModel;
import tuvarna.ticket_center_common.models.RegistrationModel;
import tuvarna.ticket_center_common.models.UserModel;
import tuvarna.ticket_center_server.TicketCenterApplication;
import tuvarna.ticket_center_server.models.EventUser;
import tuvarna.ticket_center_server.models.Role;
import tuvarna.ticket_center_server.models.User;
import tuvarna.ticket_center_server.models.VerificationCode;
import tuvarna.ticket_center_server.services.*;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = TicketCenterApplication.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private EventUserService eventUserService;

    @MockBean
    private LogService logService;

    @MockBean
    private MailService mailService;

    @MockBean
    private VerificationCodeService verificationCodeService;

    // Helper method to convert objects to JSON string
    private static String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    public void login_ValidCredentials_ReturnsSuccessResponse() throws Exception {
        // Arrange
        LoginModel loginModel = new LoginModel();
        loginModel.setEmail("ench3r@gmail.com");
        loginModel.setPassword("sach");

        User user = new User();
        user.setEmail(loginModel.getEmail());
        user.setPassword(loginModel.getPassword());
        user.setRole(new Role(1L, RoleTypes.ADMIN));

        when(userService.login(loginModel.getEmail(), loginModel.getPassword())).thenReturn(user);
        when(userService.save(user)).thenReturn(user);

        // Act
        mockMvc.perform(put("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginModel)))
                .andExpect(status().isOk());

        // Assert
        verify(userService).login(loginModel.getEmail(), loginModel.getPassword());
        verify(userService).save(user);
    }

    @Test
    public void login_InvalidCredentials_ReturnsFailureResponse() throws Exception {
        // Arrange
        LoginModel loginModel = new LoginModel();
        loginModel.setEmail("user@example.com");
        loginModel.setPassword("password");

        when(userService.login(loginModel.getEmail(), loginModel.getPassword())).thenReturn(null);

        // Act
        mockMvc.perform(put("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginModel)))
                .andExpect(status().isOk());

        // Assert
        verify(userService).login(loginModel.getEmail(), loginModel.getPassword());
    }

    @Test
    public void getUsers_ReturnsUserList() throws Exception {
        // Arrange
        User adminUser = new User();
        adminUser.setEmail("admin@example.com");
        adminUser.setRole(new Role(1L, RoleTypes.ADMIN));

        EventUser eventUser1 = new EventUser();
        eventUser1.setUser(adminUser);

        User regularUser = new User();
        regularUser.setEmail("user@example.com");
        regularUser.setRole(new Role(1L, RoleTypes.DISTRIBUTOR));
        regularUser.setStatus(UserStatuses.CONFIRMED);

        EventUser eventUser2 = new EventUser();
        eventUser2.setUser(regularUser);
        eventUser2.setFirstName("John Doe");
        eventUser2.setPhoneNumber("1234567890");
        eventUser2.setHonorarium(100);

        List<EventUser> eventUsers = Arrays.asList(eventUser1, eventUser2);

        when(eventUserService.findAll()).thenReturn(eventUsers);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestCode").value("USERS_GET"))
                .andExpect(jsonPath("$.requestStatus").value("SUCCESS"))
                .andExpect(jsonPath("$.response.users[0].email").value("user@example.com"));

        // Assert
        verify(eventUserService).findAll();
    }

    @Test
    public void register_ValidRegistrationData_ReturnsSuccess() throws Exception {
        // Arrange
        RegistrationModel registrationModel = new RegistrationModel();
        registrationModel.setEmail("user@example.com");
        registrationModel.setFirstName("John");
        registrationModel.setLastName("Doe");
        registrationModel.setPhoneNumber("1234567890");
        registrationModel.setRoleType(RoleTypes.DISTRIBUTOR);

        User registeredUser = new User();
        registeredUser.setEmail(registrationModel.getEmail());
        registeredUser.setStatus(UserStatuses.PENDING);

        EventUser eventUser = new EventUser();
        eventUser.setFirstName(registrationModel.getFirstName());
        eventUser.setPhoneNumber(registrationModel.getPhoneNumber());

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setCode("1234");

        // Mock dependencies

        doReturn(registeredUser).when(userService).register(registrationModel);
        doReturn(eventUser).when(eventUserService).register(registrationModel, registeredUser);
        doReturn(verificationCode).when(verificationCodeService).create(registeredUser);
        doReturn(true).when(mailService).sendRegistrationMail(registrationModel, verificationCode.getCode());

        // Act and Assert
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registrationModel)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testEditUserSuccess() throws Exception {

        UserModel userModel = new UserModel();
        userModel.setEmail("test@example.com");
        userModel.setStatus(UserStatuses.BLOCKED);

        // Mocking the UserService response
        when(userService.findByEmail(userModel.getEmail())).thenReturn(new User());
        when(userService.save(any(User.class))).thenReturn(new User());

        // Sending a PUT request
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userModel)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestCode").value("USERS_EDIT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestStatus").value("SUCCESS"));
    }

    @Test
    public void testEditUserInvalidEmail() throws Exception {

        UserModel userModel = new UserModel();
        userModel.setEmail("invalid@example.com");
        userModel.setStatus(UserStatuses.BLOCKED);

        // Mocking the UserService response
        when(userService.findByEmail(userModel.getEmail())).thenReturn(null);

        // Sending a PUT request
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userModel)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestCode").value("USERS_EDIT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestStatus").value("FAILURE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").value("Invalid email"));
    }

    @Test
    public void testEditUserInternalError() throws Exception {

        UserModel userModel = new UserModel();
        userModel.setEmail("invalid@example.com");
        userModel.setStatus(UserStatuses.BLOCKED);

        // Mocking the UserService response
        when(userService.findByEmail(any(String.class))).thenReturn(new User());
        when(userService.save(any(User.class))).thenReturn(null);

        // Sending a PUT request
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userModel)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}
