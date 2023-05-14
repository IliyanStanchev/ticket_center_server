package tuvarna.ticket_center_server.models;

import jakarta.persistence.*;
import tuvarna.ticket_center_common.enumerables.UserStatuses;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "USERS")
public class User {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EMAIL", length = 128, unique = true)
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name = "LAST_LOGIN_DATE")
    private LocalDateTime lastLoginDate;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    @Column(name = "STATUS")
    private UserStatuses status;

    public User() {
    }

    public User(String email, String password, LocalDateTime creationDate, LocalDateTime lastLoginDate, Role role, UserStatuses status) {
        this.email = email;
        this.password = password;
        this.creationDate = creationDate;
        this.lastLoginDate = lastLoginDate;
        this.role = role;
        this.status = status;
    }

    public UserStatuses getStatus() {
        return status;
    }

    public void setStatus(UserStatuses status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(creationDate, user.creationDate) && Objects.equals(lastLoginDate, user.lastLoginDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, creationDate, lastLoginDate);
    }
}
