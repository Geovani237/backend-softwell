package com.example.softwell.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor // Adicione isso para um construtor sem argumentos
@AllArgsConstructor // Adicione isso para um construtor com todos os argumentos
public class HumorDataDTO {
    private String questionText;
    private List<MoodOptionDTO> moodOptions;
    private List<String> emojis;
}