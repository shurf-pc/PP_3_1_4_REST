package ru.kata.spring.boot_security.demo.dto;

import ru.kata.spring.boot_security.demo.model.Role;

public class RolesDTO {
    String id;
    String role;

    public RolesDTO() {
    }

    public RolesDTO(Role role) {
        this.id = role.getId().toString();
        this.role = role.getAuthority().replaceAll("ROLE_", "");
    }

    @Override
    public String toString() {
        return "RolesDTO{" +
                "id='" + id + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
