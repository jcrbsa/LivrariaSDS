package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

import bean.Aluno;

public class Matriz {

	public static void main(String[] args) throws IOException {

		new Matriz(12345).executa();
	}

	private int porta;
	private List<ObjectOutputStream> clientes;
	private static List<Socket> setores =  new ArrayList<Socket>();
	private static List<Aluno> alunos =  new ArrayList<Aluno>();
	private static int  id = 0;
	

	public Matriz(int porta) {
		this.porta = porta;
		this.clientes = new ArrayList<ObjectOutputStream>(2);
		Aluno teste = new Aluno();
		teste.setMatricula("20112Y6-RC0027");
		teste.setNome("Bruno");
		teste.setSetor(2);
		alunos.add(0,teste);

	}

	public void executa() throws IOException {
		try {
	
			ServerSocket matriz = new ServerSocket(this.porta);
		while (true) {

			Socket setor = matriz.accept();
			setores.add(setor);
			System.out.println("Nova conexão com o cliente "
					+ setor.getInetAddress().getHostAddress());

			ObjectOutputStream out = new ObjectOutputStream(
					new DataOutputStream(setor.getOutputStream()));
			
		
			ObjectInputStream in = new ObjectInputStream(new DataInputStream(
					setor.getInputStream()));
		
			boolean flag = false;
			while(!flag){
				Aluno aluno = (Aluno) in.readObject();
				
				if(aluno.getOperacao() == 1){
				
					Aluno aluno_operacao = new Aluno();
					aluno_operacao.setMatricula(aluno.getMatricula());
					if(ExisteAlunoNoMatriz(aluno_operacao.getMatricula())== null){
						if(aluno.getSetor() == 1){
							aluno_operacao.setSetor(1);
						}if(aluno.getSetor() == 2){
							aluno_operacao.setSetor(2);
						}
					System.out.println("Cadastrando... " );
					aluno.setCodigo(id);
					alunos.add(id,aluno_operacao);
					id++;
					ListarTodosAlunosMatriz();
					aluno.setInseriu(true);
					System.out.println("Cadastro Realizado com Sucesso" );
			
					}else{
						aluno.setEncontrou(true);
						System.out.println("Aluno Já Existe " );
					}
					out.writeObject(aluno);						
				}if(aluno.getOperacao() == 2){
					Aluno aluno_consulta = new Aluno();
					if(ExisteAlunoNoMatriz(aluno.getMatricula()) != null){
						int pos;
						aluno_consulta = ExisteAlunoNoMatriz(aluno.getMatricula());
					    aluno_consulta.setEncontrou(true);
					    pos = aluno_consulta.getCodigo();
					    out.writeObject(aluno_consulta);
					    /*if(setores.size() == 2){
							Socket outroSetor = retornaOutroSocket(setor);
							ObjectOutputStream out_outroSetor = new ObjectOutputStream(
									new DataOutputStream(outroSetor.getOutputStream()));
							
						
							ObjectInputStream in_outroSetor = new ObjectInputStream(new DataInputStream(
									outroSetor.getInputStream()));
							out_outroSetor.writeObject(aluno_consulta);
							aluno_consulta = (Aluno)in_outroSetor.readObject();
							out.writeObject(aluno_consulta);
						}*/
					    aluno_consulta = (Aluno) in.readObject();
					  if(aluno_consulta.isEdit() == true){
						alunos.add(pos, aluno_consulta);
						ListarTodosAlunosMatriz();
					    }
					}else{
						aluno_consulta.setEncontrou(false);
					}
					out.writeObject(aluno_consulta);
				}
				
			
					if(aluno.getOperacao() == 0){
						flag = true;
					}
				}
			}

			
			
//			TrataCliente tc = new TrataCliente(in, this, setor);
//			new Thread(tc).start();
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
}
	
	
static Socket retornaOutroSocket(Socket socket){
		
		for (Socket setor : setores) {
			if(socket.getInetAddress() != setor.getInetAddress()){
				return setor;
			}
		}
		
		
		return null;
	}

	
	static Aluno ExisteAlunoNoMatriz(String matricula){
		

		for (Aluno aluno : alunos) {
			if(aluno.getMatricula().equals(matricula)){
				return aluno;
		
			}
		}
		
		
		return null;
	}
	
	static void ListarTodosAlunosMatriz(){
		
		int i =  0;
		for (Aluno aluno : alunos) {
				System.out.println("|" + i  + "||"+ aluno.getMatricula() +"|" );
				i++;
			}
		}

	
	
	public void distribuiMensagem(Aluno aluno, ObjectOutputStream saida)
			throws IOException {
		for (ObjectOutputStream out : this.clientes) {

			if (!out.equals(saida))
				out.writeObject(aluno);
		}

	}

	public void realizaOperacao(ObjectInputStream in, Socket cliente) {

		try {
			
//
//			FachadaDados fd = FachadaDados.getInstance();
//			Aluno	aluno = (Aluno) in.readObject();
//			if (aluno.isRespConfirmacao() == false) {
//				if (aluno.getOperacao() == 1) {
//					if (fd.consultar(aluno) == null) {
//						fd.inserir(aluno);
//						aluno.setInseriu(true);
//					}
//				} else if (aluno.getOperacao() == 2) {
//					if (fd.consultar(aluno) != null) {
//
//						if (fd.consultar(aluno).getSetor() == 1) {
//							aluno.setEnviarSetor(1);
//						} else if (fd.consultar(aluno).getSetor() == 2) {
//							aluno.setEnviarSetor(2);
//						}
//					}
//				}
//			} else {
//				if (aluno.getSetor() == 1) {
//					aluno.setEnviarSetor(2);
//				} else if (aluno.getSetor() == 2) {
//					aluno.setEnviarSetor(1);
//				}
//
//			}
//			ObjectOutputStream out = new ObjectOutputStream(
//					new DataOutputStream(cliente.getOutputStream()));
//
//			this.distribuiMensagem(aluno, out);
		} catch (Exception e) {
		}
	}

}

class TrataCliente implements Runnable {

	private ObjectInputStream in;
	private Matriz servidor;
	private Socket cliente;

	public TrataCliente(ObjectInputStream inputStream, Matriz servidor,
			Socket setor) {
		this.in = inputStream;
		this.servidor = servidor;
		this.cliente = setor;
	}

	public void run() {

		servidor.realizaOperacao(this.in, this.cliente);
	}

}
