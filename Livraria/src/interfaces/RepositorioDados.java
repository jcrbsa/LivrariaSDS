package interfaces;

import bean.Aluno;
import java.util.Collection;

public interface RepositorioDados {

	public void inserir(Aluno dados);

	public Aluno consultar(String matricula);

	public Collection<Aluno> listarTodos();

}
