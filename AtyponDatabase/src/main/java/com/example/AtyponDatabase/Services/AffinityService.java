package com.example.AtyponDatabase.Services;

import org.springframework.stereotype.Service;

@Service
public class AffinityService {
    private int totalNodes = 2;
    private int counter = 0;
    public int calculateAffinity() {
        synchronized (this) {
            counter = (counter + 1) % totalNodes;
            return counter;
        }
    }
}
