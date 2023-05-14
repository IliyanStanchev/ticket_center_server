package tuvarna.ticket_center_server.services;

import tuvarna.ticket_center_common.models.RegistrationModel;
import tuvarna.ticket_center_server.models.User;

import java.util.List;

public interface UserService {

    User login(String email, String password);

    User save(User user);

    User register(RegistrationModel registrationModel);

    User findByEmail(String email);

    List<User> findAll();
}
