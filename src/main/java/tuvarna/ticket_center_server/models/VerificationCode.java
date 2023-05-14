package tuvarna.ticket_center_server.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "VERIFICATION_CODES")
public class VerificationCode {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CODE", length = 64)
    private String code;

    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name = "EXPIRATION_DATE")
    private LocalDateTime expirationDate;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public VerificationCode() {
    }

    public VerificationCode(String code, LocalDateTime creationDate, LocalDateTime expirationDate, User user) {
        this.code = code;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
