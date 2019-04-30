import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;

public class SpeculateClient {
	public static void main(String[] args) {
		try {
			SpeculateInterface speculate = (SpeculateInterface) Naming.lookup("//localhost/Speculate");
			BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Bem vindo ao jogo Speculate");
			
			
			while(true) {
				int id = -1;
				System.out.println("Digite seu nome de usuário");
				String usuario = scanner.readLine();
				id = speculate.registraJogador(usuario);
				
				if(id == -1) {
					System.out.println("Nome de usuário já cadastrado");
					break;
				} else if(id == -2) {
					System.out.println("Número máximo de jogadores já alcancado");
					break;
				}
				
				System.out.println(usuario +": "+id);
				System.out.println("Buscando partida");
				
			}
			
		}catch(Exception e) {
			System.out.println("Speculate client failed");
			e.printStackTrace();
		}
	}
}
