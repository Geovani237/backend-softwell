package com.example.softwell.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LeadershipRelation {
    // Campos de Slider (Float de 1.0 a 5.0)
    private float leaderCaresWellbeing; // Liderança demonstra interesse pelo bem-estar
    private float leaderIsAvailable; // Liderança está disponível para ouvir
    private float comfortableReportingIssues; // Confortável para reportar problemas
    private float leaderRecognizesEfforts; // Liderança reconhece entregas e esforços
    private float trustAndTransparency; // Confiança e transparência

    // **Atenção:** Seu Android tinha 5 sliders, mas o construtor da resposta anterior
    // estava com 4. Mantenha os 5 que estão na tela do Compose:
    // taskClarity, openCommunication, infoFlow, goalClarity, leaderCaresWellbeing,
    // leaderIsAvailable, comfortableReportingIssues, leaderRecognizesEfforts, trustAndTransparency.
}