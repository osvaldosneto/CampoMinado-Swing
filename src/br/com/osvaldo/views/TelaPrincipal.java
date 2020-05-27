package br.com.osvaldo.views;

import javax.swing.JFrame;

import br.com.osvaldo.modelo.Tabuleiro;

public class TelaPrincipal extends JFrame {
	
	public TelaPrincipal() {
		
		Tabuleiro tabuleiro = new Tabuleiro(16,30,50);
		
		//criando painel tabuleiro
		PainelTabuleiro painel = new PainelTabuleiro(tabuleiro);
		add(painel);
		
		//setando jframe
		setTitle("Campo Minado");
		setSize(690,438);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		
	}
	
	public static void main(String[] args) {
		new TelaPrincipal();
		
		
	}

}
