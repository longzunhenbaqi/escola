package com.pml.escola.model;

import java.util.ArrayList;
import java.util.List;

import com.pml.escola.dto.AlunoDTO;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Aluno {

	private static final int AVALIACOES = 4;
	private static final double NOTA_APROV = 60;
	private static final double FREQ_APROV = 0.75;
	private static final double MAX_AVALIACAO = 25;
	
	private String nome;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int registro;

	@ElementCollection
	private List<Double> notas;
	private int faltas;

	public Aluno(){
		
	}
	
	/**
	 * Construtor para o aluno. Em caso de nome vazio, será criado como 
	 * "Aluno XXXX", sendo XXXX seu número de registro atribuído automaticamente.
	 * @param nome Nome do aluno. Não deve ser vazio.
	 */
	public Aluno(String nome) {
		
		if(nome.length()==0){
			nome = "Aluno "+registro;
		}
		this.nome = nome;
		faltas = 0;
		notas = new ArrayList<>(AVALIACOES);
	}

	/**
	 * Retorna o número de registro do aluno (id único);
	 * @return Inteiro com o registro do aluno
	 */
	public int getRegistro() {
		return registro;
	}

	/**
	 * Lança uma nota para o aluno. Retorna a própria nota, caso a operação
	 * tenha sucesso, ou -1 em caso de problemas (avaliações já realizadas ou
	 * valor incorreto da nota)
	 * @param nota Nota para o aluno. Deve estar entre 0 e 25.
	 * @return A nota lançada, ou -1 em caso de problemas.
	 */
	public double lancarNota(double nota) {
		double resposta = -1;
		if(nota <= MAX_AVALIACAO && nota >= 0){
			if(notas.size() < AVALIACOES){
				notas.add(nota);
				resposta = nota;
			}
		}
		return resposta;
	}

	/**
	 * Registra uma nota para o aluno. Retorna a quantidade de faltas atual
	 * do aluno. 
	 * @return Inteiro com a quantidade atual de faltas.
	 */
	public int registrarFalta() {
		faltas++;
		return faltas;
	}

	/**
	 * Retorna a nota final do aluno (que pode ser 0 se ele ainda não recebeu
	 * nenhuma nota)
	 * @return Double com a nota final do aluno.
	 */
	public double notaFinal() {
		double nota = 0;
		if(notas != null)
			for (Double notaAluno : notas) {
				nota += notaAluno;
			}
		return nota;
	}

	/**
	 * Verifica se um aluno não atingiu a porcentagem mínima para ser aprovado
	 * de acordo com o total de aulas recebido. Atenção para o retorno: TRUE se o 
	 * aluno foi *reprovado* por faltas.
	 * @param totalAulas Total de aulas de referência da turma. Deve ser maior que 0.
	 * @return TRUE caso o aluno foi *reprovado* por faltas. FALSE caso contrário.
	 */
	public boolean reprovadoPorFaltas(int totalAulas) {
		double porcentagem = 1 - (double)faltas/totalAulas;
		return (porcentagem <= FREQ_APROV);
	}

	/**
	 * Verifica se o aluno está aprovado, ou seja, se superou a nota mínima e a frequência
	 * mínima para a quantidade de aulas especificada pela turma. Retorna TRUE para aprovação
	 * e FALSE para reprovação.
	 * @param totalAulas Total de aulas de referência da turma. Deve ser maior que 0.
	 * @return TRUE para aluno aprovado e FALSE para aluno reprovado.
	 */
	public boolean estahAprovado(int totalAulas) {
		return notaFinal() >= NOTA_APROV
		       && !reprovadoPorFaltas(totalAulas);
	}

	/**
	 * O histórico do aluno é composto por seu nome, registro, total de faltas; seguido do detalhamento
	 * de cada nota recebida e da nota final. 
	 * @return String com os dados descritos acima. 
	 */
	public String historico() {
		StringBuilder relatorio = new StringBuilder();
		relatorio.append(String.format("Aluno %s (%d) - Total de faltas: %d\n",nome, registro, faltas));
		relatorio.append("Notas obtidas:\n");
		for (int i = 0; i < notas.size(); i++) {
			relatorio.append(String.format("\tNota %02d: %.2f\n", (i+1), notas.get(i)));
		}
		relatorio.append(String.format("NOTA FINAL: %2.f",notaFinal()));
		return relatorio.toString();
	}

	public String toString(){
		return String.format("Aluno %s (%d) com nota %.2f",nome, registro, notaFinal());
	}

	public AlunoDTO dto() {
   		return new AlunoDTO(registro, nome, notaFinal());
	}

}
