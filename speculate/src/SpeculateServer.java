import java.rmi.Naming;
import java.rmi.RemoteException;

public class SpeculateServer {
	public static void main(String[] args){
		try {
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println("RMI registry ready.");
		} catch (RemoteException e) {
			System.out.println("RMI registry already running");
		}
		
		try {
			Naming.rebind("Speculate", new SpeculateImpl());
			System.out.println("Speculate Server is ready");
		} catch (Exception e) {
			System.out.println("Speculate Server failed");
			e.printStackTrace();
		}
	}
}
