package tuvarna.ticket_center_server.models;

import jakarta.persistence.*;
import tuvarna.ticket_center_common.models.TicketKindModel;

@Entity(name = "TICKET_KINDS")
public class TicketKind {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", length = 128)
    private String name;

    @Column(name = "DESCRIPTION", length = 256)
    private String description;

    @Column(name = "PRICE")
    private float price;

    @Column(name = "LIMIT_PER_BUYER")
    private int limitPerBuyer;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private Event event;

    public TicketKind() {
    }

    public TicketKind(String name, String description, float price, int limitPerBuyer, Event event) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.limitPerBuyer = limitPerBuyer;
        this.event = event;
    }

    public TicketKind(TicketKindModel ticketKindName, Event event) {

        this.name = ticketKindName.getName();
        this.description = ticketKindName.getDescription();
        this.price = ticketKindName.getPrice();
        this.limitPerBuyer = ticketKindName.getLimitPerBuyer();
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getLimitPerBuyer() {
        return limitPerBuyer;
    }

    public void setLimitPerBuyer(int limitPerBuyer) {
        this.limitPerBuyer = limitPerBuyer;
    }
}
