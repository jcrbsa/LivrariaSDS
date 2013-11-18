package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import controller.SetorA;
import controller.SetorB;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ArrayList;

import bean.Aluno;

public class Matriz implements Runnable {

	public static void main(String[] args) throws IOException {

		new Matriz(12345).executa();
	}

	private int porta;
	private static List<Socket> setores = new ArrayList<Socket>();
	private static List<ObjectInputStream> objectinputstream = new ArrayList<ObjectInputStream>();
	private static List<ObjectOutputStream> objectoutputstream = new ArrayList<ObjectOutputStream>();
	private static List<Aluno> alunos = new ArrayList<Aluno>();
	private static int id = 0;

	private ServerSocket matriz;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private static ObjectInputStream in_outroSetor;
	private static ObjectOutputStream out_outroSetor;
	private static SetorA setorA;
	private static SetorB setorB;
	private Socket dest;

	public Matriz(ServerSocket serverSocket, ObjectInputStream in,
			ObjectOutputStream out, Socket destino, ObjectInputStream in_dest,
			ObjectOutputStream out_dest, int i) throws IOException {

		matriz = serverSocket;
		this.in = in;
		this.out = out;
		dest = destino;
		if (i == 1) {
			setorA = new SetorA(in_dest, out_dest, dest.getInetAddress()
					.getHostAddress(), dest.getPort());
		} else if (i == 2) {
			setorB = new SetorB(in_dest, out_dest, dest.getInetAddress()
					.getHostAddress(), dest.getPort());
		}

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
		Matriz tc = new Matriz(matriz, objectinputstream.get(0),
				objectoutputstream.get(0), setores.get(1),
				objectinputstream.get(1), objectoutputstream.get(1), 1);
		new Thread(tc).start();
		Matriz tc2 = new Matriz(matriz, objectinputstream.get(1),
				objectoutputstream.get(1), setores.get(0),
				objectinputstream.get(0), objectoutputstream.get(0), 2);
		new Thread(tc2).start();
		matriz.close();
	}

	Socket retornaOutroSocket(Socket socket) {

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

			setor = aluno.getSetor() == 1 ? 'A' : 'B';
			System.out.println("|" + i + "||" + aluno.getMatricula() + "||"
					+ setor + "|");
			i++;
		}
	}

	public static ObjectOutputStream defineCanalOutput(int x) {

		if (x == 1) {

			return setorA.out;
		} else

			return setorB.out;

	}

	public ObjectInputStream defineCanalInput(int x) {

		if (x == 1) {

			return setorA.in;
		} else

			return setorB.in;

	}

	public void run() {
		try {

			while (true) {
				Aluno aluno = (Aluno) in.readObject();

				if (aluno.getOperacao() == 1) {

					Aluno aluno_cadastro = new Aluno();
					aluno_cadastro.setMatricula(aluno.getMatricula());
					System.out.println("Matricula:" + aluno.getMatricula());
					aluno.setEstado(1);
					aluno.setOperacao(1);
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
						aluno.setEncontrou(false);
						aluno.setInseriu(true);
						System.out.println("Cadastro Realizado com Sucesso");
						System.out
								.println("-------------Todos Alunos Cadastrados --------");
						ListarTodosAlunosMatriz();
					} else {
						aluno.setEncontrou(true);
					}
					out.writeObject(aluno);
				} else if (aluno.getOperacao() == 2) {
					if (setores.size() == 2) {
						System.out.println("Consultando...");
						if (ExisteAlunoNoMatriz(aluno.getMatricula()) != null) {
						
							if (aluno.getConsulta() == 1) {
								if (aluno.getSetor() == 1) {
									in_outroSetor = setorA.getIn();
									out_outroSetor = setorA.getOut();
								} else if (aluno.getSetor() == 2) {
									in_outroSetor = setorB.getIn();
									out_outroSetor = setorB.getOut();
								}

								in_outroSetor = defineCanalInput(aluno.getSetor());
								out_outroSetor = defineCanalOutput(aluno.getSetor());

								
								aluno = ExisteAlunoNoMatriz(aluno
										.getMatricula());

								aluno.setEncontrou(true);

								System.out
										.println("Encontrado referencia p/ valor na Matriz");
								aluno.setEstado(2);
								aluno.setConsulta(1);
								System.out.println("Enviando Solicitação...");
								System.out.println("Aguardando Resposta...");

								out_outroSetor.writeObject(aluno);
								//Recebey de A, Se A vai p B
							} else if (aluno.getConsulta() == 2) {

								if (aluno.getSetor() == 2) {
									in_outroSetor = setorA.getIn();
									out_outroSetor = setorA.getOut();
								} else if (aluno.getSetor() == 1) {
									in_outroSetor = setorB.getIn();
									out_outroSetor = setorB.getOut();
								}

								in_outroSetor = defineCanalInput(aluno
										.getSetor());
								out_outroSetor = defineCanalOutput(aluno
										.getSetor());
					
								aluno.setEstado(1);
								aluno.setOperacao(2);
								aluno.setEncontrou(true);
								System.out
										.println("Enviando Solicitação Recebida p/ Solicitante...");
								out_outroSetor.writeObject(aluno);
								//Setor B enviou
							} else if (aluno.getConsulta() == 3) {

								if (aluno.getSetor() == 1) {
									in_outroSetor = setorA.getIn();
									out_outroSetor = setorA.getOut();
								} else if (aluno.getSetor() == 2) {
									in_outroSetor = setorB.getIn();
									out_outroSetor = setorB.getOut();
								}

								in_outroSetor = defineCanalInput(aluno
										.getSetor());
								out_outroSetor = defineCanalOutput(aluno
										.getSetor());

								if (aluno.isEdit() == true) {
									System.out
											.println("Solicitante Editou...");
									aluno.setEstado(2);
									aluno.setConsulta(3);
									out_outroSetor.writeObject(aluno);
									System.out.println("Enviando Edicao p/ atualizacao...");
								}
								} else if (aluno.getConsulta() == 4) {

									if (aluno.isRespConfirmacao() == true) {
										System.out
												.println("Edicao Realizada Com Sucesso!!!");
									} else {
										System.out
												.println("Nenhuma Edicao Realizada!");
									}
								}
						} else {
							aluno.setEncontrou(false);
							System.out
									.println("Nenhum Registro Encontrado na Matriz!");
						}
						// aluno.setEstado(1);
						// aluno.setOperacao(2);
						// out.writeObject(aluno);
					} else {
						System.out
								.println("Nem todos os Setores estão Conectados!");
					}
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			System.out.println(cnfe.getMessage());
		}

	}

}
