package com.example.AtyponDatabase.Database;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@Data
public class Database {
    private String name;
    private Map<String,Collection> collections = new HashMap<>();
    private Map<String, Integer> collectionsAffinity = new HashMap<>();
    private Map<String, ReentrantReadWriteLock> collectionLock = new HashMap<>();
}
