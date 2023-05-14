package tuvarna.ticket_center_server.models;

import jakarta.persistence.*;

@Entity(name = "INVOICES")
public class Invoice {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PRICE")
    private float price;

    @Column(name = "PURCHASER_MAIL", length = 128)
    private String purchaserMail;

    @Column(name = "PURCHASER_NAME", length = 128)
    private String purchaserName;

    @Column(name = "PURCHASER_PHONE", length = 128)
    private String purchaserPhone;

    @ManyToOne
    @JoinColumn(name = "EVENT_USER_ID")
    private EventUser eventUser;

    public Invoice() {
    }

    public Invoice(float price, String purchaserMail, String purchaserName, String purchaserPhone, EventUser eventUser) {
        this.price = price;
        this.purchaserMail = purchaserMail;
        this.purchaserName = purchaserName;
        this.purchaserPhone = purchaserPhone;
        this.eventUser = eventUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPurchaserMail() {
        return purchaserMail;
    }

    public void setPurchaserMail(String purchaserMail) {
        this.purchaserMail = purchaserMail;
    }

    public String getPurchaserName() {
        return purchaserName;
    }

    public void setPurchaserName(String purchaserName) {
        this.purchaserName = purchaserName;
    }

    public String getPurchaserPhone() {
        return purchaserPhone;
    }

    public void setPurchaserPhone(String purchaserPhone) {
        this.purchaserPhone = purchaserPhone;
    }

    public EventUser getEventUser() {
        return eventUser;
    }

    public void setEventUser(EventUser eventUser) {
        this.eventUser = eventUser;
    }
}
