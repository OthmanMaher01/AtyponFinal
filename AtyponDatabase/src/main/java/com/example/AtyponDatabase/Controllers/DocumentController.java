package com.example.AtyponDatabase.Controllers;

import com.example.AtyponDatabase.Managers.ClusterManager;
import com.example.AtyponDatabase.Services.CollectionService;
import com.example.AtyponDatabase.Services.DocumentService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping ("/document")
public class DocumentController {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private CollectionService collectionService;
    private RestTemplate restTemplate = new RestTemplate();

    String port = System.getenv("NodePort");

    @PostMapping("/create")
    public void createDocument(@RequestParam String databaseName, @RequestParam String collectionName, @RequestParam(defaultValue = "false") String fileName, @RequestBody String schema, @RequestParam(defaultValue = "true") Boolean firstReq) throws IOException {
        String affinity = ClusterManager.getInstance().getNodes().get(collectionService.getCollectionAffinity(databaseName, collectionName));

        if(Objects.equals(port, affinity)){
                String result = documentService.createDocument(databaseName, collectionName, fileName, schema);
                if (result != "The file is not valid. Please check the schema and try again.") {
                        fileName = result;
                        createDocumentOnClusterNodes(databaseName, collectionName, fileName, schema);
                    System.out.println("File created successfully");
                }else {
                    System.out.println(result);
                }
        }
        else if(firstReq) {
            String node = affinity;
            String clusterDatabaseUrl = "http://host.docker.internal:" + node + "/document/create?databaseName=" + databaseName + "&collectionName=" + collectionName + "&fileName=" + fileName + "&firstReq=false";
            restTemplate.postForObject(clusterDatabaseUrl, schema, String.class);
        }
        else
        {
            documentService.createDocument(databaseName, collectionName, fileName, schema);
        }
    }

    private void createDocumentOnClusterNodes(String databaseName, String collectionName, String fileName, String schema) {
        List<String> nodes = ClusterManager.getInstance().getNodes();
        for (String node : nodes) {
            if(!Objects.equals(node, port)) {
                String clusterDatabaseUrl = "http://host.docker.internal:" + node + "/document/create?databaseName=" + databaseName + "&collectionName=" + collectionName + "&fileName=" + fileName + "&firstReq=false";
                restTemplate.postForObject(clusterDatabaseUrl, schema, String.class);
            }
        }
    }

    @DeleteMapping("/delete")
    public void deleteDocument(@RequestParam String databaseName, @RequestParam String collectionName, @RequestParam String fileName, @RequestParam(defaultValue = "true") Boolean firstReq) {
        String affinity = ClusterManager.getInstance().getNodes().get(collectionService.getCollectionAffinity(databaseName, collectionName));
        if(Objects.equals(port, affinity)){
            documentService.deleteDocument(databaseName, collectionName, fileName);
            deleteDocumentOnClusterNodes(databaseName, collectionName, fileName);
            System.out.println("File deleted successfully");
        }
        else if(firstReq)
        {
            String node = affinity;
            String clusterDatabaseUrl = "http://host.docker.internal:" + node + "/document/delete?databaseName=" + databaseName + "&collectionName=" + collectionName + "&fileName=" + fileName + "&firstReq=false";
            restTemplate.exchange(clusterDatabaseUrl, HttpMethod.DELETE, null, String.class);

        }
        else
        {
            documentService.deleteDocument(databaseName, collectionName, fileName);
        }
    }

    private void deleteDocumentOnClusterNodes(String databaseName, String collectionName, String fileName) {
        List<String> nodes = ClusterManager.getInstance().getNodes();
        for (String node : nodes) {
            if (!Objects.equals(node, port)) {
                String clusterDatabaseUrl = "http://host.docker.internal:" + node + "/document/delete?databaseName=" + databaseName + "&collectionName=" + collectionName + "&fileName=" + fileName + "&firstReq=false";
                restTemplate.exchange(clusterDatabaseUrl, HttpMethod.DELETE, null, String.class);
            }
        }
    }

    @PutMapping("/update")
    public void updateDocument(@RequestParam String databaseName, @RequestParam String collectionName, @RequestParam String fileName, @RequestBody String document, @RequestParam(defaultValue = "true") Boolean firstReq) throws IOException {
        String affinity = ClusterManager.getInstance().getNodes().get(collectionService.getCollectionAffinity(databaseName, collectionName));
        if (Objects.equals(port, affinity)) {
            String result = documentService.updateDocument(databaseName, collectionName, fileName, document);
            updateDocumentOnClusterNodes(databaseName, collectionName, fileName, document);
            System.out.println(result);
        }else if(firstReq)
        {
            String node = affinity;
            String clusterDatabaseUrl = "http://host.docker.internal:" + node + "/document/update?databaseName=" + databaseName + "&collectionName=" + collectionName + "&fileName=" + fileName + "&firstReq=false";
            restTemplate.put(clusterDatabaseUrl, document, String.class);
        }
        else
        {
            documentService.updateDocument(databaseName, collectionName, fileName, document);
        }
    }
    private void updateDocumentOnClusterNodes(String databaseName, String collectionName, String fileName, String document){
        List<String> nodes = ClusterManager.getInstance().getNodes();
        for (String node : nodes) {
            if (!Objects.equals(node, port)) {
                String clusterDatabaseUrl = "http://host.docker.internal:" + node + "/document/update?databaseName=" + databaseName + "&collectionName=" + collectionName + "&fileName=" + fileName + "&firstReq=false";
                restTemplate.put(clusterDatabaseUrl, document, String.class);
            }
        }
    }

    @GetMapping("/getAllDocuments")
    public List<JsonNode> getAllDocuments(@RequestParam String databaseName, @RequestParam String collectionName) throws IOException {
        return documentService.getAllDocuments(databaseName, collectionName);
    }
}
