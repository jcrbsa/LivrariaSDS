package fachada;


import bean.Aluno;
import interfaces.RepositorioDados;
import java.util.Collection;
import repository.RepositorioDadosCollection;


public class FachadaDados {
	
	private static FachadaDados fachada;
	private RepositorioDados repositorio;
    static {
        fachada = new FachadaDados();
    }
	private FachadaDados() {
		repositorio = new RepositorioDadosCollection();
	}
    public static FachadaDados getInstance() {
        return fachada;
    }
	public void inserir(Aluno aluno) {
		
	  repositorio.inserir(aluno);
	}
	
	public Aluno consultar(Aluno aluno) {

		return repositorio.consultar(aluno.getMatricula());
	}
        
        
        public Collection<Aluno> listarTodos() {
		return repositorio.listarTodos();
	}
   



}
