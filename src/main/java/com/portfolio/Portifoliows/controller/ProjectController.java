package com.portfolio.Portifoliows.controller;

import com.portfolio.Portifoliows.model.Project;
import com.portfolio.Portifoliows.repository.ProjectRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@Validated @RequestBody Project project) {
        try {
            // Adicionar logs de depuração para verificar os dados recebidos na requisição
            System.out.println("Dados recebidos na requisição: " + project);

            // Salvar o projeto no banco de dados apenas se todos os campos forem diferentes de null
            if (project.getNome() != null && project.getDescricao() != null && project.getTipo() != null && project.getLink() != null) {
                Project createdProject = projectRepository.save(project);
                // Retornar uma resposta de sucesso com o projeto recém-criado e o status 201 (Created)
                return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
            } else {
                // Se algum campo for null, retornar uma resposta de erro com o status 400 (Bad Request)
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            // Se ocorrer algum erro ao salvar o projeto, retornar uma resposta de erro com o status 500 (Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project projectDetails) {
        Optional<Project> optionalProject = projectRepository.findById(id);

        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.setNome(projectDetails.getNome());
            project.setDescricao(projectDetails.getDescricao());
            project.setTipo(projectDetails.getTipo());
            project.setLink(projectDetails.getLink());

            Project updatedProject = projectRepository.save(project);
            return ResponseEntity.ok(updatedProject);
        } else {
            return ResponseEntity.notFound().build();
        }
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        try {
            Project project = projectRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + id));

            // Excluir o projeto
            projectRepository.delete(project);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            // Se o projeto não for encontrado, retornar uma resposta com o status 404 (Not Found)
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Se ocorrer algum outro erro, retornar uma resposta de erro com o status 500 (Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
