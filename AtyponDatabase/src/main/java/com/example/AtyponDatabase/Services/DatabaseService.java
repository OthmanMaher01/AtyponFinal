package com.example.AtyponDatabase.Services;

import com.example.AtyponDatabase.Database.Database;
import com.example.AtyponDatabase.Managers.DatabaseManager;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseService {
    public void createDatabase(String databaseName) {
//        DatabaseManager.getInstance().getDatabaseLock().writeLock().lock();
        try {
            String databasesFolderPath = System.getProperty("user.dir") + "/databases";
            File file = new File(databasesFolderPath, databaseName);

            if (!file.exists()) {
                file.mkdir();
                Database database = new Database();
                database.setName(databaseName);
                DatabaseManager.getInstance().getDatabases().put(databaseName,database);
                DatabaseManager.getInstance().getDatabaseLock().put(databaseName, new ReentrantReadWriteLock());
            }
        }
        finally {
//            DatabaseManager.getInstance().getDatabaseLock().writeLock().unlock();
        }
    }

    public void deleteDatabase(String databaseName) {
        DatabaseManager.getInstance().getDatabaseLock().get(databaseName).writeLock().lock();
        boolean flag =false;
        try {
            DatabaseManager.getInstance().getDatabases().remove(databaseName);
            String databasesFolderPath = System.getProperty("user.dir") + "/databases";
            File database = new File(databasesFolderPath, databaseName);
            FileSystemUtils.deleteRecursively(database);
            flag=true;
        }
        finally {
            DatabaseManager.getInstance().getDatabaseLock().get(databaseName).writeLock().unlock();
            if(flag) {
                DatabaseManager.getInstance().getDatabaseLock().remove(databaseName);
            }
        }
    }

    public List<String> getAllDatabases() {
        DatabaseManager.getInstance().getDatabaseLock().forEach((s, reentrantReadWriteLock) -> reentrantReadWriteLock.readLock().lock());
        try {
            List<String> databasesNames = new ArrayList<>();
            for (String databasesName : DatabaseManager.getInstance().getDatabases().keySet()) {
                databasesNames.add(databasesName);
            }
            return databasesNames;
        }
        finally {
            DatabaseManager.getInstance().getDatabaseLock().forEach((s, reentrantReadWriteLock) -> reentrantReadWriteLock.readLock().unlock());
        }
    }
}
