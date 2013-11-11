import java.io.*;
import java.util.*;

class Main {

	//static BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
	static Reader r =  new InputStreamReader(System.in);
	static StreamTokenizer st = new StreamTokenizer(r);
	static List<Usuario> vetorUsuarios = new ArrayList<Usuario>();
	static List<Livro> vetorLivros = new ArrayList<Livro>();

	public static void main(String Arg[]) throws IOException {
		Usuario usuario;
		//Livro livro;
		int op;
		cadastraLivros();
		cadastraUsuarios();
		System.out.println("\nAtendimento aos usuarios");
		usuario = getUsuario(); // seleciona usuario
		while (usuario != null) {
			System.out.println(usuario);
			op = getOperacao(usuario); // seleciona operacao
			executaOperacao(usuario, op); // e executa
			usuario = getUsuario();
		}
	}

	static void cadastraLivros() throws IOException {
		Livro livro;
		String titulo;
		int op;
		System.out.println("Cadastramento dos livros");
		titulo = getString("titulo (\"fim\" para encerrar)");
		while (!titulo.equals("fim")) {
			op = getInt("1=livro, 2=periodico", 1, 2);
			if (op == 1)
				livro = new Livro(titulo);
			else
				livro = new Periodico(titulo);
			vetorLivros.add(livro);
			System.out.println(livro + " incluido com # " + vetorLivros.size()
					+ "\n");
			titulo = getString("proximo titulo (\"fim\" para encerrar)");
		}
	}

	static void cadastraUsuarios() throws IOException {
		Usuario usuario;
		String nome;
		int op;
		GregorianCalendar cal=new GregorianCalendar();
		cal.add(Calendar.MONTH, 6);
		Date dt=cal.getTime();
		System.out.println("Cadastramento dos usuarios");
		nome=getString("nome (\"fim\" para encerrar)");
		while (!nome.equals("fim")) {
		op=getInt
		("1=usuario externo, 2=aluno, 3=professor",1,3);
		if (op==1) usuario=new Usuario(nome); else

		if (op==2) usuario=new UsuarioAluno(nome, dt);
		else usuario=new UsuarioProfessor(nome);
		vetorUsuarios.add(usuario);
		System.out.println(usuario+" incluido com # "+
		vetorUsuarios.size()+"\n");
		nome=getString("proximo nome (\"fim\" para encerrar)");
		}
	}

	static int getOperacao(Usuario usuario) throws IOException {
		int op;
		if (usuario.isProfessor())
			op = getInt("operacao:\n" + " 1=retira, 2=devolve, 3=ver carga,"
					+ " 4=bloqueia, 5=desbloqueia", 1, 5);
		else
			op = getInt("operacao:\n" + " 1=retira, 2=devolve, 3=ver carga", 1,
					3);
		return (op);
	}

	static Usuario getUsuario() throws IOException {
		int i = getInt("numero do usuario (0=fim)", 0, vetorUsuarios.size());
		if (i > 0)
			return ((Usuario) vetorUsuarios.get(--i));
		else
			return (null);
	}

	static Livro getLivro() throws IOException {
		int i = getInt("livro", 1, vetorLivros.size());
		return ((Livro) vetorLivros.get(--i));
	}

	static void executaOperacao(Usuario usuario, int op) throws IOException {
		Livro livro;
		boolean r = false;
		int i;

		if (op == 3)
			usuario.listaCarga();
		else {
			livro = getLivro();
			System.out.println(livro);
			switch (op) {
			case 1:
				r = usuario.retiraLivro(livro);
				break;
			case 2:
				r = usuario.devolveLivro(livro);
				break;
			case 4:
				i = getInt("prazo", 1, 20);
				r = ((UsuarioProfessor)usuario).bloqueiaLivro(livro, op);
				break;
			case 5:
				r = ((UsuarioProfessor)usuario).desbloqueiaLivro(livro);
				break;
			}
			if (r == true)
				System.out.println(livro);
			else
				System.out.println("Operacao rejeitada");
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
			if (st.ttype == quoteChar || st.ttype == StreamTokenizer.TT_WORD) {
				if (in.length() > 0)
					in = in.concat(" ");
				in = in.concat(st.sval);
			}
		} while (in.length() == 0 || st.ttype != StreamTokenizer.TT_EOL);
		st.eolIsSignificant(false);
		return (in);
	}

}