package tuvarna.ticket_center_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tuvarna.ticket_center_server.models.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM EVENTS e WHERE e.organizer.id = ?1")
    List<Event> getEventsByOrganizer(Long organizerId);
}
