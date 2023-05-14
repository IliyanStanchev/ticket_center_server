package tuvarna.ticket_center_server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tuvarna.ticket_center_common.enumerables.RoleTypes;
import tuvarna.ticket_center_common.models.AssignDistributorModel;
import tuvarna.ticket_center_common.models.DistributorModel;
import tuvarna.ticket_center_common.models.DistributorsWrapper;
import tuvarna.ticket_center_common.models.RatingModel;
import tuvarna.ticket_center_common.requests.RequestCodes;
import tuvarna.ticket_center_common.requests.RequestStatuses;
import tuvarna.ticket_center_common.responses.CommonResponse;
import tuvarna.ticket_center_server.enumerables.LogLevels;
import tuvarna.ticket_center_server.exceptions.InternalErrorResponseStatusException;
import tuvarna.ticket_center_server.models.AssignedDistributor;
import tuvarna.ticket_center_server.models.Event;
import tuvarna.ticket_center_server.models.EventUser;
import tuvarna.ticket_center_server.services.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DistributorController {

    @Autowired
    private EventUserService eventUserService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private AssignedDistributorService assignedDistributorService;

    @Autowired
    private EventService eventService;

    @Autowired
    private LogService logService;

    @GetMapping(value = "distributors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getDistributors() {

        List<EventUser> distributors = eventUserService.getDistributors();

        List<DistributorModel> distributorModels = new ArrayList<>();

        for (EventUser distributor : distributors) {

            List<RatingModel> ratings = ratingService.getUserRatings(distributor.getId());

            distributorModels.add(new DistributorModel(distributor.getFirstName()
                    , distributor.getLastName()
                    , distributor.getPhoneNumber()
                    , distributor.getHonorarium()
                    , distributor.getUser().getEmail()
                    , ratings));
        }

        return ResponseEntity.ok(new CommonResponse(RequestCodes.DISTRIBUTORS_GET, RequestStatuses.SUCCESS, new DistributorsWrapper(distributorModels)));
    }

    @GetMapping(value = "distributors/assigned/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAssignedDistributors(@PathVariable Long eventId) {

        List<AssignedDistributor> distributors = assignedDistributorService.getAssignedDistributors(eventId);

        List<DistributorModel> distributorModels = new ArrayList<>();

        for (AssignedDistributor distributor : distributors) {

            List<RatingModel> ratings = ratingService.getUserRatings(distributor.getDistributor().getId());

            distributorModels.add(new DistributorModel(distributor.getDistributor().getFirstName()
                    , distributor.getDistributor().getLastName()
                    , distributor.getDistributor().getPhoneNumber()
                    , distributor.getDistributor().getHonorarium()
                    , distributor.getDistributor().getUser().getEmail()
                    , ratings));
        }

        return ResponseEntity.ok(new CommonResponse(RequestCodes.DISTRIBUTORS_GET_ASSIGNED, RequestStatuses.SUCCESS, new DistributorsWrapper(distributorModels)));
    }

    @GetMapping(value = "distributors/free/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getFreeDistributors(@PathVariable Long eventId) {

        List<EventUser> distributors = eventUserService.getDistributors();
        List<AssignedDistributor> assignedDistributors = assignedDistributorService.getAssignedDistributors(eventId);

        List<DistributorModel> distributorModels = new ArrayList<>();

        for (EventUser distributor : distributors) {

            boolean isAssigned = false;

            for (AssignedDistributor assignedDistributor : assignedDistributors) {

                if (assignedDistributor.getDistributor().getId() == distributor.getId()) {
                    isAssigned = true;
                    break;
                }
            }

            if (!isAssigned) {

                List<RatingModel> ratings = ratingService.getUserRatings(distributor.getId());

                distributorModels.add(new DistributorModel(distributor.getFirstName()
                        , distributor.getLastName()
                        , distributor.getPhoneNumber()
                        , distributor.getHonorarium()
                        , distributor.getUser().getEmail()
                        , ratings));
            }
        }

        return ResponseEntity.ok(new CommonResponse(RequestCodes.DISTRIBUTORS_GET_FREE, RequestStatuses.SUCCESS, new DistributorsWrapper(distributorModels)));
    }

    @PostMapping(value = "distributors/assign", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> assignDistributor(@RequestBody AssignDistributorModel assignDistributorModel) {

        EventUser distributor = eventUserService.findByEmail(assignDistributorModel.getDistributorEmail());
        if (distributor == null) {
            logService.log(LogLevels.ERROR, "EventUserService::findByEmail", assignDistributorModel.getDistributorEmail(), "DistributorController::assignDistributor");
            throw new InternalErrorResponseStatusException();
        }

        if (distributor.getUser().getRole().getRoleType() != RoleTypes.DISTRIBUTOR) {
            logService.log(LogLevels.ERROR, "Distributor with email " + assignDistributorModel.getDistributorEmail() + " is not a distributor", null, "DistributorController::assignDistributor");
            throw new InternalErrorResponseStatusException();
        }

        Event event = eventService.findById(assignDistributorModel.getEventId());
        if (event == null) {
            logService.log(LogLevels.ERROR, "EventService::findById", String.valueOf(assignDistributorModel.getEventId()), "DistributorController::assignDistributor");
            throw new InternalErrorResponseStatusException();
        }

        if (!assignedDistributorService.assign(distributor, event)) {
            logService.log(LogLevels.ERROR, "AssignedDistributorService::assign", "Distributor with id " + distributor.getId() + " and event with id " + event.getId(), "DistributorController::assignDistributor");
            throw new InternalErrorResponseStatusException();
        }

        return ResponseEntity.ok(new CommonResponse(RequestCodes.DISTRIBUTORS_ASSIGN, RequestStatuses.SUCCESS, null));
    }

    @GetMapping(value = "distributors/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getDistributor(@PathVariable String email) {

        EventUser distributor = eventUserService.findByEmail(email);

        if (distributor == null) {
            logService.log(LogLevels.ERROR, "EventUserService::findByEmail", email, "DistributorController::getDistributor");
            throw new InternalErrorResponseStatusException();
        }

        List<RatingModel> ratings = ratingService.getUserRatings(distributor.getId());

        DistributorModel distributorModel = new DistributorModel(distributor.getFirstName()
                , distributor.getLastName()
                , distributor.getPhoneNumber()
                , distributor.getHonorarium()
                , distributor.getUser().getEmail()
                , ratings);

        return ResponseEntity.ok(new CommonResponse(RequestCodes.DISTRIBUTORS_GET, RequestStatuses.SUCCESS, distributorModel));
    }
}
