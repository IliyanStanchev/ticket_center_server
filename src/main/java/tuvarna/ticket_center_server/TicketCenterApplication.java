package tuvarna.ticket_center_server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class TicketCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketCenterApplication.class, args);
	}

}
