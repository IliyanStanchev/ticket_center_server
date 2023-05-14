package tuvarna.ticket_center_server.models;

import jakarta.persistence.*;

@Entity(name = "PURCHASED_TICKETS")
public class PurchasedTicket {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TICKET_ID")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "INVOICE_ID")
    private Invoice invoice;

    public PurchasedTicket() {
    }

    public PurchasedTicket(Ticket ticket, Invoice invoice) {
        this.ticket = ticket;
        this.invoice = invoice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
