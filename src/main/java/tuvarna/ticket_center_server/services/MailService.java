package tuvarna.ticket_center_server.services;

import tuvarna.ticket_center_common.models.RegistrationModel;
import tuvarna.ticket_center_server.data.EventStatistic;
import tuvarna.ticket_center_server.models.Event;
import tuvarna.ticket_center_server.models.EventUser;
import tuvarna.ticket_center_server.models.PurchasedTicket;

public interface MailService {

    boolean sendRegistrationMail(RegistrationModel registrationModel, String verificationCode);

    boolean sendTicketPurchaseMail(PurchasedTicket purchasedTicket);

    void sendEventStatisticMail(Event event, EventStatistic statistic);

    void sendEventTicketsNotSold(EventUser distributor, Event event, EventStatistic statistic);
}
