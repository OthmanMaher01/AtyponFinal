package com.example.BootStrapper.Controllers;


import com.example.BootStrapper.Authentication.User;
import com.example.BootStrapper.Authentication.UsersManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("AtyponDatabase")
public class AuthenticationController {

    @PostMapping("register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> credentials) throws IOException, ServletException {
        String username = credentials.get("email");
        String password = credentials.get("password");
        User user = new User(username, password);

        String userNode = UsersManager.getInstance().register(user);
        return ResponseEntity.ok("register successful");
    }

    @PostMapping("login")
    public ResponseEntity<String> loginUser(@RequestBody Map<String, String> credentials) throws IOException, ServletException {
        String username = credentials.get("email");
        String password = credentials.get("password");

        System.out.println(username + "  " + password);

        boolean isValidUser = false;
        isValidUser = UsersManager.getInstance().Authenticate(username, password);

        System.out.println(isValidUser);

        if (isValidUser)
        {
            return ResponseEntity.ok("Login successful");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials!!");
        }
    }
}
