package tuvarna.ticket_center_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tuvarna.ticket_center_server.models.AssignedDistributor;

import java.util.List;

@Repository
public interface AssignedDistributorRepository extends JpaRepository<AssignedDistributor, Long> {

    @Query("FROM ASSIGNED_DISTRIBUTORS a WHERE a.event.id = ?1")
    List<AssignedDistributor> getByEventId(Long eventId);


    @Query("FROM ASSIGNED_DISTRIBUTORS a WHERE a.distributor.id = ?1")
    List<AssignedDistributor> getByDistributorId(Long distributorId);
}
