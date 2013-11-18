package controller;

import bean.Aluno;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ArrayList;

public class SetorA implements Runnable {

	static Reader r = new InputStreamReader(System.in);
	static StreamTokenizer st = new StreamTokenizer(r);
	private int porta;
	private String host;
	private static List<Aluno> alunos = new ArrayList<Aluno>();
	private static int id = 0;

	public ObjectInputStream in;
	public ObjectOutputStream out;

	public ObjectInputStream getIn() {
		return in;
	}

	public void setIn(ObjectInputStream in) {
		this.in = in;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}


	public SetorA(ObjectInputStream entrada, ObjectOutputStream saida,
			Socket setor) throws UnknownHostException, IOException {

		in = entrada;
		out = saida;

	}

	public SetorA(ObjectInputStream entrada, ObjectOutputStream saida,
			String host, int porta) throws UnknownHostException, IOException {

		in = entrada;
		out = saida;
		this.host = host;
		this.porta = porta;

	}

	public void run() {
		try {
			while (true) {
				 Aluno teste = (Aluno) in.readObject();
				if (teste.getEstado() == 2) {
					if (teste.getConsulta() == 1) {

						teste = ExisteAlunoNoSetorA(teste.getMatricula());
						teste.setOperacao(2);
						teste.setConsulta(2);
						this.out.writeObject(teste);
						
					} else if(teste.getConsulta() == 3) {

						if (teste.isEdit() == true) 
						{
							alunos.remove(teste.getId());
							alunos.add(teste.getId(), teste);
							teste.setRespConfirmacao(true);
							teste.setConsulta(4);
							this.out.writeObject(teste);
						}
					}

				} else if (teste.getEstado() == 1) {
					if (teste.getOperacao() == 1) {
						if (teste.isEncontrou() != true) {
							teste.setId(id);
							alunos.add(id, teste);
							id++;
							System.out
									.println("Aluno Registrado no SetorA Com Sucesso!!!");
							System.out
									.println("Aluno Registrado na Central Com Sucesso!!!");
						} else if (teste.isEncontrou() != false) {
							System.out
									.println("Aluno já cadastrado no Sistema!!!");
						}
					} else if (teste.getOperacao() == 2) {
						int op;
						String nome;
						String matricula;
						if (teste.isEncontrou() != true) {
							System.out
									.println("Nenhum Registro Encontrado no Sistema!!!");
						} else {
							System.out.println("|Nome:" + teste.getNome()
									+ "||" + teste.getMatricula() + "||"
									+ teste.getSetor());
							op = getInt(
									"Alterar Informações registro 1=Sim,0-Não",
									1, 2);
							if (op == 1) {
								Aluno aluno_alteracao = new Aluno();
								nome = getString("Digite Nome:");
								matricula = getString("Digite Matricula:");
								aluno_alteracao.setNome(nome);
								aluno_alteracao.setMatricula(matricula);
								aluno_alteracao.setEdit(true);
								out.writeObject(aluno_alteracao);
							}
						}

					}
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException {

		new SetorA("127.0.0.1", 12345).executa();
	}

	public SetorA(String host, int porta) {
		this.host = host;
		this.porta = porta;
	}

	public void executa() throws IOException {

		Socket setor = new Socket(this.host, this.porta);

		System.out.println("Nova conexão com a matriz "
				+ setor.getInetAddress().getHostAddress());

		this.in = new ObjectInputStream(new DataInputStream(
				setor.getInputStream()));

		this.out = new ObjectOutputStream(new DataOutputStream(
				setor.getOutputStream()));

		// thiSetorA tc = new SetorA(in, out,setor);
		Thread test = new Thread(this);
		test.start();

		String nome;
		String matricula = null;
		Aluno aluno = new Aluno();

		int op;
		System.out.println("Bem Vindo Livraria");
		// sair = getString("\"cont\" p/ continuar (\"fim\" para encerrar)");
		while (true) {
			op = getInt("1=Cadastrar,2=Consulta,3=Listar Todos Usuários,", 1, 3);
			if (op == 1 || op == 2) {
				matricula = getString("Digite Matricula:");

				aluno.setMatricula(matricula);

			}
			switch (op) {
			case 1:
				Aluno aluno_cadastro = new Aluno();
				nome = getString("Digite Nome:");
				aluno_cadastro.setNome(nome);
				aluno_cadastro.setOperacao(1);
				aluno_cadastro.setMatricula(matricula);
				out.writeObject(aluno_cadastro);
				break;

			case 2:
				Aluno aluno_consulta = new Aluno();
				if (ExisteAlunoNoSetorA(matricula) != null) {
					aluno_consulta = ExisteAlunoNoSetorA(matricula);
					System.out.println("Aluno Encontrado no SetorA");
					System.out.println("|Nome:" + aluno_consulta.getNome()
							+ "||" + aluno_consulta.getMatricula() + "|");
				} else {
					System.out.println("Nenhum Registro Encontrado no Setor A");
					aluno_consulta.setMatricula(matricula);
					aluno_consulta.setOperacao(2);
					aluno_consulta.setSetor(1);
					out.writeObject(aluno_consulta);

				}

				break;
			case 3:
				System.out.println("Lista dos Alunos Cadastrados no SetorA");
				ListarTodosAlunosSetorA();

				break;

			default:
				System.out.println("Opcao Inválida");
				break;
			}

			// sair =
			// getString("Continuar' p/ prosseguir ou(\"fim\" para encerrar)");
		}
		// System.out.println("Programa encerrado");
		// setor.close();

	}

	static Aluno ExisteAlunoNoSetorA(String matricula) {

		for (Aluno aluno : alunos) {
			if (aluno.getMatricula().equals(matricula)) {
				return aluno;
			}
		}
		return null;
	}

	static void ListarTodosAlunosSetorA() {

		int i = 0;
		System.out.println("|Id||     Nome    ||  Matricula  |");
		for (Aluno aluno : alunos) {
			System.out.println("|" + i + "||    " + aluno.getNome() + "    ||"
					+ aluno.getMatricula() + "|");
			i++;
		}
	}

	static int getInt(String str, int de, int ate) throws IOException {
		do {
			System.out.println("Entre com " + str);
			try {
				st.nextToken();
			} catch (IOException e) {
				System.out.println("Erro na leitura do teclado");
				return (0);
			}
		} while (st.ttype != StreamTokenizer.TT_NUMBER || st.nval < de
				|| st.nval > ate);
		return ((int) st.nval);
	}

	static String getString(String str) throws IOException {
		String in = new String();
		System.out.println("Entre com " + str);
		int quoteChar = '"';
		st.eolIsSignificant(true);

		do {
			try {
				st.nextToken();
			} catch (IOException e) {
				System.out.println("Erro na leitura do teclado");
				return ("");
			}

			if (st.ttype == StreamTokenizer.TT_NUMBER) {
				Double test = st.nval;
				Integer test2 = test.intValue();
				in = in.concat(test2.toString());
			}
			if (st.ttype == quoteChar || st.ttype == StreamTokenizer.TT_WORD) {
				/* if (in.length() > 0)in = in.concat(" "); */
				in = in.concat(st.sval);
			}
		} while (in.length() == 0 || st.ttype != StreamTokenizer.TT_EOL);
		st.eolIsSignificant(false);
		return (in);
	}

}
