package com.example.AtyponDatabase.Managers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Data
@Component
public class LoadBalancingManager {
    @Value("${server.port}")
    private String port;
    private RestTemplate restTemplate = new RestTemplate();

    private static final int MAX_REQUESTS_PER_TIME_UNIT = 100;
    private static final long TIME_UNIT_IN_MILLIS = 1000;

    private int requestCount;
    private long lastUpdateTime;

    public LoadBalancingManager() {
        this.requestCount = 0;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public boolean shouldOffloadRequest() {
        long currentTime = System.currentTimeMillis();
        System.out.println(currentTime);
        if (currentTime - lastUpdateTime >= TIME_UNIT_IN_MILLIS) {
            // Reset request count if a new time unit has started
            requestCount = 1;
            System.out.println(requestCount);
            lastUpdateTime = currentTime;
            return false;
        } else {
            // Increment request count within the current time unit
            requestCount++;
            System.out.println(requestCount);
            return requestCount > MAX_REQUESTS_PER_TIME_UNIT;
        }
    }

    public void offloadExcessRequests(String databaseName) {

        String targetNode = getNextNode();

        String clusterDatabaseUrl = "http://host.docker.internal:" + targetNode + "/database/create?databaseName=" + databaseName;
        restTemplate.exchange(clusterDatabaseUrl, HttpMethod.GET, null, String.class);
        // TODO: Send excess requests to the selected targetNode
    }

    private synchronized String getNextNode() {
        List<String> nodes = ClusterManager.getInstance().getNodes();
        int currentNodeIndex = nodes.indexOf(port);
        int nextNodeIndex = (currentNodeIndex + 1) % nodes.size();
        return nodes.get(nextNodeIndex);
    }
}