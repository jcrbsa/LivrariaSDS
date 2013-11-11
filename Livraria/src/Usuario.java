import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Enumeration;

class Usuario {
	private String nome;
	private List<Livro> livrosRetirados;

	Usuario(String st) {
		nome = st;
		livrosRetirados = new ArrayList<Livro>(5);
	}

	public int getCotaMaxima() {
		return 2;
	}

	public int getPrazoMaximo() {
		return 4;
	}

	public boolean isADevolver() {
		return ((livrosRetirados.size() >= getCotaMaxima() || temPrazoVencido()) ? true
				: false);
	}

	public boolean isAptoARetirar() {
		return (!isADevolver());
	}

	public boolean temPrazoVencido() {

		
		
		for (Livro livro : livrosRetirados ) {
			if (livro.isEmAtraso())
				return (true);
		}
		return (false);
	}

	public boolean retiraLivro(Livro it) {
		if (isAptoARetirar())
			if (it.empresta(this, getPrazoMaximo())) {
				livrosRetirados.add(it);
				return (true);
			} else
				return (false);
		return false;
	}

	public boolean devolveLivro(Livro it) {
		if (it.retorna(this)) {
			livrosRetirados.remove(it);
			return (true);
		} else
			return (false);
	}

	public boolean isProfessor() {
		return (false);
	}

	public String getNome() {
		return nome;
	}

	public String toString() {
		return ("Usuario " + nome);
	}

	public void listaCarga() {
		System.out.println(toString() + " Limite: " + getCotaMaxima()
				+ " Carga atual: " + livrosRetirados.size());
		for (Livro livro : livrosRetirados )
			System.out.println(livro);
	}

}