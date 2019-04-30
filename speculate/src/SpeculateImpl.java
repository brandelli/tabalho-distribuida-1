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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String obtemOponente(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int ehMinhaVez(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
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
