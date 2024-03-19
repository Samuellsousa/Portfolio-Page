package com.portfolio.Portifoliows.repository;

import com.portfolio.Portifoliows.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByNome(String nome);
    List<Project> findByDescricao(String descricao);
    List<Project> findByTipo(String tipo);
    List<Project> findByLink(String link);
    // Aqui você pode adicionar métodos adicionais de consulta, se necessário
}
