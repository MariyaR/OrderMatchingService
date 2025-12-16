package com.example.OrderMatchingService.controller;

import com.example.OrderMatchingService.dto.UserAccountDto;
import com.example.OrderMatchingService.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/trade-api/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    //todo transfer to user service

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/account")
    public UserAccountDto getUserAccount(@AuthenticationPrincipal Jwt jwt) {
      String username = jwt.getClaim("preferred_username");
      UUID userID = userService.getUserIdByKeycloakUsername(username);
      return userService.getAccountByUserId(userID);
    }
}
