
public class Partida {
	private Jogador jogador1 = null;
	private Jogador jogador2 = null;
	public Partida() {
		
	}
	
	public void setJogador1(Jogador jogador) {
		this.jogador1 = jogador;
	}
	
	public void setJogador2(Jogador jogador) {
		this.jogador2 = jogador;
	}
	
	public Jogador getJogador1() {
		return this.jogador1;
	}
	
	public Jogador getJogador2() {
		return this.jogador2;
	}
}
