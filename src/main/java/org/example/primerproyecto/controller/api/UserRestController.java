package org.example.primerproyecto.controller.api;


import jakarta.servlet.http.HttpServletRequest;
import org.example.primerproyecto.dto.UserDTO;
import org.example.primerproyecto.entity.Role;
import org.example.primerproyecto.entity.User;
import org.example.primerproyecto.service.UserService;
import org.example.primerproyecto.util.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String email = jwtService.extractAllClaims(token).getSubject();

            // Assuming your UserService has a findByEmail method
            User user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(404).body("User not found");
            }

            // Convert user entity to DTO
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setEmail(user.getEmail());
            userDTO.setRoles(user.getRoles().stream()
                    .map(Role::getName) // Assuming Role has getName method
                    .collect(java.util.stream.Collectors.toList()));

            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }
}
