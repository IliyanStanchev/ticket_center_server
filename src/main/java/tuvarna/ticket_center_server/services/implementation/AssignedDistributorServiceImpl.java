package tuvarna.ticket_center_server.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tuvarna.ticket_center_server.models.AssignedDistributor;
import tuvarna.ticket_center_server.models.Event;
import tuvarna.ticket_center_server.models.EventUser;
import tuvarna.ticket_center_server.repositories.AssignedDistributorRepository;
import tuvarna.ticket_center_server.services.AssignedDistributorService;

import java.util.List;

@Service
public class AssignedDistributorServiceImpl implements AssignedDistributorService {

    @Autowired
    AssignedDistributorRepository assignedDistributorRepository;

    @Override
    public boolean assign(EventUser eventUser, Event event) {

        AssignedDistributor assignedDistributor = assignedDistributorRepository.save(new AssignedDistributor(eventUser, event));
        if (assignedDistributor == null) {
            return false;
        }

        return true;
    }

    @Override
    public List<AssignedDistributor> getAssignedDistributors(Long eventId) {

        return assignedDistributorRepository.getByEventId(eventId);
    }

    @Override
    public List<AssignedDistributor> getAssignedEvents(Long distributorId) {

        return assignedDistributorRepository.getByDistributorId(distributorId);
    }
}
