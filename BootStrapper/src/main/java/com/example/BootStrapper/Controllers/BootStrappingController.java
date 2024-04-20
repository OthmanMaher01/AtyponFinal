package com.example.BootStrapper.Controllers;

import com.example.BootStrapper.AsyncRequests;
import com.example.BootStrapper.Authentication.User;
import com.example.BootStrapper.Authentication.UsersManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("database")
@RequiredArgsConstructor
public class BootStrappingController {
    private final AsyncRequests asyncRequests = new AsyncRequests();

    @PostMapping("")
    public void getStartupMessage(@RequestBody InitPingMessage initPingMessage) throws InterruptedException {
        asyncRequests.sync(initPingMessage.getPort());
    }
}
