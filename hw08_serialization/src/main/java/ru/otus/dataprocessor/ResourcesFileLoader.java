package ru.otus.dataprocessor;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {
    private final ObjectMapper objectMapper;
    private final String fileName;
    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
        this.objectMapper = JsonMapper.builder().build();
        objectMapper.registerModule(new JavaTimeModule());
    }
    @Override
    public List<Measurement> load() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try(InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            return objectMapper.readValue(inputStream, new TypeReference<List<Measurement>>() {});
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
