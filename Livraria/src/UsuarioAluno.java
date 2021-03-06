import java.util.Date;

class UsuarioAluno extends Usuario {
	private Date dataLimite;

	UsuarioAluno(String st, Date dt) {
		super(st);
		dataLimite = dt;
	}

	public void renovaCartao(Date dt) {
		dataLimite = dt;
	}

	public boolean isRegular() {
		Date hoje = new Date();
		return (dataLimite.after(hoje));
	}

	public boolean isARenovar() {
		return (!isRegular());
	}

	public int getCotaMaxima() {
		return (isRegular() ? 3 : super.getCotaMaxima());
	}

	public int getPrazoMaximo() {
		return (isRegular() ? 7 : super.getPrazoMaximo());
	}

	public String toString() {
		return ("Aluno " + getNome());
	}

}