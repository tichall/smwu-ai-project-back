package smwu.server.domain.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/users/login-page")
    public String loginPage() {
        return "login";
    }
}