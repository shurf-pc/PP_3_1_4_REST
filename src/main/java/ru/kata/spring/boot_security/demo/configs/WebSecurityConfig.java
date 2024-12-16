package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(UserDetailsService userDetailsService, SuccessUserHandler successUserHandler, PasswordEncoder passwordEncoder) {
        this.successUserHandler = successUserHandler;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN") // Доступ только для роли ADMIN
                .antMatchers("/user/**").hasRole("USER")   // Доступ только для роли USER
                .antMatchers("/", "/login").permitAll()    // Доступ к главной странице и странице логина для всех
                .anyRequest().authenticated()              // Все остальные запросы требуют аутентификации
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(successUserHandler)       // Обработчик успешной аутентификации
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout")        // URL после выхода
                .permitAll();
    }
}