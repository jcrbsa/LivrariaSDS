package bean;

public class Aluno {
	
	private String matricula;
	private int qtLivros;
	private int codigo;
	private int setor;//Setor A =1 Setor B = 2;
	private int operacao; //1 - cadastrar; 2 - Consultar; 
	private int respConfirmacao;// 0 - Não; 1 - Sim;
	
	public int getRespConfirmacao() {
		return respConfirmacao;
	}
	public void setRespConfirmacao(int respConfirmacao) {
		this.respConfirmacao = respConfirmacao;
	}
	public int getOperacao() {
		return operacao;
	}
	public void setOperacao(int operacao) {
		this.operacao = operacao;
	}
	public int getSetor() {
		return setor;
	}
	public void setSetor(int setor) {
		this.setor = setor;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public int getQtLivros() {
		return qtLivros;
	}
	public void setQtLivros(int qtLivros) {
		this.qtLivros = qtLivros;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	

}
