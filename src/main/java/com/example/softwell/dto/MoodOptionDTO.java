package com.example.softwell.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Adicione isso para um construtor sem argumentos
@AllArgsConstructor // Adicione isso para um construtor com todos os argumentos
public class MoodOptionDTO {
    private String id;
    private String text;
}