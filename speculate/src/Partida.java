
public class Partida {
	private Jogador jogador1 = null;
	private Jogador jogador2 = null;
	private Jogador proximaJogada = null;
	private Jogador vencedor = null;
	private Boolean desistencia = false;
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
	
	public void setProximaJogada(Jogador proximo) {
		this.proximaJogada = proximo;
	}
	
	public Jogador getProximaJogada() {
		return this.proximaJogada;
	}
	
	public void setVencedor(Jogador vencedor) {
		this.vencedor = vencedor;
	}
	
	public Jogador getVencedor() {
		return this.vencedor;
	}
	
	public void setDesistencia(Boolean desistencia) {
		this.desistencia = desistencia;
	}
	
	public Boolean getDesistencia() {
		return this.desistencia;
	}
}
