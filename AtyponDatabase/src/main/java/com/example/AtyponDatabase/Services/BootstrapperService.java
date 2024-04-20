package com.example.AtyponDatabase.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BootstrapperService {

    @Autowired
    private ServerProperties serverProperties;
    private final RestTemplate restTemplate=new RestTemplate();
    public void pingBootstrapperForInitData(){
        int bootstrapperPort = Integer.parseInt(System.getenv("BootstrapperPORT"));
        System.out.println(bootstrapperPort);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JsonNode jsonNode = new ObjectMapper().createObjectNode();
        ((ObjectNode) jsonNode).put("port", System.getenv("NodePort"));
        String pingBootstrapperUrl =String.format("http://host.docker.internal:%s/database", bootstrapperPort);
        HttpEntity<String> request = new HttpEntity<>(jsonNode.toString(), headers);
        restTemplate.postForObject(pingBootstrapperUrl, request, String.class);
    }
}
