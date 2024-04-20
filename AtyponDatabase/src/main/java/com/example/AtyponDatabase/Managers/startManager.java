package com.example.AtyponDatabase.Managers;

import com.example.AtyponDatabase.Database.Collection;
import com.example.AtyponDatabase.Database.Database;
import com.example.AtyponDatabase.Database.DocumentIndex;
import com.example.AtyponDatabase.Services.BootstrapperService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

@Component
public class startManager{

    @Autowired
    private final BootstrapperService bootstrapperService;

    public startManager(BootstrapperService bootstrapperService) {
        this.bootstrapperService = bootstrapperService;
    }

    @PostConstruct
    public void startUp() throws FileNotFoundException, InterruptedException {
        getAllDatabases();
    }

    public void getAllDatabases() throws FileNotFoundException {
        String databasesFolderPath = System.getProperty("user.dir") + "/databases";
        try {
            for (File file : new File(databasesFolderPath).listFiles()) {
                String databaseName = file.getName();
                Database database = new Database();
                database.setName(databaseName);
                DatabaseManager.getInstance().getDatabases().put(databaseName, database);
                getAllCollections(databaseName);
                getCollectionsAffinity(databaseName);
            }
        } catch (Exception e){
            System.out.println("databases is empty");
        }
    }

    public void getAllCollections(String databaseName) throws FileNotFoundException {
        String databaseFolderPath = System.getProperty("user.dir") + "/databases/" + databaseName;
        try {
            for (File file : new File(databaseFolderPath).listFiles()) {
                if(!Objects.equals(file.getName(), "affinity.json")) {

                    String collectionName = file.getName();
                    Collection collection = new Collection();
                    collection.setName(collectionName);
                    DatabaseManager.getInstance().getDatabases().get(databaseName).getCollections().put(collectionName, collection);
                    getAllDocuments(databaseName, collectionName);
                    getIndex(databaseName, collectionName);
                }
            }
        } catch (Exception e){
            System.out.println("database is empty");
        }
    }

    public void getAllDocuments(String databaseName,String collectionName){
        String collectionFolderPath = System.getProperty("user.dir") + "/databases/" + databaseName + "/" + collectionName;
        try {
            for (File file : new File(collectionFolderPath).listFiles()) {
                String documentName = file.getName();
                DatabaseManager.getInstance().getDatabases().get(databaseName).getCollections().get(collectionName).getDocuments().add(documentName);
            }
        } catch (Exception e){
            System.out.println("collection is empty");
        }
    }

    public void getIndex(String databaseName, String collectionName) throws FileNotFoundException {
        String indexFilePath = System.getProperty("user.dir") + "/databases/" + databaseName + "/" + collectionName + "/index.json";
        File index = new File(indexFilePath);
        String jsonString = new Scanner(index).useDelimiter("\\Z").next();

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Map<String, DocumentIndex>>>() {}.getType();
        Map<String, Map<String, DocumentIndex>> map = gson.fromJson(jsonString, type);
        for (String key : map.keySet())
        {
            DatabaseManager.getInstance().getDatabases().get(databaseName).getCollections().get(collectionName).getIndexes().put(key, new HashMap<>());
            Map<String, DocumentIndex> map1 = map.get(key);
            for (String key1 : map1.keySet()) {
                DatabaseManager.getInstance().getDatabases().get(databaseName).getCollections().get(collectionName).getIndexes().get(key).put(key1, map1.get(key1));
            }
        }
    }

    private void getCollectionsAffinity(String databaseName) throws FileNotFoundException {
        String affinityFilePath = System.getProperty("user.dir") + "/databases/" + databaseName + "/affinity.json";
        File affinity = new File(affinityFilePath);
        String jsonString = new Scanner(affinity).useDelimiter("\\Z").next();

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String,Integer>>() {}.getType();
        Map<String, Integer> map = gson.fromJson(jsonString, type);
        DatabaseManager.getInstance().getDatabases().get(databaseName).setCollectionsAffinity(map);
    }
}
