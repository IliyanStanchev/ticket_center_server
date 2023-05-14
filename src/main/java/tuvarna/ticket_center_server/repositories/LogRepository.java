package tuvarna.ticket_center_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tuvarna.ticket_center_server.models.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
}
