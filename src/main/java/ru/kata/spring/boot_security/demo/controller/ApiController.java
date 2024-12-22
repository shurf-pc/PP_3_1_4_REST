package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.RolesDTO;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/")

public class ApiController {
    private final UserService userServiceImp;
    private final RoleService roleServiceImp;

    public ApiController(UserService userService, RoleService roleService) {
        this.userServiceImp = userService;
        this.roleServiceImp = roleService;
    }

    @GetMapping("admin/getallusers")
    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = userServiceImp.getAllUsers().stream().map(UserDTO::new).collect(Collectors.toList());
        users.forEach(System.out::println);
        return users;
    }

    @GetMapping("getcurrentuser")
    public UserDTO getCurrentUser(@AuthenticationPrincipal User user) {
        return new UserDTO(user);
    }

    @GetMapping("admin/getallroles")
    public List<RolesDTO> getAllRoles() {
        List<RolesDTO> roles = roleServiceImp.getAllRoles().stream().map(RolesDTO::new).collect(Collectors.toList());
        roles.forEach(System.out::println);
        return roles;
    }

    @DeleteMapping("admin/deleteuser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        System.out.println("Deleting user with id: " + id);
        try {
            userServiceImp.deleteUser(id);
            System.out.println("Deleted user with id: " + id + "successfully");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("csrf-token")
    public Map<String, String> getCsrfToken(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        System.out.println("CSRF TOKEN = " + csrfToken.getToken());
        System.out.println("CSRF HEADER = " + csrfToken.getHeaderName());
        return Map.of("csrfToken", csrfToken.getToken(), "csrfHeader", csrfToken.getHeaderName());
    }

    @PutMapping("admin/updateuser")
    public ResponseEntity<Void> updateUser(@RequestBody UserDTO userDTO) {
        System.out.println("Updating userDTO " + userDTO);
        System.out.println("Long id " + Long.parseLong(userDTO.getId()) + ", UserDto: " + userDTO.getId());
        User user = new User();
        user.setId(Long.parseLong(userDTO.getId()));
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setAuthorities(userDTO.getRoles().stream()
                .map(x -> {
                    Role role = new Role();
                    role.setId(Long.parseLong(x));
                    return role;
                })
                .collect(Collectors.toList()));
        System.out.println("Updated user User: " + user);
        try {
            userServiceImp.updateUser(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("admin/addnewuser")
    public ResponseEntity<Void> addUser(@RequestBody UserDTO userDTO) {
        System.out.println("Updating userDTO " + userDTO);
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setAuthorities(userDTO.getRoles().stream()
                .map(x -> {
                    Role role = new Role();
                    role.setId(Long.parseLong(x));
                    return role;
                })
                .collect(Collectors.toList()));
        try {
            userServiceImp.addUser(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
