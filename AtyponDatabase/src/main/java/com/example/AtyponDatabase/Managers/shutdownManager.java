package com.example.AtyponDatabase.Managers;

import com.example.AtyponDatabase.Database.Collection;
import com.example.AtyponDatabase.Database.Database;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Component
public class shutdownManager {
    @PreDestroy
    public void shutdown() throws IOException {
        createIndexesFiles();
        createAffinityFile();
    }

    private void createAffinityFile() throws IOException {
        HashMap<String, Database> databases = DatabaseManager.getInstance().getDatabases();
        for (String databaseName : databases.keySet()) {
            Map<String, Integer> affinity = databases.get(databaseName).getCollectionsAffinity();

            Gson gson = new Gson();
            Type gsonType = new TypeToken<HashMap>(){}.getType();
            String gsonString = gson.toJson(affinity, gsonType);

            String databasesFolderPath = System.getProperty("user.dir") + "/databases/" + databaseName;
            File file = new File(databasesFolderPath, "affinity.json");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(gsonString);
            writer.close();
        }
    }

    public void createIndexesFiles() throws IOException {
        HashMap<String, Database> databases = DatabaseManager.getInstance().getDatabases();
        for (String databaseName : databases.keySet())
        {
            Map<String, Collection> collections = databases.get(databaseName).getCollections();
            for (String collectionName : collections.keySet())
            {
                Gson gson = new Gson();
                Type gsonType = new TypeToken<HashMap>(){}.getType();
                String gsonString = gson.toJson(collections.get(collectionName).getIndexes(), gsonType);
                String collectionFolderPath = System.getProperty("user.dir") + "/databases/" + databaseName + "/" + collectionName;

                File index = new File(collectionFolderPath, "index.json");
                index.createNewFile();
                FileWriter writer = new FileWriter(index);
                writer.write(gsonString);
                writer.close();
            }
        }
    }
}
