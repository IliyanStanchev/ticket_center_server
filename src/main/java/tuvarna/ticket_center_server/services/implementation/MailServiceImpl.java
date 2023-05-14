package tuvarna.ticket_center_server.services.implementation;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tuvarna.ticket_center_common.models.RegistrationModel;
import tuvarna.ticket_center_server.data.EventStatistic;
import tuvarna.ticket_center_server.data.MailMessage;
import tuvarna.ticket_center_server.enumerables.LogLevels;
import tuvarna.ticket_center_server.models.Event;
import tuvarna.ticket_center_server.models.EventUser;
import tuvarna.ticket_center_server.models.PurchasedTicket;
import tuvarna.ticket_center_server.providers.MailTemplateProvider;
import tuvarna.ticket_center_server.services.MailService;


@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private Environment environment;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private LogServiceImpl logService;

    private boolean sendEmail(MailMessage message) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(String.valueOf(new InternetAddress(message.getSender())));
            mimeMessageHelper.setTo(message.getReceiver());
            mimeMessageHelper.setSubject(message.getSubject());
            mimeMessageHelper.setText(message.getContent(), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (Exception e) {
            logService.log(LogLevels.ERROR, "JavaMailSender::send", e.getMessage(), "MailService::sendEmail");
            return false;
        }
        return true;
    }

    public boolean sendRegistrationMail(RegistrationModel registrationModel, String verificationCode) {

        MailMessage message = new MailMessage();

        message.setSender(environment.getProperty("spring.mail.username"));
        message.setReceiver(registrationModel.getEmail());

        message.setSubject("Welcome");

        final String messageContent = MailTemplateProvider.getRegistrationMailTemplate()
                .replace("{NAME}", registrationModel.getFirstName() + " " + registrationModel.getLastName())
                .replace("{VERIFICATION_CODE}", verificationCode);

        message.setContent(messageContent);

        if (!sendEmail(message))
            return false;

        return true;
    }

    @Override
    public boolean sendTicketPurchaseMail(PurchasedTicket purchasedTicket) {

        MailMessage message = new MailMessage();

        message.setSender(environment.getProperty("spring.mail.username"));
        message.setReceiver(purchasedTicket.getInvoice().getPurchaserMail());

        message.setSubject("Ticket purchase");

        final String messageContent = MailTemplateProvider.getPurchasedMailTemplate()
                .replace("{NAME}", purchasedTicket.getInvoice().getPurchaserName())
                .replace("{EVENT_NAME}", purchasedTicket.getTicket().getTicketKind().getEvent().getName())
                .replace("{EVENT_LOCATION}", purchasedTicket.getTicket().getTicketKind().getEvent().getLocation())
                .replace("{EVENT_START_DATE}", purchasedTicket.getTicket().getTicketKind().getEvent().getStartDate() + " " + purchasedTicket.getTicket().getTicketKind().getEvent().getStartTime())
                .replace("{TICKET_KIND_NAME}", purchasedTicket.getTicket().getTicketKind().getName())
                .replace("{TICKET_NOTE}", purchasedTicket.getTicket().getNote())
                .replace("{TICKET_PRICE}", String.valueOf(purchasedTicket.getTicket().getTicketKind().getPrice()));

        message.setContent(messageContent);

        if (!sendEmail(message))
            return false;

        return true;

    }

    @Override
    public void sendEventStatisticMail(Event event, EventStatistic statistic) {

        MailMessage message = new MailMessage();

        message.setSender(environment.getProperty("spring.mail.username"));
        message.setReceiver(event.getOrganizer().getUser().getEmail());

        message.setSubject("Event statistic");

        final String messageContent = MailTemplateProvider.getEventStatisticMail()
                .replace("{NAME}", event.getOrganizer().getName())
                .replace("{EVENT_NAME}", event.getName())
                .replace("{EVENT_LOCATION}", event.getLocation())
                .replace("{EVENT_START_DATE}", event.getStartDate() + " " + event.getStartTime())
                .replace("{TICKETS}", String.valueOf(statistic.getTotalTickets()))
                .replace("{REMAINING_TICKETS}", String.valueOf(statistic.getTotalTickets() - statistic.getSoldTickets()));

        message.setContent(messageContent);

        sendEmail(message);
    }

    @Override
    public void sendEventTicketsNotSold(EventUser distributor, Event event, EventStatistic statistic) {

        MailMessage message = new MailMessage();

        message.setSender(environment.getProperty("spring.mail.username"));
        message.setReceiver(distributor.getUser().getEmail());

        message.setSubject("Event notification");

        final String messageContent = MailTemplateProvider.getEventTicketsNotSoldTemplate()
                .replace("{NAME}", distributor.getName())
                .replace("{EVENT_NAME}", event.getName())
                .replace("{EVENT_LOCATION}", event.getLocation())
                .replace("{EVENT_START_DATE}", event.getStartDate() + " " + event.getStartTime())
                .replace("{REMAINING_TICKETS}", String.valueOf(statistic.getTotalTickets() - statistic.getSoldTickets()));

        message.setContent(messageContent);

        sendEmail(message);
    }
}
