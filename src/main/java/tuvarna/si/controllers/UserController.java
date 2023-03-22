package tuvarna.si.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tuvarna.si.models.User;
import tuvarna.si.repositories.UserRepository;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String index() {

        User user = new User();
        user.setName("John Doe");

        userRepository.save(user);

        return "Welcome to the Ticket Center!";
    }
}
