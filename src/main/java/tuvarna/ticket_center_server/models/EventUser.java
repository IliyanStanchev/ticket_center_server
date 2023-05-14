package tuvarna.ticket_center_server.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "EVENT_USERS")
public class EventUser {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FIRST_NAME", length = 64)
    private String firstName;

    @Column(name = "LAST_NAME", length = 64)
    private String lastName;


    @Column(name = "PHONE_NUMBER", length = 32, unique = true)
    private String phoneNumber;

    @Column(name = "HONORARIUM")
    private float honorarium;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public EventUser() {
    }

    public EventUser(String firstName, String lastName, String phoneNumber, float honorarium, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.honorarium = honorarium;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public float getHonorarium() {
        return honorarium;
    }

    public void setHonorarium(float honorarium) {
        this.honorarium = honorarium;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventUser eventUser = (EventUser) o;
        return Float.compare(eventUser.honorarium, honorarium) == 0 && Objects.equals(id, eventUser.id) && Objects.equals(firstName, eventUser.firstName) && Objects.equals(lastName, eventUser.lastName) && Objects.equals(phoneNumber, eventUser.phoneNumber) && Objects.equals(user, eventUser.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, phoneNumber, honorarium, user);
    }

    public String getName() {

        return this.firstName + " " + this.lastName;
    }
}
