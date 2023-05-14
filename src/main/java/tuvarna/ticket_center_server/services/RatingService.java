package tuvarna.ticket_center_server.services;

import tuvarna.ticket_center_common.models.RatingModel;
import tuvarna.ticket_center_server.models.Rating;

import java.util.List;

public interface RatingService {

    List<RatingModel> getUserRatings(Long userId);

    boolean save(Rating rating);
}
