package tuvarna.ticket_center_server.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "RATINGS")
public class Rating {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "RATING")
    private int rating;

    @Column(name = "COMMENT", length = 1024)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "RATED_USER_ID")
    private EventUser ratedUser;

    @ManyToOne
    @JoinColumn(name = "RATING_USER_ID")
    private EventUser ratingUser;

    @Column(name = "DATE")
    private LocalDateTime date;

    public Rating() {
    }

    public Rating(int rating, String comment, EventUser ratedUser, EventUser ratingUser) {
        this.rating = rating;
        this.comment = comment;
        this.ratedUser = ratedUser;
        this.ratingUser = ratingUser;
        this.date = LocalDateTime.now();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public EventUser getRatedUser() {
        return ratedUser;
    }

    public void setRatedUser(EventUser ratedUser) {
        this.ratedUser = ratedUser;
    }

    public EventUser getRatingUser() {
        return ratingUser;
    }

    public void setRatingUser(EventUser ratingUser) {
        this.ratingUser = ratingUser;
    }
}
