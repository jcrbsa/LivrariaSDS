package repository;

import java.util.ArrayList;
import java.util.Collection;

import bean.Aluno;

import interfaces.RepositorioDados;

public class RepositorioDadosCollection implements RepositorioDados {

	private Collection<Aluno> Aluno = new ArrayList<Aluno>();
	private Collection<Aluno> alunoSetorA = new ArrayList<Aluno>();

	private Collection<Aluno> alunoSetorB = new ArrayList<Aluno>();


	private static int idAluno = 0;
	private static int idAlunoSetorA = 0;
	private static int idAlunoSetorB = 0;

	@Override
	public void inserir(Aluno aluno) {

		aluno.setCodigo(idAluno++);
		Aluno.add(aluno);

	}
	
	public void inserirSetorA(Aluno aluno) {

		aluno.setCodigo(idAlunoSetorA++);
		alunoSetorA.add(aluno);

	}
	public void inserirSetorB(Aluno aluno) {

		aluno.setCodigo(idAlunoSetorB++);
		alunoSetorB.add(aluno);

	}
	

	@Override
	public Aluno consultar(String matricula) {

		for (Aluno aluno : Aluno) {
		
			if (aluno.getMatricula().equals(matricula)) {
				return aluno;

			}
		}

		return null;

	}
	
	public Aluno consultarAlunoSetorA(String matricula) {

		for (Aluno aluno : alunoSetorA) {
		
			if (aluno.getMatricula().equals(matricula)) {
				return aluno;

			}
		}

		return null;

	}
	
	public Aluno consultarSetorB(String matricula) {

		for (Aluno aluno : alunoSetorB) {
		
			if (aluno.getMatricula().equals(matricula)) {
				return aluno;

			}
		}

		return null;

	}
	
	
	

	@Override
	public Collection<Aluno> listarTodos() {
		return Aluno;
	}

}
