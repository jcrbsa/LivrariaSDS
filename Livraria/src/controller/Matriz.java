package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import fachada.FachadaDados;

import bean.Aluno;

public class Matriz implements Serializable {

	
	private List<ObjectOutputStream> clientes;
	
	public static void main(String[] args) {

		try {
			ServerSocket matriz = new ServerSocket(6789);
			Socket escreveMatrizParaA = new Socket("localhost", 4589);
			Socket escreveMatrizParaB = new Socket("localhost", 4889);
		

			while (true) {
				Socket cliente = matriz.accept();
				System.out.println("Nova conexão com o cliente "
						+ cliente.getInetAddress().getHostAddress());
				ObjectInputStream in = new ObjectInputStream(
						new DataInputStream(cliente.getInputStream()));
				ObjectOutputStream out = new ObjectOutputStream(
						new DataOutputStream(
								escreveMatrizParaA.getOutputStream()));

				ObjectOutputStream outB = new ObjectOutputStream(
						new DataOutputStream(
								escreveMatrizParaB.getOutputStream()));
				
				Aluno aluno = (Aluno) in.readObject();
				TrataCliente tc = new TrataCliente(in, matriz, cliente, aluno);
				new Thread(tc).start();

			
				FachadaDados fd = FachadaDados.getInstance();

				if (aluno.getOperacao() == 1) {
					if(fd.consultar(aluno) == null)
					fd.inserir(aluno);
					else
						if (aluno.getSetor() == 1)
							//outA.write(null);		
						if (aluno.getSetor() == 2)
							outB.write(null);
				} else {
					// B consulta A
					if (aluno.getSetor() == 1) {
						// Procura no Setor
						if (aluno.getRespConfirmacao() == 1)
							//outA.writeObject(aluno);
						else
							outB.writeObject(aluno);

					} else if (aluno.getSetor() == 2) {
						if (aluno.getRespConfirmacao() == 1)
							outB.writeObject(aluno);
						else
							//outA.writeObject(aluno);

					}

				}

			}

		}

		catch (Exception e) {

		}
	}
	
	public void distribuiMensagem(Aluno aluno, ObjectOutputStream saida ) throws IOException {
		 for (ObjectOutputStream out : this.clientes) {

		      if(!out.equals(saida));
			 	out.writeObject(aluno);
		}

}
	
	class TrataCliente implements Runnable {

		 private ObjectInputStream cliente;
		 private ServerSocket servidor;
		 private Socket cliente_tc;
		 private Aluno aluno;

		 public TrataCliente(ObjectInputStream  inputStream, ServerSocket servidor, Socket cliente_tc, Aluno aluno) {
		 this.cliente = inputStream;
		 this.servidor = servidor;
		 this.cliente_tc = cliente_tc;
		 this.aluno = aluno;
		 }

		 public void run() {
			 
			 ObjectOutputStream saida = new ObjectOutputStream(new DataOutputStream(cliente_tc.getOutputStream()));
		 servidor.distribuiMensagem(this.aluno, saida );
		 }
	
		 }
		 }
