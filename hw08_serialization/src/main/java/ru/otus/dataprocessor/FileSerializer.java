package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {
    private final ObjectMapper objectMapper;
    private final String fileName;
    public FileSerializer(String fileName) {
        this.fileName = fileName;
        this.objectMapper = JsonMapper.builder().build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void serialize(Map<String, Double> data) {
        // формирует результирующий json и сохраняет его в файл
        try (FileWriter writer = new FileWriter(fileName)) {
            String jsonString = objectMapper.writeValueAsString(data);
            writer.write(jsonString);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
