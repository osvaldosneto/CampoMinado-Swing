package br.com.osvaldo.modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {
	
	private boolean minado;
	private boolean aberto;
	private boolean marcado;
	
	private final int linha;
	private final int coluna;
	
	private List<Campo> vizinhos = new ArrayList<>(); //lista de vizinhos relacionados
	private List<CampoObservador> observadores = new ArrayList<>(); //lista de observadores
	
	public Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
		this.minado = false;
		this.aberto = false;
		this.marcado = false;
	}
	
	public void registrarObservador(CampoObservador observador) {
		observadores.add(observador); //adicionando lista de observadores do evento
	}
	
	public void notificarObservadores(CampoEvento evento) {
		observadores.stream()
			.forEach(o -> o.eventoOcorreu(this, evento));
	}
	
	boolean adicionarVizinho(Campo vizinho) {
		int deltaLinha = Math.abs(vizinho.linha - linha);
		int deltaColuna = Math.abs(vizinho.coluna - coluna);
		int soma = deltaColuna + deltaLinha;
		
		if(linha == vizinho.linha || coluna == vizinho.coluna) {
			if(soma == 1) {
				vizinhos.add(vizinho);
				return true;
			}
		} else {
			if(soma == 2) {
				vizinhos.add(vizinho);
				return true;
			} 
		} 
		return false;
	}
	
	public void alternarMarcacao() {
		if(!aberto) {
			marcado = !marcado;		
			if(marcado) {
				notificarObservadores(CampoEvento.MARCAR);
			} else {
				notificarObservadores(CampoEvento.DESMARCAR);
			}
		}
	}
	
	public boolean abrir() {	
		if(!aberto && !marcado) {
			aberto = true;
			if(minado) {
				notificarObservadores(CampoEvento.EXPLODIR);
				return true;
			}
			notificarObservadores(CampoEvento.ABRIR);
			if(vizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrir()); //chamada recursiva
			}
			return true;
		} else {
			return false;
		}
	}
	
	public boolean vizinhancaSegura() {
		boolean vizinhoSeguro = vizinhos.stream().noneMatch(v -> v.minado);
		return vizinhoSeguro;
	}
	
	public boolean isMarcado() {
		return marcado;
	}
	
	public boolean isAberto() {
		return aberto;
	}
	
	public boolean isMinado() {
		return minado;
	}
	
	public void setMinado(boolean b) {
		this.minado = b;
	}
	
	public void setMarcado(boolean b) {
		this.marcado = b;
	}
	
	public void setAberto(boolean b) {
		this.aberto = b;
		if(aberto) {
			notificarObservadores(CampoEvento.ABRIR);
		}
	}
	
	public int getLinha() {
		return this.linha;
	}
	
	public int getColuna() {
		return this.coluna;
	}
	
	boolean objetivoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		return desvendado || protegido;
	}
	
	public long minasNaVizinhanca() {
		return vizinhos.stream().filter(v -> v.minado).count();
	}
	
	void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
		notificarObservadores(CampoEvento.REINICIAR);
	}
	
}
