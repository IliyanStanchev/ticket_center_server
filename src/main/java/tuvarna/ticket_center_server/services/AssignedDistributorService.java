package tuvarna.ticket_center_server.services;

import tuvarna.ticket_center_server.models.AssignedDistributor;
import tuvarna.ticket_center_server.models.Event;
import tuvarna.ticket_center_server.models.EventUser;

import java.util.List;

public interface AssignedDistributorService {

    boolean assign(EventUser eventUser, Event event);

    List<AssignedDistributor> getAssignedDistributors(Long eventId);

    List<AssignedDistributor> getAssignedEvents(Long distributorId);
}
