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
				
				// mensagem de erro de jogador j� cadastrado
				if(id == -1) {
					System.out.println("Nome de usu�rio j� cadastrado");
				// mensagem de erro de n�mero m�ximo de jogadores alcan�ados
				} else if(id == -2) {
					System.out.println("N�mero m�ximo de jogadores j� alcancado");
				// jogador cadastrado com sucesso, parte para busca de partida
				} else {
					System.out.println(jogador +": "+id);
					System.out.println("Buscando partida");
					
					int temPartida = speculate.temPartida(id);
					while(temPartida == 0) {
						temPartida = speculate.temPartida(id);
					}
					
					System.out.println("Achou Oponente");
					
					if(temPartida < 0) {
						mensagensErroAoAcharPartida(temPartida);
					} else {
						System.out.println("Partida encontrada, seu oponente �: " + speculate.obtemOponente(id));
						if(temPartida == 1) {
							System.out.println("Voc� joga primeiro");
						}else if(temPartida == 2) {
							System.out.println("Seu oponente come�a jogando");
						}
						
						int ehMinhaVez = speculate.ehMinhaVez(id);
						while(ehMinhaVez == 0 || ehMinhaVez == 1) {
							if(ehMinhaVez == 1) {
								System.out.println("Digite o comando desejado: ");
								String comando = scanner.readLine();
								if(comando.contentEquals("h")) {
									mostraMenu();
								}
								if(comando.contentEquals("1")) {
									System.out.println("Voc� tem " + speculate.obtemNumBolas(id)+ " bolas");
								}
								if(comando.contentEquals("2")) {
									System.out.println("Seu oponente tem " + speculate.obtemNumBolasOponente(id) + " bolas");
								}
								if(comando.contentEquals("3")) {
									System.out.println("Este � o estado atual do tabuleiro");
									System.out.println(speculate.obtemTabuleiro(id));
								}
								if(comando.contentEquals("5")) {
									System.out.println("Encerrando a partida");
									speculate.encerraPartida(id);
								}
								if(comando.contentEquals("4")) {
									System.out.println("Digite o n�mero de jogadas: ");
									int jogadas = Integer.parseInt(scanner.readLine());
									int codigoRespostaJogadas = speculate.defineJogadas(id, jogadas);
									if(codigoRespostaJogadas == 1) {
										while(jogadas > 0) {
											System.out.println("Pressione enter para rodar o dado");
											scanner.readLine();
											int dado = speculate.jogaDado(id);
											System.out.println("Dado: " + dado);
											jogadas--;
										}
										if(speculate.obtemNumBolas(id) != 0) {
											System.out.println("Aguarde a jogada do seu oponente");
										}
										
									} else {
										mensagensErroJogada(codigoRespostaJogadas);
									}
								}
							}
							ehMinhaVez = speculate.ehMinhaVez(id);
						}
						
						if(ehMinhaVez != 1) {
							mensagensTerminoJogo(ehMinhaVez);
							System.out.println("Aperte enter para terminar o jogo");
							scanner.readLine();
							speculate.encerraPartida(id);
						}
					}
					
				}
				
			}
			
		}catch(Exception e) {
			System.out.println("Speculate client failed");
			e.printStackTrace();
		}
	}
	
	public static void mensagensTerminoJogo(int codigo) {
		if(codigo == -2) {
			System.out.println("Erro: n�o h� 2 jogadores registrados na partida");
		}
		if(codigo == -1) {
			System.out.println("Erro: jogador n�o encontrado");
		}
		if(codigo == 2) {
			System.out.println("Parab�ns voc� � o vencedor");
		}
		if(codigo == 3) {
			System.out.println("Seu oponente � o vencedor");
		}
		if(codigo == 5) {
			System.out.println("Parab�ns voc� � o vencedor por WO");
		}
		if(codigo == 6) {
			System.out.println("Seu oponente venceu por WO");
		}
	}
	
	public static void mensagensErroAoAcharPartida(int codigo) {
		if(codigo == -2) {
			System.out.println("Tempo de procura esgotado");
		} else if(codigo == -1) {
			System.out.println("Erro na partida");
		}
	}
	
	public static void mostraMenu() {
		System.out.println("1 - Minhas bolas");
		System.out.println("2 - Bolas oponente");
		System.out.println("3 - Mostra tabuleiro");
		System.out.println("4 - Inicia jogada");
		System.out.println("5 - Encerra partida");
	}
	
	public static void mensagensErroJogada(int codigo) {
		if(codigo == -1) {
			System.out.println("Erro");
		}
		if(codigo == -2) {
			System.out.println("Erro ainda n�o h� partida");
		}
		if(codigo == -3) {
			System.out.println("Erro: n�o � sua vez de jogar");
		}
		if(codigo == -4) {
			System.out.println("Erro n�o � o momento de definir o numero de jogadas");
		}
		if(codigo == -5) {
			System.out.println("Erro: n�mero de jogadas invalido");
		}
	}
}
