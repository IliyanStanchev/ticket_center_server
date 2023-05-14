package tuvarna.ticket_center_server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tuvarna.ticket_center_common.models.RatingModel;
import tuvarna.ticket_center_common.models.RatingsWrapper;
import tuvarna.ticket_center_common.requests.RequestCodes;
import tuvarna.ticket_center_common.requests.RequestStatuses;
import tuvarna.ticket_center_common.responses.CommonResponse;
import tuvarna.ticket_center_server.enumerables.LogLevels;
import tuvarna.ticket_center_server.exceptions.InternalErrorResponseStatusException;
import tuvarna.ticket_center_server.models.EventUser;
import tuvarna.ticket_center_server.models.Rating;
import tuvarna.ticket_center_server.services.EventUserService;
import tuvarna.ticket_center_server.services.LogService;
import tuvarna.ticket_center_server.services.RatingService;

import java.util.List;

@RestController
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private EventUserService eventUserService;

    @Autowired
    private LogService logService;

    @GetMapping(value = "ratings/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getRatings(@PathVariable String email) {

        EventUser eventUser = eventUserService.findByEmail(email);
        if (eventUser == null) {
            logService.log(LogLevels.ERROR, "EventUserService::findByEmail", email, "RatingController::getRatings");
            throw new InternalErrorResponseStatusException();
        }

        List<RatingModel> ratings = ratingService.getUserRatings(eventUser.getId());
        return new ResponseEntity<>(new CommonResponse(RequestCodes.RATINGS_GET, RequestStatuses.SUCCESS, new RatingsWrapper(ratings)), HttpStatus.OK);
    }

    @PostMapping(value = "ratings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> addRating(@RequestBody RatingModel ratingModel) {

        EventUser ratedUser = eventUserService.findByEmail(ratingModel.distributorEmail);
        if (ratedUser == null) {
            logService.log(LogLevels.ERROR, "EventUserService::findByEmail", ratingModel.distributorEmail, "RatingController::addRating");
            throw new InternalErrorResponseStatusException();
        }

        EventUser ratingUser = eventUserService.findByEmail(ratingModel.ratingUserEmail);
        if (ratingUser == null) {
            logService.log(LogLevels.ERROR, "EventUserService::findByEmail", ratingModel.ratingUserEmail, "RatingController::addRating");
            throw new InternalErrorResponseStatusException();
        }

        Rating rating = new Rating(ratingModel.rating, ratingModel.comment, ratedUser, ratingUser);
        if (!ratingService.save(rating)) {
            logService.log(LogLevels.ERROR, "RatingService::save", rating.toString(), "RatingController::addRating");
            throw new InternalErrorResponseStatusException();
        }

        return new ResponseEntity<>(new CommonResponse(RequestCodes.RATINGS_POST, RequestStatuses.SUCCESS, null), HttpStatus.OK);
    }
}
