import java.util.concurrent.Semaphore;

public class ProcessoID {
	private static int nextID = 0;
	private static Semaphore semaforo = new Semaphore(1);
	
	static int getPID() {
		try{
			semaforo.acquire();
			nextID++;
		}catch(Exception e) {
			System.out.println("Falha no semaforo de criação de id de processo");
			e.printStackTrace();
		}finally {
			semaforo.release();
		}
		return nextID;
	}
}
