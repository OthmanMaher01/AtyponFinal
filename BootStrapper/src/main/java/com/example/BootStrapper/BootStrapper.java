package com.example.BootStrapper;

import com.example.BootStrapper.Authentication.UsersManager;
import com.example.BootStrapper.Shell.Shell;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Component
public class BootStrapper {


    @PostConstruct
    public static void init() throws IOException, ExecutionException, InterruptedException, TimeoutException {
        UsersManager.getInstance().getUsersFromStorage();
        Shell.getInstance().runShellCommand("docker network create NoSqlNetwork");

        for(int i=8080; i<8082; i++) {
            String runContainer=String.format("docker run -d -p %s:9000 --network NoSqlNetwork -e BootstrapperPORT=8000 -e NodePort=%s --name Node%s atypondatabase", i, i, i-8079);
            Shell.getInstance().runShellCommand(runContainer);
        }
    }
}
