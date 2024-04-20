package com.example.AtyponDatabase.Validation;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SchemaValidator {
    public boolean isValidSchema(Map<String, String> schemaMap) {
        for (String key : schemaMap.keySet()) {
            String value = schemaMap.get(key);
            try {

                DataTypes Type = DataTypes.valueOf(value);
                for (DataTypes dataTypes: DataTypes.values())
                {
                    if(Type == dataTypes)
                        break;
                }
            } catch (IllegalArgumentException e)
            {
                return false;
            }
        }
        return true;
    }
}
