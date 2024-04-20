package com.example.AtyponDatabase.Managers;

import com.example.AtyponDatabase.Database.Database;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Data
public class DatabaseManager {
    private static volatile DatabaseManager instance;
    private HashMap<String, Database> databases = new HashMap<>();
    private Map<String, ReentrantReadWriteLock> databaseLock = new HashMap<>();


    public DatabaseManager() {
    }

    public static DatabaseManager getInstance() {
        DatabaseManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (DatabaseManager.class) {
            if (instance == null) {
                instance = new DatabaseManager();
            }
            return instance;
        }
    }
}
