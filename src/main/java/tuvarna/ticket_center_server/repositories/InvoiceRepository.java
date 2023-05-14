package tuvarna.ticket_center_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tuvarna.ticket_center_server.models.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
