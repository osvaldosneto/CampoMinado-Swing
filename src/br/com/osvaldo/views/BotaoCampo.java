package br.com.osvaldo.views;

import java.awt.Color;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import org.w3c.dom.events.MouseEvent;

import br.com.osvaldo.modelo.Campo;
import br.com.osvaldo.modelo.CampoEvento;
import br.com.osvaldo.modelo.CampoObservador;

public class BotaoCampo extends JButton implements CampoObservador, MouseListener{
	
	private Campo campo;
	
	//definindo cores
	private final Color BG_PADRAO = new Color(184,184,184); //cinza
	private final Color BG_MARCADO = new Color(8,179,247); 
	private final Color BG_EXPLOSAO = new Color(189,66,68); 
	private final Color TEXTO_VERDE = new Color(0,100,0); 
	
	public BotaoCampo(Campo campo) {
		this.campo = campo;
		//setando layout botao
		setBackground(BG_PADRAO);
		setOpaque(true);
		setBorder(BorderFactory.createBevelBorder(0));
		addMouseListener(this);
		campo.registrarObservador(this);
	}

	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		// TODO Auto-generated method stub
		switch(evento) {
		case ABRIR:
			aplicarEstiloAbrir();
			break;
		case MARCAR:
			aplicarEstiloMarcar();
			break;
		case EXPLODIR:
			aplicarEstiloExplodir();
			break;
		case REINICIAR:
			aplicarEstiloPadrao();
			break;
		default:
			aplicarEstiloPadrao();
			break;
		}
	}

	private void aplicarEstiloPadrao() {
		setBackground(BG_PADRAO);
		setBorder(BorderFactory.createBevelBorder(0));
		setText("");
	}

	private void aplicarEstiloExplodir() {
		setBackground(BG_EXPLOSAO);
		setForeground(Color.WHITE);
		setText("X");
	}

	private void aplicarEstiloMarcar() {
		setBackground(BG_MARCADO);
		setForeground(Color.BLACK);
		setText("M");
	}

	private void aplicarEstiloAbrir() {
		
		if(campo.isMinado()) {
			setBackground(BG_EXPLOSAO);
			return;
		}
		
		setBackground(BG_PADRAO);
		setBorder(BorderFactory.createLineBorder(Color.gray));
		
		switch((int)campo.minasNaVizinhanca()) {
			case 1:
				setForeground(TEXTO_VERDE);
				break;
			case 2:
				setForeground(Color.BLUE);
				break;
			case 3:
				setForeground(Color.YELLOW);
				break;
			case 4:
			case 5:
			case 6:
				setForeground(Color.RED);
				break;
			default:
				setForeground(Color.PINK);
		}	
		String valor = !campo.vizinhancaSegura() ? campo.minasNaVizinhanca() + "" : "";
		setText(valor);
	}
	
	//interface mouse event
	//mouse event
	public void mousePressed(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton() == 1) {
			campo.abrir();
			//System.out.println("Botao esquerdo");
		} else {
			campo.alternarMarcacao();
			//System.out.println("Botao Direito");
		}
	}

	public void mouseClicked(java.awt.event.MouseEvent e) {}
	public void mouseReleased(java.awt.event.MouseEvent e) {}
	public void mouseEntered(java.awt.event.MouseEvent e) {}
	public void mouseExited(java.awt.event.MouseEvent e) {}
}
