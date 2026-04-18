package com.pml.escola.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pml.escola.model.Aluno;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;

@RestController
@RequestMapping("/alunos")
public class AlunoController {
    
    @PersistenceUnit
    private EntityManagerFactory emf;

    @PostMapping("/criar")
    public Aluno cadastrarAluno(@RequestBody String nome){
        Aluno novo = new Aluno(nome);
        EntityManager manager = emf.createEntityManager();
        manager.getTransaction().begin();
        manager.persist(novo);
        manager.getTransaction().commit();
        return novo;
    }

    @GetMapping("/{registro}")
    public Aluno localizarAluno(@PathVariable int registro){
        EntityManager manager = emf.createEntityManager();
        Aluno aluno = manager.find(Aluno.class, registro);
        return aluno;
    }

    @PutMapping("/lancar/{registro}/{nota}")
    public Aluno lancarNota(@PathVariable int registro, @PathVariable double nota){
        EntityManager manager = emf.createEntityManager();
        Aluno aluno = manager.find(Aluno.class, registro);
        if(aluno!=null){
            aluno.lancarNota(nota);
            manager.getTransaction().begin();
            manager.persist(aluno);
            manager.getTransaction().commit();
        }
        return aluno;
    }

}
