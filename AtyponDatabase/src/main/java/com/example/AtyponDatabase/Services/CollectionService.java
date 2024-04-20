package com.example.AtyponDatabase.Services;

import com.example.AtyponDatabase.Database.Collection;
import com.example.AtyponDatabase.Managers.DatabaseManager;
import com.example.AtyponDatabase.Managers.Filters;
import com.example.AtyponDatabase.Validation.SchemaValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CollectionService {
    @Autowired
    private SchemaValidator schemaValidator;
    @Autowired
    private Filters filters;
    @Autowired
    private AffinityService affinityService;
    public String createCollection(String databaseName,String collectionName, String schema) throws IOException {
//        DatabaseManager.getInstance().getDatabases().get(databaseName).getCollectionLock().writeLock().lock();
        try {
            if(isValid(schema)) {

                String databaseFolderPath = System.getProperty("user.dir") + "/databases/" + databaseName;
                File file = new File(databaseFolderPath, collectionName);

                // Create the folder if it doesn't exist.
                if (!file.exists()) {
                    file.mkdirs();
                    DatabaseManager.getInstance().getDatabases().get(databaseName).getCollectionLock().put(collectionName, new ReentrantReadWriteLock());

                    Collection collection = new Collection();
                    collection.setName(collectionName);
                    DatabaseManager.getInstance().getDatabases().get(databaseName).getCollections().put(collectionName, collection);

                    createSchemaFile(databaseName, collectionName, schema);
                    createIndex(databaseName, collectionName, schema);
                    setCollectionAffinity(databaseName,collectionName);
                    return "Collection created successfully";
                }
                else
                {
                    return "Invalid collection name";
                }
            }
            else
            {
                return "The schema is not valid. Please check the Data Types and try again.";
            }
        } finally {
//            DatabaseManager.getInstance().getDatabases().get(databaseName).getCollectionLock().writeLock().unlock();
        }

    }
    public void deleteCollection(String databaseName,String collectionName) throws IOException {
        DatabaseManager.getInstance().getDatabases().get(databaseName).getCollectionLock().get(collectionName).writeLock().lock();
        boolean flag = false;
        try {
            DatabaseManager.getInstance().getDatabases().get(databaseName).getCollections().remove(collectionName);
            String databaseFolderPath = System.getProperty("user.dir") + "/databases/" + databaseName;
            File collection = new File(databaseFolderPath, collectionName);
            FileSystemUtils.deleteRecursively(collection);
            flag=true;
        } finally {
            DatabaseManager.getInstance().getDatabases().get(databaseName).getCollectionLock().get(collectionName).writeLock().unlock();
            if(flag) {
                DatabaseManager.getInstance().getDatabases().get(databaseName).getCollectionLock().remove(collectionName);
            }
        }
    }

    public boolean isValid(String schema) throws IOException {
        return schemaValidator.isValidSchema(readSchema(schema));
    }

    public Map<String, String> readSchema(String schema) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object schemaObject = mapper.readValue(schema, Object.class);
        Map<String, String> schemaMap = mapper.convertValue(schemaObject, Map.class);
        schemaMap.put("_id", "STRING");
        return schemaMap;
    }

    public void createSchemaFile(String databaseName, String collectionName, String schema) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(readSchema(schema));

        String collectionFolderPath = System.getProperty("user.dir") + "/databases/" + databaseName + "/" + collectionName;

        File schemaFile = new File(collectionFolderPath, "schema.json");
        schemaFile.createNewFile();
        FileWriter writer = new FileWriter(schemaFile);
        writer.write(json);
        writer.close();

        DatabaseManager.getInstance().getDatabases().get(databaseName).getCollections().get(collectionName).getDocuments().add("schema.json");
    }

    public ResponseEntity<JsonNode> getSchemaFile(String databaseName, String collectionName) throws IOException {
        return filters.filterById(databaseName, collectionName, "schema");
    }

    public void createIndex(String databaseName, String collectionName, String schema) throws IOException {
        DatabaseManager.getInstance().getDatabases().get(databaseName).getCollectionLock().get(collectionName).writeLock().lock();
        try {
            Map<String, String> schemaMap = readSchema(schema);
            for (String key : schemaMap.keySet()) {
                if (key != "_id") {
                    DatabaseManager.getInstance().
                            getDatabases().get(databaseName).
                            getCollections().get(collectionName).
                            getIndexes().put(key, new HashMap<>());
                }
            }
        }finally {
            DatabaseManager.getInstance().getDatabases().get(databaseName).getCollectionLock().get(collectionName).writeLock().unlock();
        }
    }

    public ResponseEntity<List<JsonNode>> filterByKey(String databaseName, String collectionName, String key) throws IOException {
        DatabaseManager.getInstance().getDatabases().get(databaseName).getCollectionLock().forEach((s, reentrantReadWriteLock) -> reentrantReadWriteLock.readLock().lock());
        try {
            return filters.filterByKey(databaseName, collectionName, key);
        } finally {
            DatabaseManager.getInstance().getDatabases().get(databaseName).getCollectionLock().forEach((s, reentrantReadWriteLock) -> reentrantReadWriteLock.readLock().unlock());
        }
    }
    public ResponseEntity<List<JsonNode>> filterByValue(String databaseName, String collectionName, String key, String value) throws IOException {
        DatabaseManager.getInstance().getDatabases().get(databaseName).getCollectionLock().forEach((s, reentrantReadWriteLock) -> reentrantReadWriteLock.readLock().lock());
        try {
            return filters.filterByValue(databaseName, collectionName, key, value);
        }finally {
            DatabaseManager.getInstance().getDatabases().get(databaseName).getCollectionLock().forEach((s, reentrantReadWriteLock) -> reentrantReadWriteLock.readLock().unlock());
        }
    }
    public ResponseEntity<JsonNode> filterById(String databaseName, String collectionName, String id) throws IOException {
        DatabaseManager.getInstance().getDatabases().get(databaseName).getCollectionLock().forEach((s, reentrantReadWriteLock) -> reentrantReadWriteLock.readLock().lock());
        try {
            return filters.filterById(databaseName, collectionName, id);
        }finally {
            DatabaseManager.getInstance().getDatabases().get(databaseName).getCollectionLock().forEach((s, reentrantReadWriteLock) -> reentrantReadWriteLock.readLock().unlock());
        }
    }

    public void setCollectionAffinity(String databaseName, String collectionName) {
        int affinity = affinityService.calculateAffinity();

        Map<String, Integer> map = DatabaseManager.getInstance().getDatabases().get(databaseName).getCollectionsAffinity();
        map.put(collectionName,affinity);
    }
    public int getCollectionAffinity(String databaseName, String collectionName)
    {
        return DatabaseManager.getInstance().getDatabases().get(databaseName).getCollectionsAffinity().get(collectionName);
    }

    public List<String> getAllCollections(String databaseName) {
        DatabaseManager.getInstance().getDatabases().get(databaseName).getCollectionLock().forEach((s, reentrantReadWriteLock) -> reentrantReadWriteLock.readLock().lock());
        try {
            List<String> CollectionsNames = new ArrayList<>();
            for (String CollectionName : DatabaseManager.getInstance().getDatabases().get(databaseName).getCollections().keySet()) {
                CollectionsNames.add(CollectionName);
            }
            return CollectionsNames;
        } finally {
            DatabaseManager.getInstance().getDatabases().get(databaseName).getCollectionLock().forEach((s, reentrantReadWriteLock) -> reentrantReadWriteLock.readLock().unlock());
        }
    }
}