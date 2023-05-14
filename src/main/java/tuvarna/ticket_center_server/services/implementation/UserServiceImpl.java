package tuvarna.ticket_center_server.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tuvarna.ticket_center_common.enumerables.UserStatuses;
import tuvarna.ticket_center_common.models.RegistrationModel;
import tuvarna.ticket_center_server.models.Role;
import tuvarna.ticket_center_server.models.User;
import tuvarna.ticket_center_server.repositories.RoleRepository;
import tuvarna.ticket_center_server.repositories.UserRepository;
import tuvarna.ticket_center_server.security.BCryptPasswordEncoderExtender;
import tuvarna.ticket_center_server.services.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User login(String email, String password) {

        User user = userRepository.findByEmail(email);
        if (user == null)
            return null;

        BCryptPasswordEncoderExtender encoder = new BCryptPasswordEncoderExtender();
        if (!encoder.matches(password, user.getPassword()))
            return null;

        return user;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User register(RegistrationModel registrationModel) {

        User user = new User();
        user.setEmail(registrationModel.getEmail());
        user.setCreationDate(LocalDateTime.now());

        Role role = roleRepository.findByRoleType(registrationModel.getRoleType());
        if (role == null)
            return null;

        user.setRole(role);
        user.setStatus(UserStatuses.PENDING);

        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {

        return userRepository.findAll();
    }
}
