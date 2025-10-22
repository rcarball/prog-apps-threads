package es.deusto.ingenieria.threads.p2;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ThreadProgressBar extends JFrame {

    private static final long serialVersionUID = 1L;
    
    // Valor máximo a contar
    private static final long MAX_VALUE = 10_000_000;
    // Botones de Iniciar y Parar
    private JButton btnIniciar = new JButton("Iniciar");
    private JButton btnParar = new JButton("Parar");
    // Progress Bar
    private JProgressBar progressBar = new JProgressBar(0, 100);
    
    // Clase que implementa el hilo contador
    private Contador contador;
    
    public ThreadProgressBar() {        
    	// Configuración del estado inicial de los botones
    	btnIniciar.setEnabled(true);
    	btnParar.setEnabled(false);
    	
    	// Visualización del % en la Progress Bar
    	progressBar.setStringPainted(true);    	
                
    	btnIniciar.addActionListener((e) -> {
    		// Cambiar el estado de los botones
    		btnIniciar.setEnabled(false);
    		btnParar.setEnabled(true);
    		
    		contador = new Contador();
    		contador.start();
        });

    	btnParar.addActionListener((e) -> {
    		// Cambiar el estado de los botones
    		btnIniciar.setEnabled(true);
    		btnParar.setEnabled(false);
    		
    		// Interrumpir el contador
    		contador.interrupt();
        });
    	
        // Layout de los botones en un panel
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnIniciar);
        panelBotones.add(btnParar);
        
        this.setLayout(new BorderLayout());
        this.add(panelBotones, BorderLayout.CENTER);
        this.add(progressBar, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 180);
        this.setTitle("Swing + Hilo externo");
        this.setLocationRelativeTo(null);
        
        setVisible(true);
    }
    
    private class Contador extends Thread {
    	@Override
    	public void run() {
    		int progreso;
    		
    		for (int i=0; i <= MAX_VALUE; i++) {
    			// Comprobar si hay que parar el hilo
				if (Thread.currentThread().isInterrupted()) {
					updateProgressBar(100);
					break;
				}
    			
    			// Valor de progreso
    			progreso = (int) ((i * 100) / MAX_VALUE);
    			// Imprimir en consola
    			System.out.println(String.format("- 'Hilo-%s' -> %d (%d%%)", Thread.currentThread().getName(), i, progreso));    			
    			// Actualizar la Progress Bar
    			updateProgressBar(progreso);
    		}
    	}
    }

    // Actualización de la Progress Bar usando SwingUtilities
    private void updateProgressBar(final int value) {
        SwingUtilities.invokeLater(() -> progressBar.setValue(value));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ThreadProgressBar()); 
    }
}