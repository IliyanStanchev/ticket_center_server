package tuvarna.ticket_center_server.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tuvarna.ticket_center_common.enumerables.RoleTypes;
import tuvarna.ticket_center_common.models.RegistrationModel;
import tuvarna.ticket_center_server.models.EventUser;
import tuvarna.ticket_center_server.models.User;
import tuvarna.ticket_center_server.repositories.EventUserRepository;
import tuvarna.ticket_center_server.services.EventUserService;

import java.util.List;

@Service
public class EventUserServiceImpl implements EventUserService {

    @Autowired
    private EventUserRepository eventUserRepository;

    @Override
    public EventUser findByEmail(String email) {

        return eventUserRepository.findByEmail(email);
    }

    @Override
    public EventUser register(RegistrationModel registrationModel, User registeredUser) {

        EventUser eventUser = new EventUser();
        eventUser.setFirstName(registrationModel.getFirstName());
        eventUser.setLastName(registrationModel.getLastName());
        eventUser.setPhoneNumber(registrationModel.getPhoneNumber());
        eventUser.setHonorarium(registrationModel.getHonorarium());
        eventUser.setUser(registeredUser);

        return eventUserRepository.save(eventUser);
    }

    @Override
    public List<EventUser> findAll() {

        return eventUserRepository.findAll();
    }

    @Override
    public List<EventUser> getDistributors() {

        return eventUserRepository.getUsersByRole(RoleTypes.DISTRIBUTOR);
    }

}
