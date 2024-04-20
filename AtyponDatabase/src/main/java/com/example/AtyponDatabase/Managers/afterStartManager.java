package com.example.AtyponDatabase.Managers;

import com.example.AtyponDatabase.Services.BootstrapperService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class afterStartManager implements ApplicationRunner {
    private final BootstrapperService bootstrappingService;

    public afterStartManager(BootstrapperService bootstrappingService) {
        this.bootstrappingService = bootstrappingService;
    }

    @Override
    public void run(ApplicationArguments args) {
        bootstrappingService.pingBootstrapperForInitData();
    }
}
