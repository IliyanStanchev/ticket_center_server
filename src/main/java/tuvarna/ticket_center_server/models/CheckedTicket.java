package tuvarna.ticket_center_server.models;

import jakarta.persistence.*;

@Entity(name = "CHECKED_TICKETS")
public class CheckedTicket {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "TICKET_ID")
    private Ticket ticket;

    public CheckedTicket() {
    }

    public CheckedTicket(Ticket ticket) {
        this.ticket = ticket;
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
}
