package bean;
import java.io.*;
public class Aluno implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nome;

	private String matricula;
	private int qtLivros;
	private int codigo;
	private int setor;//Setor A =1 Setor B = 2;
	private int operacao; //0- finalizada; -1 -nao iniciada 1 - cadastrar; 2 - Consultar; 3 - Iniciar 
	private boolean respConfirmacao;// 0 - Não; 1 - Sim;
	private boolean inseriu;
	private int enviarSetor;//Setor A =1 Setor B = 2;
	private boolean encontrou;
	private boolean consultaSetor;
	private boolean edit;
	private int id;
	private int estado; //0 - nao definido;1 - get; 2 - set;
	private int consulta; //1 - inicial 2- retorno consulta no outro setor/3 editar registor consulta inicial/4 retorno p/ edicao para outro setor
	
	



	public int getConsulta() {
		return consulta;
	}

	public void setConsulta(int consulta) {
		this.consulta = consulta;
	}


	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public Aluno(){
		qtLivros = 3;
		setor = 1;
		respConfirmacao = false;
		inseriu = false;
		enviarSetor = 1 ;
		encontrou = false;
		operacao = -1;
		consultaSetor = false;
		edit = false;
		estado= 0;
		consulta = 3;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}
	
	public boolean isConsultaSetor() {
		return consultaSetor;
	}

	public void setConsultaSetor(boolean consultaSetor) {
		this.consultaSetor = consultaSetor;
	}
	
	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}
	public boolean isRespConfirmacao() {
		return respConfirmacao;
	}
	public void setRespConfirmacao(boolean respConfirmacao) {
		this.respConfirmacao = respConfirmacao;
	}


	
	public boolean isEncontrou() {
		return encontrou;
	}
	public void setEncontrou(boolean encontrou) {
		this.encontrou = encontrou;
	}
	public int getEnviarSetor() {
		return enviarSetor;
	}
	public void setEnviarSetor(int enviarSetor) {
		this.enviarSetor = enviarSetor;
	}
	

	
	public boolean isInseriu() {
		return inseriu;
	}
	public void setInseriu(boolean inseriu) {
		this.inseriu = inseriu;
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
