package com.springsecuritytutorial.demo.session;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SessionController {

    @PostMapping("login")
    public String login() {
        return "login";
    }
}
