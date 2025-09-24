package com.example.softwell.repository;

import com.example.softwell.model.Humor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HumorRepository extends MongoRepository<Humor, String> {
    // Ao herdar de MongoRepository, você ganha métodos prontos como:
    // - findAll(): Retorna todos os humores da coleção.
    // - findById(String id): Busca um humor pelo ID.
    // - save(Humor humor): Salva ou atualiza um humor.
    // Você não precisa escrever nenhum código aqui para as operações básicas.
}