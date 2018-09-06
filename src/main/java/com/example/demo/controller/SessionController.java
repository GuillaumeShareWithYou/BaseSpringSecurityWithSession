package com.example.demo.controller;

import com.example.demo.dto.CredentialsDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.tools.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("/session")
public class SessionController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    @Autowired
    public SessionController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserDto register(CredentialsDto credentials, HttpServletResponse response) throws IOException {

        System.out.println(String.format("register with username : %s and password : %s and email %s",
                credentials.getUserName(),
                credentials.getPassword(),
                credentials.getEmail()));
        UserDto userDto;
        try {
            userDto = userService.create(credentials);
            response.setStatus(201);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(400, "Email or login already used.");
            return null;
        }
        return userDto;
    }

    @GetMapping("/info")
    public Object getUser() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof String) {
            return principal;
        }
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Converter.map(user, UserDto.class);
    }

    @PostMapping("/logout")
    @ResponseStatus(code = HttpStatus.OK)
    public String logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();
        response.setStatus(200);
        return "You have been logged out successfully";
    }
}
