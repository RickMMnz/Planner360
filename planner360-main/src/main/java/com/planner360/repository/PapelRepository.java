package com.planner360.repository;

import com.planner360.model.Papel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PapelRepository extends JpaRepository<Papel, Long> {

    // Buscar papel pelo nome
    Papel findByNome(String nome);
}

