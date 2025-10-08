package com.example.softwell.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarningSigns {
    // Opções de dropdown: "Nunca", "Raramente", "Às vezes", "Frequentemente", "Sempre"
    private String warningSigns; // Sintomas como insônia, irritabilidade, etc.
    private String mentalHealthImpact; // Saúde mental prejudica produtividade.
}