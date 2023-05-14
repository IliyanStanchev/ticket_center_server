package tuvarna.ticket_center_server.services;

import tuvarna.ticket_center_common.models.RegistrationModel;
import tuvarna.ticket_center_server.models.EventUser;
import tuvarna.ticket_center_server.models.User;

import java.util.List;

public interface EventUserService {

    EventUser findByEmail(String email);

    EventUser register(RegistrationModel registrationModel, User registeredUser);

    List<EventUser> findAll();

    List<EventUser> getDistributors();
}
