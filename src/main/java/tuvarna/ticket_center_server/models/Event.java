package tuvarna.ticket_center_server.models;

import jakarta.persistence.*;
import tuvarna.ticket_center_common.enumerables.EventTypes;
import tuvarna.ticket_center_common.models.EventModel;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity(name = "EVENTS")
public class Event {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", length = 128)
    private String name;

    @Column(name = "DESCRIPTION", length = 1024)
    private String description;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "START_TIME")
    private LocalTime startTime;

    @Column(name = "EVENT_TYPE")
    private EventTypes eventType;

    @Column(name = "LOCATION", length = 256)
    private String location;

    @ManyToOne
    @JoinColumn(name = "ORGANIZER_ID")
    private EventUser organizer;

    public Event() {
    }

    public Event(String name, String description, LocalDate startDate, LocalDate endDate, LocalTime startTime, EventTypes eventType) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.eventType = eventType;
    }

    public Event(EventModel eventModel, EventUser organizer) {

        this.name = eventModel.getName();
        this.description = eventModel.getDescription();
        this.startDate = LocalDate.parse(eventModel.getStartDate());
        this.endDate = LocalDate.parse(eventModel.getEndDate());
        this.startTime = LocalTime.parse(eventModel.getStartTime());
        this.eventType = eventModel.getEventType();
        this.location = eventModel.getLocation();
        this.organizer = organizer;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public EventTypes getEventType() {
        return eventType;
    }

    public void setEventType(EventTypes eventType) {
        this.eventType = eventType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public EventUser getOrganizer() {
        return organizer;
    }

    public void setOrganizer(EventUser organizer) {
        this.organizer = organizer;
    }
}
