package com.pml.escola;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.pml.escola.model.Aluno;
import com.pml.escola.model.Turma;
public class TurmaTest {
Turma turma;
Aluno aprovado;
Aluno reprovado;
@BeforeEach
public void setup(){
turma = new Turma("LPM", 10);
aprovado = new Aluno("Aprovado");
reprovado = new Aluno("Reprovado");
for (int i = 0; i < 3; i++) {
aprovado.lancarNota(20);
reprovado.lancarNota(20);
reprovado.registrarFalta();
}
}
@Test
public void matricularAlunos(){
turma.matricular(aprovado);
int quantos = turma.matricular(reprovado);
assertEquals(2, quantos);
}
@Test
public void naoMatriculaRepetido(){
turma.matricular(aprovado);
int quantos = turma.matricular(aprovado);
assertEquals(1, quantos);
}
@Test
public void deveLancarNotaParaAlunoExistente(){
    turma.matricular(aprovado);

    boolean resultado = turma.lancarNota(aprovado.getRegistro(), 20);

    assertTrue(resultado);
}
@Test
public void naoDeveLancarFaltaParaAlunoInexistente(){
    boolean resultado = turma.registrarFalta(100);

    assertFalse(resultado);
}
}
