package com.pml.escola.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/relatorio/{idTurma}")
    public String relatorioTurma(@PathVariable int idTurma){
        EntityManager  manager = emf.createEntityManager();
        String resposta = "Turma não encontrada.";
        Turma turma = manager.find(Turma.class, idTurma);
        if(turma != null)
            resposta = turma.relatorio();

        return resposta;
    }
}
