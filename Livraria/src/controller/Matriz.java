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
	private static List<Aluno> alunos = new ArrayList<Aluno>();
	private static int id = 0;
	
	private Socket setor;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public Matriz( Socket setor,
			ObjectInputStream in, ObjectOutputStream out) {

		this.setor = setor;
		this.in = in;
		this.out = out;
	}

	public Matriz(int porta) {
		this.porta = porta;
	}

	public void executa() throws IOException {
	
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

			Matriz tc = new Matriz(setor, in, out);
			new Thread(tc).start();
			
		}
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
				out.writeObject(aluno);
			}
			if (aluno.getOperacao() == 2) {
				if (setores.size() == 2) {
					System.out.println("Operação Consulta:");
					Aluno aluno_consulta = new Aluno();
					if (ExisteAlunoNoMatriz(aluno.getMatricula()) != null) {

						aluno_consulta = ExisteAlunoNoMatriz(aluno
								.getMatricula());

						Socket outroSetor = retornaOutroSocket(setor);
						System.out
								.println("Consultar Informações do Aluno - Endereço:"
										+ outroSetor.getInetAddress()
												.getHostAddress());
						ObjectOutputStream out_outroSetor = new ObjectOutputStream(
								new DataOutputStream(
										outroSetor.getOutputStream()));
						ObjectInputStream in_outroSetor = new ObjectInputStream(
								new DataInputStream(
										outroSetor.getInputStream()));

						out_outroSetor.writeObject(aluno_consulta);
						Aluno aluno_consulta_setor = (Aluno) in_outroSetor
								.readObject();

						if (aluno_consulta_setor.isEncontrou() == true) {

							// int pos = aluno_consulta.getCodigo();
							out.writeObject(aluno_consulta_setor);
							aluno_consulta = (Aluno) in.readObject();

							if (aluno_consulta.isEdit() == true) {
								/*
								 * alunos.remove(pos); alunos.add(pos,
								 * aluno_consulta);
								 */
								// ListarTodosAlunosMatriz();
								out_outroSetor.writeObject(aluno_consulta);
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
					out.writeObject(aluno_consulta);
				} else {
					System.out
							.println("Nem todos os Setores estão Conectados!");
				}
			}
			aluno = (Aluno) in.readObject();
			if (aluno.getOperacao() == 0) {
				flag = true;
			}
		}
	
		
	} catch (Exception e) {
	}
	
}
	
}
