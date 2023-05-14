package tuvarna.ticket_center_server.controllers;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tuvarna.ticket_center_common.enumerables.RoleTypes;
import tuvarna.ticket_center_common.enumerables.UserStatuses;
import tuvarna.ticket_center_common.models.*;
import tuvarna.ticket_center_common.requests.RequestCodes;
import tuvarna.ticket_center_common.requests.RequestStatuses;
import tuvarna.ticket_center_common.responses.CommonResponse;
import tuvarna.ticket_center_server.enumerables.LogLevels;
import tuvarna.ticket_center_server.exceptions.InternalErrorResponseStatusException;
import tuvarna.ticket_center_server.models.EventUser;
import tuvarna.ticket_center_server.models.User;
import tuvarna.ticket_center_server.models.VerificationCode;
import tuvarna.ticket_center_server.security.BCryptPasswordEncoderExtender;
import tuvarna.ticket_center_server.services.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EventUserService eventUserService;

    @Autowired
    private LogService logService;

    @Autowired
    private MailService mailService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @PutMapping(value = "users/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> login(@RequestBody LoginModel loginModel) {

        User user = userService.login(loginModel.getEmail(), loginModel.getPassword());
        if (user == null) {
            return ResponseEntity.ok().body(new CommonResponse(RequestCodes.USERS_LOGIN, RequestStatuses.FAILURE, "Invalid username or password"));
        }

        user.setLastLoginDate(LocalDateTime.now());
        if (userService.save(user) == null) {
            logService.log(LogLevels.ERROR, "IUserService::save", user.getEmail(), "UserController::login");
            throw new InternalErrorResponseStatusException();
        }

        UserModel userModel = new UserModel();
        userModel.setEmail(user.getEmail());
        userModel.setRole(user.getRole().getRoleType());

        if (user.getRole().getRoleType() != RoleTypes.ADMIN) {
            EventUser eventUser = eventUserService.findByEmail(user.getEmail());
            if (eventUser == null) {
                logService.log(LogLevels.ERROR, "IEventUserService::findByEmail", user.getEmail(), "UserController::login");
                throw new InternalErrorResponseStatusException();
            }

            userModel.setName(eventUser.getName());
        }

        return new ResponseEntity<>(new CommonResponse(RequestCodes.USERS_LOGIN, RequestStatuses.SUCCESS, userModel), HttpStatus.OK);
    }

    @PostMapping(value = "users/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<CommonResponse> register(@RequestBody RegistrationModel registrationModel) {

        User registeredUser = userService.register(registrationModel);
        if (registeredUser == null) {
            logService.log(LogLevels.ERROR, "IUserService::register", registrationModel.getEmail(), "UserController::register");
            throw new InternalErrorResponseStatusException();
        }

        EventUser eventUser = eventUserService.register(registrationModel, registeredUser);
        if (eventUser == null) {
            logService.log(LogLevels.ERROR, "IEventUserService::register", registrationModel.getEmail(), "UserController::register");
            throw new InternalErrorResponseStatusException();
        }

        VerificationCode verificationCode = verificationCodeService.create(registeredUser);
        if (verificationCode == null) {
            logService.log(LogLevels.ERROR, "VerificationCodeService::create", registrationModel.getEmail(), "UserController::register");
            throw new InternalErrorResponseStatusException();
        }

        if (!mailService.sendRegistrationMail(registrationModel, verificationCode.getCode())) {
            logService.log(LogLevels.ERROR, "MailService::sendRegistrationMail", registrationModel.getEmail(), "UserController::register");
            throw new InternalErrorResponseStatusException();
        }

        return new ResponseEntity<>(new CommonResponse(RequestCodes.USERS_REGISTER, RequestStatuses.SUCCESS, null), HttpStatus.OK);
    }

    @PutMapping(value = "users/verify", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<CommonResponse> verify(@RequestBody VerificationModel verificationModel) {

        VerificationCode verificationCode = verificationCodeService.findVerificationCode(verificationModel);
        if (verificationCode == null) {
            return ResponseEntity.ok().body(new CommonResponse(RequestCodes.USERS_VERIFY, RequestStatuses.FAILURE, "Invalid verification data"));
        }

        User user = userService.findByEmail(verificationModel.getEmail());
        if (user == null) {
            logService.log(LogLevels.ERROR, "IUserService::findByEmail", verificationModel.getEmail(), "UserController::verify");
            throw new InternalErrorResponseStatusException();
        }

        user.setStatus(UserStatuses.VERIFIED);
        if (userService.save(user) == null) {
            logService.log(LogLevels.ERROR, "IUserService::save", verificationModel.getEmail(), "UserController::verify");
            throw new InternalErrorResponseStatusException();
        }

        verificationCodeService.delete(verificationCode);

        return new ResponseEntity<>(new CommonResponse(RequestCodes.USERS_VERIFY, RequestStatuses.SUCCESS, null), HttpStatus.OK);
    }

    @PutMapping(value = "users/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<CommonResponse> changePassword(@RequestBody ChangePasswordModel changePasswordModel) {

        User user = userService.findByEmail(changePasswordModel.getEmail());
        if (user == null) {
            return ResponseEntity.ok().body(new CommonResponse(RequestCodes.USERS_CHANGE_PASSWORD, RequestStatuses.FAILURE, "Invalid email"));
        }

        BCryptPasswordEncoderExtender bCryptPasswordEncoderExtender = new BCryptPasswordEncoderExtender();

        if (user.getStatus() == UserStatuses.CONFIRMED && !bCryptPasswordEncoderExtender.matches(changePasswordModel.getOldPassword(), user.getPassword())) {
            return ResponseEntity.ok().body(new CommonResponse(RequestCodes.USERS_CHANGE_PASSWORD, RequestStatuses.FAILURE, "Invalid old password"));
        }

        user.setPassword(bCryptPasswordEncoderExtender.encode(changePasswordModel.getNewPassword()));
        user.setStatus(UserStatuses.CONFIRMED);

        if (userService.save(user) == null) {
            logService.log(LogLevels.ERROR, "IUserService::save", changePasswordModel.getEmail(), "UserController::changePassword");
            throw new InternalErrorResponseStatusException();
        }

        return new ResponseEntity<>(new CommonResponse(RequestCodes.USERS_CHANGE_PASSWORD, RequestStatuses.SUCCESS, null), HttpStatus.OK);
    }

    @PutMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> editUser(@RequestBody UserModel userModel) {

        User user = userService.findByEmail(userModel.getEmail());
        if (user == null) {
            return ResponseEntity.ok().body(new CommonResponse(RequestCodes.USERS_EDIT, RequestStatuses.FAILURE, "Invalid email"));
        }

        user.setStatus(userModel.getStatus());

        if (userService.save(user) == null) {
            logService.log(LogLevels.ERROR, "IUserService::save", userModel.getEmail(), "UserController::blockUser");
            throw new InternalErrorResponseStatusException();
        }

        return new ResponseEntity<>(new CommonResponse(RequestCodes.USERS_EDIT, RequestStatuses.SUCCESS, null), HttpStatus.OK);
    }

    @GetMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getUsers() {

        List<EventUser> eventUsers = eventUserService.findAll();
        if (eventUsers == null) {
            logService.log(LogLevels.ERROR, "IUserService::findAll", null, "UserController::getUsers");
            throw new InternalErrorResponseStatusException();
        }

        List<UserModel> userModels = new ArrayList<>();
        for (EventUser eventUser : eventUsers) {

            if (eventUser.getUser().getRole().getRoleType() == RoleTypes.ADMIN)
                continue;

            UserModel userModel = new UserModel();
            userModel.setEmail(eventUser.getUser().getEmail());
            userModel.setRole(eventUser.getUser().getRole().getRoleType());
            userModel.setStatus(eventUser.getUser().getStatus());
            userModel.setName(eventUser.getName());
            userModel.setPhoneNumber(eventUser.getPhoneNumber());
            userModel.setHonorarium(eventUser.getHonorarium());
            userModels.add(userModel);
        }

        return new ResponseEntity<>(new CommonResponse(RequestCodes.USERS_GET, RequestStatuses.SUCCESS, new UsersWrapper(userModels)), HttpStatus.OK);
    }
}
