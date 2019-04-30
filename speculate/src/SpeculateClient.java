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
				System.out.println("Digite seu nome de usu�rio");
				String jogador = scanner.readLine();
				id = speculate.registraJogador(jogador);
				
				if(id == -1) {
					System.out.println("Nome de usu�rio j� cadastrado");
				} else if(id == -2) {
					System.out.println("N�mero m�ximo de jogadores j� alcancado");
				}else {
					System.out.println(jogador +": "+id);
					System.out.println("Buscando partida");
					
					while(true) {
						
					}
				}
				
			}
			
		}catch(Exception e) {
			System.out.println("Speculate client failed");
			e.printStackTrace();
		}
	}
}
