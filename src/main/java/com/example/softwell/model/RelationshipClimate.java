package com.example.softwell.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RelationshipClimate {
    // Campos de Rating (1 a 5)
    private int bossRating;
    private int coworkerRating;
    private int coworkerRespect; // Sinto que sou tratado(a) com respeito
    private int teamRelationship; // Relacionar de forma saudável e colaborativa
    private int freedomSpeech; // Liberdade para expressar opiniões
    private int welcomedPart; // Me sinto acolhido(a) a parte do time
    private int cooperationSpirit; // Espírito de cooperação
}