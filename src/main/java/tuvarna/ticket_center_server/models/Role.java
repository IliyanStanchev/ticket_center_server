package tuvarna.ticket_center_server.models;

import jakarta.persistence.*;
import tuvarna.ticket_center_common.enumerables.RoleTypes;

@Entity(name = "ROLES")
public class Role {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ROLE_TYPE")
    private RoleTypes roleType;

    public Role() {
    }

    public Role(Long id, RoleTypes roleType) {
        this.id = id;
        this.roleType = roleType;
    }

    public RoleTypes getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleTypes roleType) {
        this.roleType = roleType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
