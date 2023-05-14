package tuvarna.ticket_center_server.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tuvarna.ticket_center_common.models.RatingModel;
import tuvarna.ticket_center_server.models.Rating;
import tuvarna.ticket_center_server.repositories.RatingRepository;
import tuvarna.ticket_center_server.services.RatingService;

import java.util.ArrayList;
import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public List<RatingModel> getUserRatings(Long userId) {

        List<RatingModel> ratingModels = new ArrayList<>();

        List<Rating> ratings = ratingRepository.getUserRatings(userId);
        for (Rating rating : ratings) {
            ratingModels.add(new RatingModel(rating.getRating()
                    , rating.getComment()
                    , rating.getRatedUser().getUser().getEmail()
                    , rating.getRatingUser().getUser().getEmail()
                    , rating.getRatingUser().getFirstName() + " " + rating.getRatingUser().getLastName()
                    , rating.getDate()));
        }

        return ratingModels;
    }

    @Override
    public boolean save(Rating rating) {

        return ratingRepository.save(rating) != null;
    }
}
