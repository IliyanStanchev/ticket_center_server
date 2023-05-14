package tuvarna.ticket_center_server.providers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MailTemplateProviderTests {

    @Test
    public void testGetRegistrationMailTemplate() {
        String template = MailTemplateProvider.getRegistrationMailTemplate();
        validateTemplate(template);
    }

    @Test
    public void testGetPurchasedMailTemplate() {
        String template = MailTemplateProvider.getPurchasedMailTemplate();
        validateTemplate(template);
    }

    @Test
    public void testGetEventStatisticMail() {
        String template = MailTemplateProvider.getEventStatisticMail();
        validateTemplate(template);
    }

    @Test
    public void testGetEventTicketsNotSoldTemplate() {
        String template = MailTemplateProvider.getEventTicketsNotSoldTemplate();
        validateTemplate(template);
    }

    private void validateTemplate(String template) {
        // Check if the template contains the company address, phone, and email
        assertTrue(template.contains(MailTemplateProvider.COMPANY_ADDRESS));
        assertTrue(template.contains(MailTemplateProvider.COMPANY_PHONE));
        assertTrue(template.contains(MailTemplateProvider.COMPANY_EMAIL));

        // Check if the template is not empty
        assertTrue(template.length() > 0);

        // Additional validation specific to your use case can be added here
    }

    @Test
    public void testConvertHtmlToString() {
        String htmlPath = "src/main/resources/templates/registrationMail.html";
        String content = MailTemplateProvider.convertHtmlToString(htmlPath);
        // Perform additional assertions or validations on the converted HTML content
        assertTrue(content.contains("<html"));
        assertTrue(content.contains("<body"));
        assertTrue(content.contains("</body>"));
        assertTrue(content.contains("</html>"));
    }
}