package com.iny.side.users.web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping(value = "/")
    public String dashboard() {
        return "/dashboard";
    }

    @GetMapping(value = "/student")
    public String user() {
        return "/student";
    }

    @GetMapping(value = "/professor")
    public String professor() {
        return "/professor";
    }

    @GetMapping(value = "/admin")
    public String admin() {
        return "/admin";
    }

    @GetMapping(value = "/api")
    public String restDashboard() {
        return "/rest/dashboard";
    }
}