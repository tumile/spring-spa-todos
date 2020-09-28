package com.example.server.user;

import com.example.server.user.dto.IDTokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser(Authentication authentication) {
        User user = userService.getUser(Long.parseLong(authentication.getName()));
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/auth/google")
    public ResponseEntity<Object> loginOAuthGoogle(@RequestBody IDTokenRequest requestBody, HttpServletResponse response) {
        String authToken = userService.loginOAuthGoogle(requestBody);
        Cookie cookie = new Cookie("AUTH-TOKEN", authToken);
        cookie.setMaxAge(7 * 24 * 3600); // expires in 7 days
        // cookie.setSecure(true); // use with HTTPS
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
