package es.deusto.ingenieria.threads.swing;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Ejemplo de bloqueo del hilo de gestión de eventos (EDT) de Swing
 * al ejecutar una tarea larga directamente en un ActionListener.
 * 
 * Al hacer clic en el botón la interfaz gráfica se bloquea.
 */
public class EjemploSwingBloqueo extends JFrame {

    private static final long serialVersionUID = 1L;

    public EjemploSwingBloqueo() {
        JButton button = new JButton("Haz click!!!");        

        button.addActionListener((e) -> {
        	// Tarea larga que bloquea el hilo EDT de Swing
        	for (int i = 0; i < 1000_000_000; i++) {
                System.out.println(i);
            }
        });
        
        JPanel panel = new JPanel();
        panel.add(button);
        this.add(panel);        
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(320, 240);
        this.setTitle("Bloqueo de Swing");
        this.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EjemploSwingBloqueo()); 
    }
}