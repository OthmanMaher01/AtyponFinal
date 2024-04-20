package com.example.AtyponDatabase.Controllers;

import com.example.AtyponDatabase.Managers.ClusterManager;
import com.example.AtyponDatabase.Services.CollectionService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/collection")
public class CollectionController {
    @Autowired
    private CollectionService collectionService;
    private RestTemplate restTemplate = new RestTemplate();

    String port = System.getenv("NodePort");

    @PostMapping("/create")
    public String createCollection(@RequestParam String databaseName, @RequestParam String collectionName, @RequestBody String schema, @RequestParam(defaultValue = "true") Boolean flag) throws IOException {
        if(flag ==  true){
            createCollectionOnClusterNodes(databaseName, collectionName, schema);
        }
        return collectionService.createCollection(databaseName,collectionName, schema);
    }

    private void createCollectionOnClusterNodes(String databaseName, String collectionName, String schema) throws IOException {
        List<String> nodes = ClusterManager.getInstance().getNodes();
        for (String node : nodes) {
            if(!Objects.equals(node, port)) {
                String clusterDatabaseUrl = "http://host.docker.internal:" + node + "/collection/create?databaseName=" + databaseName + "&collectionName=" + collectionName + "&flag=false";
                restTemplate.postForObject(clusterDatabaseUrl, schema, String.class);
            }
        }
    }

    @GetMapping("/delete")
    public String deleteCollection(@RequestParam String databaseName, @RequestParam String collectionName, @RequestParam(defaultValue = "true") Boolean firstReq) throws IOException {
        String affinity = ClusterManager.getInstance().getNodes().get(collectionService.getCollectionAffinity(databaseName, collectionName));
        if(Objects.equals(port, affinity)) {

            collectionService.deleteCollection(databaseName,collectionName);
            deleteCollectionOnClusterNodes(databaseName, collectionName);
        }else if(firstReq)
        {
            String node = affinity;
            String clusterDatabaseUrl = "http://host.docker.internal:" + node + "/collection/delete?databaseName=" + databaseName + "&collectionName=" + collectionName + "&firstReq=false";
            restTemplate.exchange(clusterDatabaseUrl, HttpMethod.GET, null, String.class);

        }
        else {
            collectionService.deleteCollection(databaseName,collectionName);
        }
        return "Collection deleted successfully";
    }

    private void deleteCollectionOnClusterNodes(String databaseName, String collectionName) {
        List<String> nodes = ClusterManager.getInstance().getNodes();
        for (String node : nodes) {
            if (!Objects.equals(node, port)) {
                String clusterDatabaseUrl = "http://host.docker.internal:" + node + "/collection/delete?databaseName=" + databaseName + "&collectionName=" + collectionName + "&firstReq=false";
                restTemplate.exchange(clusterDatabaseUrl, HttpMethod.GET, null, String.class);
            }
        }
    }

    @GetMapping("/filterByKey")
    public ResponseEntity<List<JsonNode>> filterByKey(@RequestParam String databaseName, @RequestParam String collectionName, @RequestParam String key) throws IOException {
        return  collectionService.filterByKey(databaseName, collectionName, key);
    }
    @GetMapping("/filterByValue")
    public ResponseEntity<List<JsonNode>> filterByValue(@RequestParam String databaseName, @RequestParam String collectionName, @RequestParam String key ,@RequestParam String value) throws IOException {
        return  collectionService.filterByValue(databaseName, collectionName, key, value);
    }
    @GetMapping("/filterById")
    public ResponseEntity<JsonNode> filterById(@RequestParam String databaseName, @RequestParam String collectionName, @RequestParam String id) throws IOException {
        return  collectionService.filterById(databaseName, collectionName, id);
    }

    @GetMapping("/getAllCollections")
    public List<String> getAllCollections(@RequestParam String databaseName){
        return collectionService.getAllCollections(databaseName);
    }

    @GetMapping("/getSchema")
    public ResponseEntity<JsonNode> getSchema(@RequestParam String databaseName, @RequestParam String collectionName) throws IOException {
        System.out.println(45498745);
        return collectionService.getSchemaFile(databaseName, collectionName);
    }
}
