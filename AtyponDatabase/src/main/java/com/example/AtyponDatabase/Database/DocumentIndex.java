package com.example.AtyponDatabase.Database;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DocumentIndex {
    List<String> DocumentIds = new ArrayList<>();
}