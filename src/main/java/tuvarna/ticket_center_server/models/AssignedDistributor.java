package tuvarna.ticket_center_server.models;

import jakarta.persistence.*;

@Entity(name = "ASSIGNED_DISTRIBUTORS")
public class AssignedDistributor {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "DISTRIBUTOR_ID")
    private EventUser distributor;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private Event event;

    public AssignedDistributor() {
    }

    public AssignedDistributor(EventUser distributor, Event event) {
        this.distributor = distributor;
        this.event = event;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventUser getDistributor() {
        return distributor;
    }

    public void setDistributor(EventUser distributor) {
        this.distributor = distributor;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}





