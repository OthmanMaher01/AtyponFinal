package com.example.AtyponDatabase.Managers;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClusterManager {
    private static volatile ClusterManager instance;
    private List<String> nodes = new ArrayList<>();
    public ClusterManager() {
    }

    public static ClusterManager getInstance() {
        ClusterManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (ClusterManager.class) {
            if (instance == null) {
                instance = new ClusterManager();
            }
            return instance;
        }
    }
}
