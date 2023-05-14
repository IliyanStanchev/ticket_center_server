package tuvarna.ticket_center_server.providers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MailTemplateProvider {

    public static final String COMPANY_ADDRESS = "Varna 9000, Gotse Delchev 9";
    public static final String COMPANY_PHONE = "+359 08982231";
    public static final String COMPANY_EMAIL = "iliyan.stanchevv@gmail.com";

    public static final String REGISTRATION_TEMPLATE_PATH = "src/main/resources/templates/registrationMail.html";
    public static final String PURCHASE_TEMPLATE_PATH = "src/main/resources/templates/purchasedTicketMail.html";
    public static final String EVENT_STATISTIC_TEMPLATE_PATH = "src/main/resources/templates/eventStatisticMail.html";
    public static final String EVENT_TICKETS_NOT_SOLD_TEMPLATE_PATH = "src/main/resources/templates/eventTicketsNotSoldMail.html";

    private static String getTemplate(String htmlPath) {

        return convertHtmlToString(htmlPath)
                .replace("{ADDRESS}", COMPANY_ADDRESS)
                .replace("{PHONE}", COMPANY_PHONE)
                .replace("{EMAIL}", COMPANY_EMAIL);
    }

    public static String convertHtmlToString(String htmlPath) {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(htmlPath));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
            return "";
        }

        return contentBuilder.toString();
    }

    public static String getRegistrationMailTemplate() {
        return getTemplate(REGISTRATION_TEMPLATE_PATH);
    }

    public static String getPurchasedMailTemplate() {
        return getTemplate(PURCHASE_TEMPLATE_PATH);
    }

    public static String getEventStatisticMail() {
        return getTemplate(EVENT_STATISTIC_TEMPLATE_PATH);
    }

    public static String getEventTicketsNotSoldTemplate() {
        return getTemplate(EVENT_TICKETS_NOT_SOLD_TEMPLATE_PATH);
    }
}
