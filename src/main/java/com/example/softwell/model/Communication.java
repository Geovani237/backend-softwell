package com.example.softwell.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Communication {
    // Campos de Slider (Float de 1.0 a 5.0)
    private float taskClarity; // Orientações claras e objetivas
    private float openCommunication; // Comunicar abertamente com a liderança
    private float infoFlow; // Informações importantes circulam eficientemente
    private float goalClarity; // Clareza sobre metas e resultados
}