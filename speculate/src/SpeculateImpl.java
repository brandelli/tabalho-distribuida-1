import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class SpeculateImpl extends UnicastRemoteObject implements SpeculateInterface {
	private static final long serialVersionUID = 1234L;
	private static final int maxJogadores = 2;
	private static Map<String, Integer> jogadores = new Hashtable<String, Integer>(maxJogadores);
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
			if(jogadores.containsKey(nome)) {
				codigoDeRetorno = -1;
			//caso o número de jogadores esteja no limite
			} else if(jogadores.size() == maxJogadores) {
				codigoDeRetorno = -2;
			//caso esteja tudo OK para cadastrar o jogador
			} else {
				int id = this.getPID();
				jogadores.put(nome, id);
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
