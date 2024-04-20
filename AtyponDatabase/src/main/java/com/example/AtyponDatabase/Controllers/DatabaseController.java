package com.example.AtyponDatabase.Controllers;

import com.example.AtyponDatabase.Managers.ClusterManager;
import com.example.AtyponDatabase.Managers.LoadBalancingManager;
import com.example.AtyponDatabase.Services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/database")
public class DatabaseController {
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private LoadBalancingManager loadBalancingManager;

    String port = System.getenv("NodePort");
    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/create")
    public String createDatabase(@RequestParam String databaseName, @RequestParam(defaultValue = "true") Boolean flag) {
        databaseService.createDatabase(databaseName);
        if(flag == true) {
            createDatabaseOnClusterNodes(databaseName);
        }
        return "Database created successfully";
    }

    private void createDatabaseOnClusterNodes(String databaseName) {
        List<String> nodes = ClusterManager.getInstance().getNodes();
        for (String node : nodes) {
            if (!Objects.equals(node, port)) {
                String clusterDatabaseUrl = "http://host.docker.internal:" + node + "/database/create?databaseName=" + databaseName + "&flag=false";
                restTemplate.exchange(clusterDatabaseUrl, HttpMethod.GET, null, String.class);
            }
        }
    }

    @GetMapping("/delete")
    public String deleteDatabase(@RequestParam String databaseName, @RequestParam(defaultValue = "true") Boolean flag) {
        databaseService.deleteDatabase(databaseName);
        if(flag == true) {
            deleteDatabaseOnClusterNodes(databaseName);
        }
        return "Database deleted successfully";
    }

    private void deleteDatabaseOnClusterNodes(String databaseName) {
        List<String> nodes = ClusterManager.getInstance().getNodes();
        for (String node : nodes) {
            if (!Objects.equals(node, port)) {
                String clusterDatabaseUrl = "http://host.docker.internal:" + node + "/database/delete?databaseName=" + databaseName + "&flag=false";
                restTemplate.exchange(clusterDatabaseUrl, HttpMethod.GET, null, String.class);
            }
        }
    }

    @GetMapping("/getAllDatabases")
    public List<String> getAllDatabases(){
        return databaseService.getAllDatabases();
    }

}
