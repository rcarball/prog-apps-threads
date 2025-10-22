package es.deusto.ingenieria.threads.swing;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


/**
 * Ejemplo que bloquea del hilo de gestión de eventos (EDT) de Swing
 * al ejecutar una tarea larga en un hilo separado.
 * 
 * Al hacer clic en el botón la interfaz gráfica permanece
 * activa y el JLabel se actualiza periódicamente.
 */
public class EjemploSwingNoBloqueo extends JFrame {

    private static final long serialVersionUID = 1L;
    private JLabel label = new JLabel();
    
    public EjemploSwingNoBloqueo() {
        JButton button = new JButton("Haz click!!!");        

        button.addActionListener((e) -> {
            // Crear un nuevo hilo para realizar la tarea larga
        	Thread t = new Thread(() -> {
        		// Tarea larga que no bloquea el hilo EDT de Swing
            	for (int i = 0; i < 1000_000_000; i++) {                            
                	try {
                		// Pausa para ver el cambio en el JLabel
						Thread.sleep(1); 
					} catch (InterruptedException ex) {
						// no hacer nada
					}
                	
                	updateLabel(i);
            	}
            });

            t.start(); // lanzar el hilo
		});
        
        JPanel panel = new JPanel();
        panel.add(button);
        panel.add(label);
        this.add(panel);        
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(320, 240);
        this.setTitle("Ejemplo NO bloqueo de Swing");
        this.setVisible(true);
    }

    // Método para actualizar el JLabel de forma segura
    private void updateLabel(final int value) {
    	// Actualizar el JLabel en el hilo EDT de Swing cuando sea posible
    	SwingUtilities.invokeLater(() ->  label.setText(String.valueOf(value)));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EjemploSwingNoBloqueo());
    }
}