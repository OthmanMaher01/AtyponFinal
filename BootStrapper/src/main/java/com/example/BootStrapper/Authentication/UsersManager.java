package com.example.BootStrapper.Authentication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Data
@Component
public class UsersManager {
    private static volatile UsersManager instance;
    private List<User> users = new ArrayList<>();
    private List<String> Nodes = List.of("8080", "8081"/*, "8082"*/);
    private int totalNodes = 2;
    private int index = 0;

    public List<User> getUsersFromStorage() throws FileNotFoundException {
        String usersFilePath =System.getProperty("user.dir") + "/storage/users.json";
        File file = new File(usersFilePath);
        Scanner scanner = new Scanner(file);
        String jsonArray = scanner.useDelimiter("\\Z").next();

        Gson gson = new Gson();
        Type listType = new TypeToken<List<User>>() {}.getType();
        List<User> usersArray = gson.fromJson(jsonArray, listType);
        scanner.close();

        for (User user : usersArray) {
            users.add(user);
        }
        return users;
    }

    public static UsersManager getInstance() {
        if (instance == null) {
            instance = new UsersManager();
        }
        return instance;
    }
    public String register(User user) {
        users.add(user);
        String userNode = getNodeForUser();
        return userNode;
    }

    private String getNodeForUser() {
        synchronized (this) {
            index = (index + 1) % totalNodes;
            return Nodes.get(index);
        }
    }

    public Boolean Authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    @PreDestroy
    public void writeUsers() throws IOException {
        String usersFilePath =System.getProperty("user.dir") + "/storage/users.json";
        Gson gson = new Gson();
        Type listType = new TypeToken<List<User>>() {}.getType();
        String jsonArray = gson.toJson(UsersManager.getInstance().getUsers(), listType);
        System.out.println(jsonArray);
        FileWriter fileWriter = new FileWriter(usersFilePath);
        fileWriter.write(jsonArray);
        fileWriter.close();
    }
}
