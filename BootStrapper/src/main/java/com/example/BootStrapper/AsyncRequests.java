package com.example.BootStrapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class AsyncRequests {
    private final RestTemplate restTemplate=new RestTemplate();

    @Async
    public void sync(String port) throws InterruptedException{


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<String> ports=new ArrayList<>();
        for(int i=8080; i<8082; i++) {
            ports.add(String.valueOf(i));
        }

        JsonNode jsonNode = new ObjectMapper().createObjectNode();
        ArrayNode arrayNode=((ObjectNode) jsonNode).putArray("ports");
        ports.forEach(arrayNode::add);

        for(String portNum: ports) {
            String databaseNodeUrl = "http://localhost:" + portNum + "/bootstrapper";
            System.out.println(databaseNodeUrl);
            HttpEntity<String> request = new HttpEntity<>(jsonNode.toString(), headers);
            Thread.sleep(1000*3);
            try {
                restTemplate.postForObject(databaseNodeUrl, request, String.class);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
