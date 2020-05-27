package br.com.osvaldo.views;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.osvaldo.modelo.Tabuleiro;

public class PainelTabuleiro extends JPanel{
	
	public PainelTabuleiro(Tabuleiro tabuleiro) {
		
		//setando layout
		setLayout(new GridLayout(tabuleiro.linhas, tabuleiro.colunas));
		
		tabuleiro.paraCada(c->add(new BotaoCampo(c)));
		
		tabuleiro.registraObservador(e -> {
			//mostrar resultado
			SwingUtilities.invokeLater(() ->{
				if(e.TRUE) {
					JOptionPane.showMessageDialog(this, "Ganhou :)");
				} else {
					JOptionPane.showMessageDialog(this, "Perdeu :(");
				}
				tabuleiro.reiniciar();
			});
			
			
		});
		
		//for(int i=0; i<(tabuleiro.linhas*tabuleiro.colunas); i++) {
		//	add(new BotaoCampo(null));
		//}
		
	}

}
