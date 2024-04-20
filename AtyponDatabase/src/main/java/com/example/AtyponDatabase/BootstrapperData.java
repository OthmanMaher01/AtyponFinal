package com.example.AtyponDatabase;

import lombok.Data;

import java.util.List;

@Data
public class BootstrapperData {
    private List<String> ports;

    @Override
    public String toString() {
        return "BootstrapperData{" +
                "ports=" + ports +
                '}';
    }
}
