package com.pml.escola.model;

import java.util.ArrayList;
import java.util.List;

import com.pml.escola.dto.TurmaDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Turma {

	private static final int MAX_ALUNOS = 25;
	private static final int DURACAO_MIN = 8;
	private static final int DURACAO_MAX = 18;
	private String curso;
	private int duracao;

	@OneToMany
	private List<Aluno> matriculas;
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idTurma;


	public Turma(){

	}
	/**
	 * Construtor: recebe nome do curso e duração da turma (entre 8 e 18 aulas).
	 * Nomes vazios geram turmas com nome "Curso de XX semanas".
	 * @param curso Nome do curso. Não deveria ser vazio.
	 * @param duracao Duração (inteiro entre 8 e 18 aulas, inclusive)
	 */
	public Turma(String curso, int duracao) {
		if(duracao < DURACAO_MIN || duracao > DURACAO_MAX){
			duracao = DURACAO_MIN;
		}
		this.duracao = duracao;
		if(curso.length() == 0){
			curso = "Curso com " + this.duracao +" aulas.";
		}
		this.curso = curso;
		matriculas = new ArrayList<>(MAX_ALUNOS);
	}

	/**
	 * Método privado para localizar um aluno (e evitar repetição de código em diversos
	 * métodos públicos). Retorna o aluno encontrado ou "null" se não houver o aluno.
	 * A busca é feita pelo registro do aluno
	 * @param registro Número de registro do aluno procurado.
	 * @return Objeto aluno localizado ou "null" se ele não existir.
	 */
	private Aluno localizarAluno(int registro){
		int pos = 0;
		Aluno resposta = null;
		while(resposta == null && pos < matriculas.size()){
			Aluno candidato = matriculas.get(pos);
			if(candidato.getRegistro() == registro)
				resposta = candidato;
			pos++;
		}
		return resposta;
	}

	/**
	 * Matricula um aluno e retorna a quantidade de alunos matriculados na turma. 
	 * Em caso de não poder matricular (turma cheia ou aluno já matriculado), a 
	 * operação será ignorada
	 * @param aluno Aluno a ser matriculado.
	 * @return Quantidade de alunos da turma na finalização do método.
	 */
	public int matricular(Aluno aluno) {
		if(matriculas.size() < MAX_ALUNOS){
			Aluno jaExiste = localizarAluno(aluno.getRegistro());
			if(jaExiste == null)
				matriculas.add(aluno);
		}
		return matriculas.size();
	}

	/**
	 * Lança uma nota para um aluno. Retorna TRUE caso seja realizado com sucesso. 
	 * Em qualquer problema (aluno inexistente ou impossibilidade de lançar a nota),
	 * retorna FALSE
	 * @param aluno Número de registro do aluno para receber a nota.
	 * @param nota Nota a ser lançada para aquele aluno.
	 * @return TRUE se lançada corretamente, FALSE caso contrário.
	 */
	public boolean lancarNota(int aluno, double nota) {
		boolean resposta = false;
		Aluno jaExiste = localizarAluno(aluno);
		if(jaExiste!=null){
			double res = jaExiste.lancarNota(nota);
			resposta = (res == nota);
		}
		return resposta;
	}

	/**
	 * Registra uma falta para um aluno. Retorna TRUE caso seja realizado com sucesso. 
	 * Em qualquer problema (aluno inexistente ou impossibilidade de lançar a falta),
	 * retorna FALSE.
	 * @param aluno Número de registro do aluno para receber a falta.
	 * @return TRUE se lançada corretamente, FALSE caso contrário.
	 */
	public boolean registrarFalta(int aluno) {
		Aluno alunoTurma = localizarAluno(aluno);
		boolean resposta = false;
		if(alunoTurma != null){
			alunoTurma.registrarFalta();
			resposta = true;
		}
		return resposta;
	}

	/**
	 * Verifica se determinado aluno da turma já foi aprovado. Retorna TRUE neste caso.
	 * Em caso do aluno não existir ou de não tiver sido aprovado, retorna FALSE.
	 * @param aluno Número de registro do aluno para verificação.
	 * @return TRUE em caso de aluno aprovado; FALSE caso ele não exista ou esteja aprovado.
	 */
	public boolean alunoAprovado(int aluno) {
		Aluno alunoTurma = localizarAluno(aluno);
		return (alunoTurma != null && alunoTurma.estahAprovado(duracao));
	}

	/**
	 * Verifica se determinado aluno da turma foi reprovado por frequência. Retorna TRUE neste caso.
	 * Em caso do aluno não existir ou de não tiver sido reprovado por frequência, retorna FALSE.
	 * @param aluno Número de registro do aluno para verificação.
	 * @return TRUE em caso de aluno reprovado por frequência; FALSE caso ele não exista ou 
	 * não tenha sido reprovado por frequência.
	 */
	public boolean alunoReprovadoPorFalta(int aluno) {
		Aluno alunoTurma = localizarAluno(aluno);
		return (alunoTurma !=null && alunoTurma.reprovadoPorFaltas(duracao));
	}

	/**
	 * Deve retornar um relatório da turma, contendo identificação da turma,
	 * nome e nota final de cada aluno, além da nota média da turma
	 * e da porcentagem de aprovados. 
	 * @return String com os dados descritos. 
	 */
	public String relatorio() {
		int quantAlunos =  matriculas.size();
		StringBuilder relat = new StringBuilder(curso + " com "+quantAlunos+" alunos\n");
		int aprovados = 0;
		double notaTotal = 0d;
		for (Aluno aluno : matriculas) {
			relat.append(aluno.toString()+"\n");
			aprovados += aluno.estahAprovado(duracao) ? 1 : 0;
			notaTotal += aluno.notaFinal();
		}
		relat.append(String.format("MÉDIA DA TURMA: %.2f\n", (notaTotal/quantAlunos)));
		relat.append(String.format("PORCENTAGEM DE APROVADOS: %.2f%%\n",(aprovados*100.0/quantAlunos)));
		return relat.toString();
	}

	public TurmaDTO dto() {
    	return new TurmaDTO(idTurma, relatorio());
	}

}
