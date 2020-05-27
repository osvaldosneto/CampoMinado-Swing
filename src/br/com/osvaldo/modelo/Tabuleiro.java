package br.com.osvaldo.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador{
	
	public int linhas;
	public int colunas;
	public int minas;
	
	public List<Campo> campos = new ArrayList<>();
	
	//criando observador tabuleiro
	private final List<Consumer<Boolean>> observadores = new ArrayList<>();
	
	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		this.gerarCampos();
		this.associarVizinhos();
		this.sortearMinas();	
	}
	
	public void registraObservador(Consumer<Boolean> observador) {
		observadores.add(observador);
	}
	
	public void notificarObservadores(boolean resultado) {
		observadores.stream()
			.forEach(o -> o.accept(resultado));
	}
	
	public void abrir(int linha, int coluna) {
		try {
			campos.stream().filter(c -> c.getLinha()==linha && c.getColuna()==coluna)
				.findFirst()
				.ifPresent(c -> c.abrir());
		} catch (Exception e){
			//ajustar
			campos.forEach(c -> c.setAberto(true));
			throw e;
		}
	}
	
	public void alternarMarcacao(int linha, int coluna) {
		campos.stream().filter(c -> c.getLinha()==linha && c.getColuna()==coluna)
		.findFirst()
		.ifPresent(c -> c.alternarMarcacao());
	}
	
	public void gerarCampos() {
		for(int i=0; i<linhas; i++) {
			for(int j=0; j<colunas; j++) {
				Campo campo = new Campo(i,j);
				campo.registrarObservador(this);
				campos.add(campo);
			}
		}
	}
	
	public void paraCada(Consumer<Campo> funcao) {
		campos.forEach(funcao);
	}
	
	public void associarVizinhos() {
		for(Campo c1 : campos) {
			for(Campo c2 : campos){
				c1.adicionarVizinho(c2);
			}
		}	
	}
	
	public void sortearMinas() {
		int minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		
		while( minasArmadas <= this.minas) {
			//gerando numero aleatório n-1 tamanho da lista
			int n = (int) (Math.random() * campos.size());
			//minando o campo
			campos.get(n).setMinado(true);
			//capturando numero de minas
			minasArmadas = 1 + (int)(campos.stream().filter(minado).count());
		}
	}
	
	public boolean objetivoAlcancado() {
		Predicate<Campo> campo = c -> c.objetivoAlcancado();
		return campos.stream().allMatch(campo);
	}
	
	public void reiniciar() {
		campos.stream().forEach(c -> c.reiniciar());
		this.sortearMinas();
	}

	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		if(evento == CampoEvento.EXPLODIR) {
			this.mostrarMinas();
			System.out.println("Perdeu");
			notificarObservadores(false);
		} else if (objetivoAlcancado()) {
			System.out.println("Ganhou");
			notificarObservadores(true);
		}
	}
	
	private void mostrarMinas() {
		campos.stream()
			.filter(c -> c.isMinado())
			.filter(c -> !c.isMarcado())
			.forEach(c -> c.setAberto(true));
	}
}
