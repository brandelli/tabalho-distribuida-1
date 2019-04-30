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
				String jogador = scanner.readLine();
				id = speculate.registraJogador(jogador);
				
				// mensagem de erro de jogador já cadastrado
				if(id == -1) {
					System.out.println("Nome de usuário já cadastrado");
				// mensagem de erro de número máximo de jogadores alcançados
				} else if(id == -2) {
					System.out.println("Número máximo de jogadores já alcancado");
				// jogador cadastrado com sucesso, parte para busca de partida
				}else {
					System.out.println(jogador +": "+id);
					System.out.println("Buscando partida");
					
					int temPartida = speculate.temPartida(id);
					while(temPartida == 0) {
						temPartida = speculate.temPartida(id);
					}
					
					System.out.println("Achou Oponente");
					
					if(temPartida < 0) {
						if(temPartida == -2) {
							System.out.println("Tempo de procura esgotado");
						} else if(temPartida == -1) {
							System.out.println("Erro na partida");
						}
					} else {
						System.out.println("Partida encontrada, seu oponente é: " + speculate.obtemOponente(id));
						if(temPartida == 1) {
							System.out.println("Você joga primeiro");
						}else if(temPartida == 2) {
							System.out.println("Seu oponente começa jogando");
						}
					}
					
				}
				
			}
			
		}catch(Exception e) {
			System.out.println("Speculate client failed");
			e.printStackTrace();
		}
	}
}
