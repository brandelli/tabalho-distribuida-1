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
	private static Map<Integer, Partida> partidasRegistradas = new Hashtable<Integer, Partida>(maxJogos);
	private static Jogador jogadorEmEspera = null;
	private static Semaphore semaforo = new Semaphore(1);
	
	protected SpeculateImpl() throws RemoteException {
		
	}
	
	private Partida getPartida(int id) {
		return dictPartidas.get(id);
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
			// caso o maximo de partidas suportadas tenha sido alcançado
			}else if(partidasRegistradas.size() == maxJogos) {
				return -3;
			//caso esteja tudo OK para cadastrar o jogador
			} else {
				int id = this.getPID();
				Jogador jogador = new Jogador(id, nome);
				//caso tenha algum jogador esperando partida
				if(jogadorEmEspera != null) {
					Partida partida = dictPartidas.get(jogadorEmEspera.getId());
					partida.setJogador1(jogadorEmEspera);
					partida.setJogador2(jogador);
					partida.criaTabuleiro();
					partida.setProximaJogada(jogadorEmEspera);
					dictPartidas.put(id, partida);
					jogadoresRegistrados.put(nome, jogador);
					System.out.println("Partida criada entre "+ jogadorEmEspera.getNome() +" e " + nome);
					jogadorEmEspera = null;
				}else {
					jogadorEmEspera = jogador;
					jogadoresRegistrados.put(nome, jogador);
					dictPartidas.put(id, new Partida(this.getPID()));
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
		int codigoDeRetorno = -1;
		try {
			semaforo.acquire();
			Partida partida = this.getPartida(id);
			// existe a partida deste jogador
			if(partida != null) {
				// partida está sendo encerrada antes do termino do jogo
				if(partida.getVencedor() == null) {
					partida.setDesistencia(true);
				// partida esta sendo encerrada depois do vencedor ser definido
				} else {
					// caso um jogador já tenha encerrado a sua sessão, os requicios da partida são limpos
					if(partida.getPartidaTerminada()) {
						partidasRegistradas.remove(partida.getId());
						System.out.println("Partida entre " + partida.getJogador1().getNome() + " e " + partida.getJogador2().getNome() + " finalizada");
					// caso contrario, o primeiro cliente a fechar a partida indica na classe partida
					} else {
						partida.terminarPartida();
					}
					// a ligação entre jogador e partida é removida da estrutura
					dictPartidas.remove(id);
					String nome = "";
					if(partida.getJogador1().getId() == id) {
						nome = partida.getJogador1().getNome();
					} else {
						nome = partida.getJogador2().getNome();
					}
					// o registro do jogador é removido da estrutura
					jogadoresRegistrados.remove(nome);
				}
			}
		} catch (Exception e) {
			System.out.println("Erro em encerraPartida");
			e.printStackTrace();
		} finally {
			semaforo.release();
		}
		return codigoDeRetorno;
	}

	@Override
	public int temPartida(int id) throws RemoteException {
		Partida partida = this.getPartida(id);
		if(partida == null) {
			return -1;
		}
		
		Jogador jogador1 = partida.getJogador1();
		Jogador jogador2 = partida.getJogador2();
		
		if(jogador2 == null) {
			return 0;
		}
		
		if(jogador1.getId() == id) {
			return 1;
		}
		
		if(jogador2.getId() == id) {
			return 2;
		}
		
		return -2;
		
	}

	@Override
	public String obtemOponente(int id) throws RemoteException {
		Partida partida = this.getPartida(id);
		if(partida != null) {
			Jogador jogador1 = partida.getJogador1();
			Jogador jogador2 = partida.getJogador2();
			
			if(jogador1.getId() == id) {
				if(jogador2 != null) {
					return jogador2.getNome();
				}
			} else {
				return jogador1.getNome();
			}
		}
		return "";
	}

	@Override
	public int ehMinhaVez(int id) throws RemoteException {
		Partida partida = this.getPartida(id);
		
		// jogador não encontrado em nunhuma partida
		if(partida == null) {
			return -1;
		}
		
		// caso não tenha 2 jogadores na partida
		if(partida.getJogador2() == null) {
			return -2;
		}
		
		// caso ainda não tenha vencedor na partida
		if(partida.getVencedor() == null) {
			// proxima jogada é do cliente que fez a chamada
			if(partida.getProximaJogada().getId() == id) {
				return 1;
			}
			// proxima jogada é do oponente;
			return 0;
		// já está definido o vencedor
		} else {
			Jogador vencedor = partida.getVencedor();
			// caso o jogo tenha acabado devido a uma desistencia
			if(partida.getDesistencia() == true) {
				// vencedor por WO é o cliente que fez a chamada
				if(vencedor.getId() == id) {
					return 5;
				}
				// perdedor por WO é o cliente qu efez a chamada
				return 6;
			} else {
				// vencedor é o cliente que fez a chamada
				if(vencedor.getId() == id) {
					return 2;
				}
				// perdedor é o cliente que fez a chamada
				return 3;
			}
		}
	}
	
	private int obtemBolasDoJogador(int id, Boolean minhasBolas) {
		Partida partida = this.getPartida(id);
		// caso o jogador não esteja em nenhuma partida
		if(partida == null) {
			return -1;
		}
		
		Jogador jogador1 = partida.getJogador1();
		Jogador jogador2 = partida.getJogador2();
		
		// caso não tenha um segundo jogador cadastrado na partida
		if(jogador2 == null) {
			return -2;
		}
		
		// caso esteja querendo saber quantas bolas eu tenho
		if(minhasBolas) {
			if(jogador1.getId() == id) {
				return jogador1.getBolas();
			}
			return jogador2.getBolas();
		// caso esteja querendo saber quantas bolas meu oponente tem
		} else {
			if(jogador1.getId() == id) {
				return jogador2.getBolas();
			}
			return jogador1.getBolas();
		}
		
	}

	@Override
	public int obtemNumBolas(int id) throws RemoteException {
		return this.obtemBolasDoJogador(id, true);
	}

	@Override
	public int obtemNumBolasOponente(int id) throws RemoteException {
		return this.obtemBolasDoJogador(id, false);
	}

	@Override
	public String obtemTabuleiro(int id) throws RemoteException {
		Partida partida = this.getPartida(id);
		if(partida == null || partida.getTabuleiro() == null)
			return "";
		
		return partida.getTabuleiro().obtemEstadoDoTabuleiro();
	}

	@Override
	public int defineJogadas(int id, int jogadas) throws RemoteException {
		Partida partida = this.getPartida(id);
		
		//caso não tenha partida
		if(partida == null) {
			return -2;
		}
		
		//erro, acredito que seja não ter o jogador 2
		if(partida.getJogador2() == null) {
			return -1;
		}
		
		// caso não seja a vez do jogaodr
		if(partida.getProximaJogada().getId() != id) {
			return -3;
		}
		
		// caso seja a vez do jogador
		if(partida.getProximaJogada().getId() == id) {
			// é a vez do jogador mas o jogo já foi encerrado
			if(partida.getVencedor() != null) {
				return -4;
			}
			
			// jogada invalida
			if(jogadas < 1 || jogadas > partida.getProximaJogada().getBolas()) {
				return -5;
			}
			
			partida.setJogadas(jogadas);
			
			// jogada valida
			return 1;
		}
		
		
		
		
		return -1;
	}

	@Override
	public int jogaDado(int id) throws RemoteException {
		Partida partida = this.getPartida(id);
		
		// caso ainda não tenha partida
		if(partida == null) {
			return -2;
		}
		
		//erro, acredito que seja não ter o jogador 2
		if(partida.getJogador2() == null) {
			return -1;
		}
		
		// caso não seja a vez do jogaodr
		if(partida.getProximaJogada().getId() != id) {
			return -3;
		}
		
		if(partida.getProximaJogada().getId() == id) {
			// é a vez do jogador mas o jogo já foi encerrado
			if(partida.getVencedor() != null) {
				return -4;
			}
			
			return partida.jogaDados();
		}
		
		
		return -1;
	}
	
}
