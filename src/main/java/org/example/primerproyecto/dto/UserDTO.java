package org.example.primerproyecto.dto;

import org.example.primerproyecto.entity.Role;
import org.example.primerproyecto.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {

    private Long id;
    private String email;
    private List<String> roles;

    public UserDTO() {
    }

    public UserDTO(Long id, String email, List<String> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
    }

    // Getters and setters
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    // Conversion from Entity to DTO
    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());

        if (user.getRoles() != null) {
            dto.setRoles(user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    // Conversion from DTO to Entity (partial, excludes password)
    public User toEntity() {
        User user = new User();
        user.setId(this.id);
        user.setEmail(this.email);
        // Note: roles and password would need to be set separately
        return user;
    }
}