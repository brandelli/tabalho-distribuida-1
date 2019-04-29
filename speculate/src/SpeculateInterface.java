import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SpeculateInterface extends Remote {
	public int registraJogador(String nome) throws RemoteException;
	public int encerraPartida(int id) throws RemoteException;
	public int temPartida(int id) throws RemoteException;
	public String obtemOponente(int id) throws RemoteException;
	public int ehMinhaVez(int id) throws RemoteException;
	public int obtemNumBolas(int id) throws RemoteException;
	public int obtemNumBolasOponente(int id) throws RemoteException;
	public String obtemTabuleiro(int id) throws RemoteException;
	public int defineJogadas(int id, int jogadas) throws RemoteException;
	public int jogaDado(int id) throws RemoteException;
}
