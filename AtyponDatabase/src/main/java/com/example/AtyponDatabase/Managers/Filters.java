package com.example.AtyponDatabase.Managers;

import com.example.AtyponDatabase.Database.DocumentIndex;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class Filters {
    public ResponseEntity<List<JsonNode>> filterByKey(String databaseName, String collectionName, String key) throws IOException {
        Map<String, DocumentIndex> map = DatabaseManager.getInstance().getDatabases().get(databaseName).getCollections().get(collectionName).getIndexes().get(key);
        List<String> documentIds = new ArrayList<>();

        try {
            for (String value : map.keySet()) {
                documentIds.addAll(map.get(value).getDocumentIds());
            }
        }catch (NullPointerException e)
        {}
        return getDocuments(databaseName, collectionName, documentIds);
    }

    public ResponseEntity<List<JsonNode>> filterByValue(String databaseName, String collectionName, String key, String value) throws IOException {
        List<String> documentIds = new ArrayList<>();
        try {
            documentIds = DatabaseManager.getInstance().getDatabases().get(databaseName).getCollections().get(collectionName).getIndexes().get(key).get(value).getDocumentIds();
        }catch (NullPointerException e){}
        return getDocuments(databaseName, collectionName, documentIds);
    }

    public ResponseEntity<JsonNode> filterById(String databaseName, String collectionName, String documentId) throws IOException {
        String documentFilePath = System.getProperty("user.dir") + "/databases/" + databaseName + "/" + collectionName + "/" + documentId + ".json";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(documentFilePath));

         return ResponseEntity.ok()
                 .contentType(MediaType.APPLICATION_JSON)
                 .body(rootNode);
    }

    public ResponseEntity<List<JsonNode>> getDocuments (String databaseName, String collectionName, List<String>documentIds) throws IOException {
        List<JsonNode> jsonDoc = new ArrayList<>();
        for (String documentId : documentIds)
        {
            String documentFilePath = System.getProperty("user.dir") + "/databases/" + databaseName + "/" + collectionName + "/" + documentId + ".json";
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(new File(documentFilePath));
            jsonDoc.add(rootNode);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonDoc);
    }
}
