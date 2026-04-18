package com.pml.escola;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.pml.escola.model.Aluno;
public class AlunoTest {
Aluno aluno;
@BeforeEach
public void setup(){
aluno = new Aluno("Aluno LPM");
aluno.lancarNota(20);
aluno.lancarNota(20);
aluno.lancarNota(10);
}
@Test
public void alunoAprovado(){
aluno.lancarNota(20);
boolean aprovado = aluno.estahAprovado(10);
assertTrue(aprovado);
}
@Test
public void alunoAindaNaoAprovado(){
aluno.lancarNota(20);
aluno.registrarFalta();
boolean aprovado = aluno.estahAprovado(3);
assertFalse(aprovado);
}
@Test
public void alunoReprovadoPorFaltas(){
    // já tem 3 notas no setup

    // registrar várias faltas
    for(int i = 0; i < 3; i++){
        aluno.registrarFalta();
    }

    boolean reprovado = aluno.reprovadoPorFaltas(10);

    assertTrue(reprovado);
}
@Test
public void naoDevePermitirQuintaNota(){
    // já tem 3 notas no setup

    aluno.lancarNota(10); // 4ª nota

    double resultado = aluno.lancarNota(10); // tentativa da 5ª

    assertEquals(-1,resultado);
}
}
