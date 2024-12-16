package ru.kata.spring.boot_security.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseFiller implements CommandLineRunner {
    private final UserService userServiceImp;
    private final RoleService roleServiceImp;

    public DatabaseFiller(UserService userServiceImp, RoleService roleServiceImp) {
        this.userServiceImp = userServiceImp;
        this.roleServiceImp = roleServiceImp;
    }

    @Override
    public void run(String... args) throws Exception {

        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");

        roleServiceImp.addRole(roleAdmin);
        roleServiceImp.addRole(roleUser);

        List<Role> roles_admin = new ArrayList<>();
        roles_admin.add(roleAdmin);
        roles_admin.add(roleUser);

        List<Role> roles_user = new ArrayList<>();
        roles_user.add(roleUser);

        userServiceImp.addUser(new User("Admin", "admin", "admin@email.com", roles_admin));
        for (int i = 1; i < 6; i++) {
            userServiceImp.addUser(new User("User" + i, "user" + i, "user" + i + "@email.com", roles_user));
        }
    }
}