package com.pml.escola.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pml.escola.dto.TurmaDTO;
import com.pml.escola.model.Aluno;
import com.pml.escola.model.Turma;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;

@RestController
@RequestMapping("/turmas")
public class TurmaController {
    
    @PersistenceUnit
    private EntityManagerFactory emf;

    @PostMapping("/criar/{nome}/{duracao}")
    public Turma criarTurma(@PathVariable String nome, @PathVariable int duracao){
        Turma turma = new Turma(nome, duracao);
        EntityManager  manager = emf.createEntityManager();

        manager.getTransaction().begin();
        manager.persist(turma);
        manager.getTransaction().commit();

        return turma;
    }

    @GetMapping("/{idTurma}")
    public TurmaDTO relatorioTurma(@PathVariable int idTurma){
        EntityManager manager = emf.createEntityManager();
        Turma turma = manager.find(Turma.class, idTurma);
        if (turma == null) {
            return null;
        }
        return turma.dto();
    }

    @PutMapping("/matricular/{idTurma}/{idAluno}")
    public void matricularAluno(@PathVariable int idTurma, @PathVariable int idAluno){
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        Turma turma = entityManager.find(Turma.class, idTurma);
        Aluno aluno = entityManager.find(Aluno.class, idAluno);
        if (turma == null || aluno == null) {
            entityManager.getTransaction().rollback();
            entityManager.close();
            return;
        }
        turma.matricular(aluno);
        entityManager.merge(turma);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
