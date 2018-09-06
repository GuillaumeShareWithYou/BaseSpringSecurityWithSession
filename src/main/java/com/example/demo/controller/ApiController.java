package com.example.demo.controller;

import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final UserService userService;

    private RoleService roleService;


    public ApiController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }




}
