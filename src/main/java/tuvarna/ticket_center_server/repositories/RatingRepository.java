package tuvarna.ticket_center_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tuvarna.ticket_center_server.models.Rating;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT r FROM RATINGS r WHERE r.ratedUser.id = ?1")
    List<Rating> getUserRatings(Long userId);
}
