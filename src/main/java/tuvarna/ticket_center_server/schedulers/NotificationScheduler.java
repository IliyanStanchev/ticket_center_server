package tuvarna.ticket_center_server.schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tuvarna.ticket_center_server.data.EventStatistic;
import tuvarna.ticket_center_server.models.AssignedDistributor;
import tuvarna.ticket_center_server.models.Event;
import tuvarna.ticket_center_server.services.AssignedDistributorService;
import tuvarna.ticket_center_server.services.EventService;
import tuvarna.ticket_center_server.services.MailService;

import java.time.LocalDate;
import java.util.List;

@Component
public class NotificationScheduler {

    final static int DAYS_TILL_NOTIFICATION = 7;

    @Autowired
    private MailService mailService;

    @Autowired
    private EventService eventService;

    @Autowired
    private AssignedDistributorService assignedDistributorService;

    @Scheduled(cron = "@daily")
    @Transactional
    public void notifyUsers() {

        List<Event> events = eventService.getEvents();
        for (Event event : events) {

            if (event.getEndDate().isBefore(LocalDate.now()))
                continue;

            EventStatistic statistic = eventService.getEventStatistic(event.getId());

            if (statistic.getTotalTickets() - statistic.getSoldTickets() == 0)
                continue;

            mailService.sendEventStatisticMail(event, statistic);

            if (event.getStartDate().isAfter(LocalDate.now().plusDays(DAYS_TILL_NOTIFICATION)))
                continue;

            List<AssignedDistributor> distributorModels = assignedDistributorService.getAssignedDistributors(event.getId());
            for (AssignedDistributor distributorModel : distributorModels) {
                mailService.sendEventTicketsNotSold(distributorModel.getDistributor(), event, statistic);
            }

            mailService.sendEventTicketsNotSold(event.getOrganizer(), event, statistic);
        }
    }
}

