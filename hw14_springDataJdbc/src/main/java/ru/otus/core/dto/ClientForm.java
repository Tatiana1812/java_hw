package ru.otus.core.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClientForm {
    private String name;
    private String addressStreet;
    private List<String> phoneNumbers;
}