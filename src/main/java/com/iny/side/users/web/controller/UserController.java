package com.iny.side.users.web.controller;

import com.iny.side.users.application.service.UserService;
import com.iny.side.users.web.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signup(AccountDto accountDto) {
        userService.createUser(accountDto);
        return "redirect:/login";
    }
}
