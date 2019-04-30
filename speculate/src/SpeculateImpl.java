import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SpeculateImpl extends UnicastRemoteObject implements SpeculateInterface {
	private static final long serialVersionUID = 1234L;
	
	protected SpeculateImpl() throws RemoteException {
		
	}
	
	@Override
	public int getPID() throws RemoteException{
		return new ProcessoID().getPID();
	}

	@Override
	public int registraJogador(String nome) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
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
