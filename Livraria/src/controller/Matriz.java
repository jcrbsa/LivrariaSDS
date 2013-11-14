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

public class Matriz  implements Runnable {

	public static void main(String[] args) throws IOException {

		new Matriz(12345).executa();
	}

	private int porta;
	private static List<Socket> setores = new ArrayList<Socket>();
	private static List<ObjectInputStream> objectinputstream = new ArrayList<ObjectInputStream>();
	private static List<ObjectOutputStream> objectoutputstream = new ArrayList<ObjectOutputStream>();
	private static  List<Aluno> alunos = new ArrayList<Aluno>();
	private static int id = 0;
	
	private Socket setor;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private ObjectInputStream in_outroSetor;
	private ObjectOutputStream out_outroSetor;

	public Matriz( Socket setor,
			ObjectInputStream in, ObjectOutputStream out) {

		this.setor = setor;
		this.in = in;
		this.out = out;
	}
	
	public Matriz( Socket setor,
			ObjectInputStream in_orig, ObjectOutputStream out_orig, ObjectInputStream in_dest, ObjectOutputStream out_dest) {

		this.setor = setor;
		this.in = in_orig;
		this.out = out_orig;
		this.in_outroSetor = in_orig;
		this.out_outroSetor = out_orig;
	}

	public Matriz(int porta) {
		this.porta = porta;
	}

	public void executa() throws IOException {
	
		ServerSocket matriz = new ServerSocket(this.porta);
		while (setores.size() < 2) {

			Socket setor = matriz.accept();
			setores.add(setor);
			System.out.println("Nova conexão com o cliente "
					+ setor.getInetAddress().getHostAddress());
			
			
			ObjectOutputStream out = new ObjectOutputStream(
					new DataOutputStream(setor.getOutputStream()));
			objectoutputstream.add(out);
			ObjectInputStream in = new ObjectInputStream(new DataInputStream(
					setor.getInputStream()));
			objectinputstream.add(in);

		
			
		}
		Matriz tc = new Matriz(setor, objectinputstream.get(0),objectoutputstream.get(0),objectinputstream.get(1),objectoutputstream.get(1) );
		new Thread(tc).start();
		Matriz tc2 = new Matriz(setor, objectinputstream.get(1),objectoutputstream.get(1),objectinputstream.get(0),objectoutputstream.get(0) );
		new Thread(tc2).start();
		
	}

	static Socket retornaOutroSocket(Socket socket) {

		for (Socket setor : setores) {
			if (socket.getInetAddress() != setor.getInetAddress()) {
				return setor;
			}
		}
		return null;
	}

	static Aluno ExisteAlunoNoMatriz(String matricula) {

		for (Aluno aluno : alunos) {
			if (aluno.getMatricula().equals(matricula)) {
				return aluno;

			}
		}
		return null;
	}

	static void ListarTodosAlunosMatriz() {

		char setor;
		int i = 0;
		System.out.println("|Id||  Matrícula  ||Setor|");
		for (Aluno aluno : alunos) {
			
			setor = aluno.getSetor() == 1 ? 'A':'B';
			System.out.println("|" + i +  "||"
					+ aluno.getMatricula() + "||"+ setor +"|");
			i++;
		}
	}

	public void run() {
		
		try {	
		boolean flag = false;

		while (!flag) {
			Aluno aluno = (Aluno) in.readObject();

			if (aluno.getOperacao() == 1) {

				Aluno aluno_cadastro = new Aluno();
				aluno_cadastro.setMatricula(aluno.getMatricula());
				System.out.println("Matricula:" + aluno.getMatricula());
				if (ExisteAlunoNoMatriz(aluno_cadastro.getMatricula()) == null) {
					if (aluno.getSetor() == 1) {
						aluno_cadastro.setSetor(1);
					}
					if (aluno.getSetor() == 2) {
						aluno_cadastro.setSetor(2);
					}
					System.out.println("Cadastrando... ");
					aluno.setCodigo(id);
					alunos.add(id, aluno_cadastro);
					id++;
					aluno.setInseriu(true);
					System.out.println("Cadastro Realizado com Sucesso");
					System.out
							.println("-------------Todos Alunos Cadastrados --------");
					ListarTodosAlunosMatriz();
				} else {
					aluno.setEncontrou(true);
					System.out.println("Aluno Já Existe ");
				}
				aluno.setEstado(1);
				out.writeObject(aluno);
			}
			if (aluno.getOperacao() == 2) {
				if (setores.size() == 2) {
					System.out.println("Operação Consulta:");
					Aluno aluno_consulta = new Aluno();
					if (ExisteAlunoNoMatriz(aluno.getMatricula()) != null) {

						aluno_consulta = ExisteAlunoNoMatriz(aluno
								.getMatricula());
						Socket outroSetor = null;
						//Socket outroSetor = retornaOutroSocket(setor);
						if(aluno.getSetor() == 1){
						outroSetor = setores.get(1);
						}else if(aluno.getSetor() == 2){
							outroSetor = setores.get(0);
						}
						System.out
								.println("Consultar Informações do Aluno - Endereço:"
										+ outroSetor.getInetAddress()
												.getHostAddress());
						ObjectInputStream in_outroSetor = new ObjectInputStream(
								new DataInputStream(
										outroSetor.getInputStream()));
						ObjectOutputStream out_outroSetor = new ObjectOutputStream(
								new DataOutputStream(
										outroSetor.getOutputStream()));
						aluno_consulta.setEncontrou(true);
						System.out.println("Encontrado referencia p/ valor na Matriz");
						aluno_consulta.setEstado(2);
						System.out.println("Enviando Solicitação...");
						out_outroSetor.writeObject(aluno_consulta);
						Aluno aluno_consulta_setor = (Aluno) in_outroSetor
								.readObject();
						System.out.println("Solicitação Recebida");

						if (aluno_consulta_setor.isEncontrou() == true) {

							// int pos = aluno_consulta.getCodigo();
							aluno.setEstado(1);
							System.out.println("Enviando Solicitação Recebida p/ Solicitante...");
							out.writeObject(aluno_consulta_setor);
							aluno_consulta = (Aluno) in.readObject();
							

							if (aluno_consulta.isEdit() == true) {
								/*
								 * alunos.remove(pos); alunos.add(pos,
								 * aluno_consulta);
								 */
								// ListarTodosAlunosMatriz();
								System.out.println("Solicitante Editando...");
								aluno_consulta.setEstado(2);
								out_outroSetor.writeObject(aluno_consulta);
								System.out.println("Enviando Edicao p/ atualizacao...");
								aluno_consulta_setor = (Aluno) in_outroSetor
										.readObject();
								if (aluno_consulta_setor
										.isRespConfirmacao() == true) {
									System.out
											.println("Edicao Realizada Com Sucesso!!!");
								} else {
									System.out
											.println("Nenhuma Edicao Realizada!");
								}

							} else {
								System.out
										.println("Nenhuma Edicao Iniciada!");
							}
						} else {
							System.out
									.println("Nenhum Registro Encontrado no Setor!");
						}
					} else {
						aluno_consulta.setEncontrou(false);
						System.out
								.println("Nenhum Registro Encontrado na Matriz!");
					}
					aluno.setEstado(1);
					out.writeObject(aluno_consulta);
				} else {
					System.out
							.println("Nem todos os Setores estão Conectados!");
				}
			}
		
		}
	
		
	} catch (Exception e) {
	}
	
}
	
}
