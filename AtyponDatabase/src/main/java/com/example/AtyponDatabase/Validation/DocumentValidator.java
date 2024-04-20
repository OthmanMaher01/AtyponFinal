package com.example.AtyponDatabase.Validation;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

@Component
public class DocumentValidator {
    public boolean isValidDocument(String collectionFolderPath, Map<String, String> docMap) throws FileNotFoundException {
        Gson gson = new Gson();

        File file = new File(collectionFolderPath,"schema.json");
        Scanner scanner = new Scanner(file);
        String json = scanner.useDelimiter("\\Z").next();
        Map<String, String> schemaMap = gson.fromJson(json, Map.class);
        scanner.close();
        if (docMap.size() > schemaMap.size()) {
            return false;
        }
        for (String key : docMap.keySet()) {
            if (!schemaMap.containsKey(key)) {
                return false;
            }
        }
        return true;
    }
}
