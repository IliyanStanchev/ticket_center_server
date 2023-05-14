package tuvarna.ticket_center_server.models;

import jakarta.persistence.*;
import tuvarna.ticket_center_common.models.TicketModel;

@Entity(name = "TICKETS")
public class Ticket {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOTE", length = 64)
    private String note;

    @ManyToOne
    @JoinColumn(name = "TICKET_KIND_ID")
    private TicketKind ticketKind;

    public Ticket() {
    }

    public Ticket(String note, TicketKind ticketKind) {
        this.note = note;
        this.ticketKind = ticketKind;
    }

    public Ticket(TicketModel ticketModel, TicketKind ticketKind) {

        this.note = ticketModel.getNote();
        this.ticketKind = ticketKind;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public TicketKind getTicketKind() {
        return ticketKind;
    }

    public void setTicketKind(TicketKind ticketKind) {
        this.ticketKind = ticketKind;
    }
}
