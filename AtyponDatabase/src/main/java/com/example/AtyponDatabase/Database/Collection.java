package com.example.AtyponDatabase.Database;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Data
public class Collection {
    private String name;
    private List<String> documents = new ArrayList<>();
    private Map<String, Map<String, DocumentIndex>> indexes = new HashMap<>();
    private Map<String, ReentrantReadWriteLock> documentLock = new HashMap<>();
}
