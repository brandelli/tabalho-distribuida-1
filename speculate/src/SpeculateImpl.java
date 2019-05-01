import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class SpeculateImpl extends UnicastRemoteObject implements SpeculateInterface {
	private static final long serialVersionUID = 1234L;
	private static final int maxJogos = 2;
	private static final int maxJogadores = 2  * maxJogos;
	private static Map<Integer, Partida> dictPartidas = new Hashtable<Integer, Partida>(maxJogadores);
	private static Map<String, Jogador> jogadoresRegistrados = new Hashtable<String, Jogador>(maxJogadores);
	private static Jogador jogadorEmEspera = null;
	private static Semaphore semaforo = new Semaphore(1);
	
	protected SpeculateImpl() throws RemoteException {
		
	}
	
	private Partida getPartida(int id) {
		return dictPartidas.get(id);
	}
	
	@Override
	public int getPID() throws RemoteException{
		return ProcessoID.getPID();
	}

	@Override
	public int registraJogador(String nome) throws RemoteException {
		int codigoDeRetorno = 0;
		try {
			semaforo.acquire();
			//caso o jogador já esteja cadastrado
			if(jogadoresRegistrados.containsKey(nome)) {
				codigoDeRetorno = -1;
			//caso o número de jogadores esteja no limite
			} else if(jogadoresRegistrados.size() == maxJogadores) {
				codigoDeRetorno = -2;
			//caso esteja tudo OK para cadastrar o jogador
			} else {
				int id = this.getPID();
				Jogador jogador = new Jogador(id, nome);
				//caso tenha algum jogador esperando partida
				if(jogadorEmEspera != null) {
					Partida partida = dictPartidas.get(jogadorEmEspera.getId());
					partida.setJogador1(jogadorEmEspera);
					partida.setJogador2(jogador);
					dictPartidas.put(id, partida);
					jogadoresRegistrados.put(nome, jogador);
					System.out.println("Partida criada entre "+ jogadorEmEspera.getNome() +" e " + nome);
					jogadorEmEspera = null;
				}else {
					jogadorEmEspera = jogador;
					jogadoresRegistrados.put(nome, jogador);
					dictPartidas.put(id, new Partida());
				}
				codigoDeRetorno = id;
			}
		} catch(Exception e) {
			System.out.println("Erro em registraJogador");
			e.printStackTrace();
		} finally {
			semaforo.release();
		}
		return codigoDeRetorno;
	}

	@Override
	public int encerraPartida(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int temPartida(int id) throws RemoteException {
		Partida partida = this.getPartida(id);
		if(partida == null) {
			return -1;
		}
		
		Jogador jogador1 = partida.getJogador1();
		Jogador jogador2 = partida.getJogador2();
		
		if(jogador2 == null) {
			return 0;
		}
		
		if(jogador1.getId() == id) {
			return 1;
		}
		
		if(jogador2.getId() == id) {
			return 2;
		}
		
		return -2;
		
	}

	@Override
	public String obtemOponente(int id) throws RemoteException {
		Partida partida = this.getPartida(id);
		if(partida != null) {
			Jogador jogador1 = partida.getJogador1();
			Jogador jogador2 = partida.getJogador2();
			
			if(jogador1.getId() == id) {
				if(jogador2 != null) {
					return jogador2.getNome();
				}
			} else {
				return jogador1.getNome();
			}
		}
		return "";
	}

	@Override
	public int ehMinhaVez(int id) throws RemoteException {
		Partida partida = this.getPartida(id);
		
		// jogador não encontrado em nunhuma partida
		if(partida == null) {
			return -1;
		}
		
		// caso não tenha 2 jogadores na partida
		if(partida.getJogador2() == null) {
			return -2;
		}
		
		// caso ainda não tenha vencedor na partida
		if(partida.getVencedor() != null) {
			// proxima jogada é do cliente que fez a chamada
			if(partida.getProximaJogada().getId() == id) {
				return 1;
			}
			// proxima jogada é do oponente;
			return 0;
		// já está definido o vencedor
		} else {
			Jogador vencedor = partida.getVencedor();
			// caso o jogo tenha acabado devido a uma desistencia
			if(partida.getDesistencia() == true) {
				// vencedor por WO é o cliente que fez a chamada
				if(vencedor.getId() == id) {
					return 5;
				}
				// perdedor por WO é o cliente qu efez a chamada
				return 6;
			} else {
				// vencedor é o cliente que fez a chamada
				if(vencedor.getId() == id) {
					return 2;
				}
				// perdedor é o cliente que fez a chamada
				return 3;
			}
		}
	}

	@Override
	public int obtemNumBolas(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int obtemNumBolasOponente(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String obtemTabuleiro(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int defineJogadas(int id, int jogadas) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int jogaDado(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
