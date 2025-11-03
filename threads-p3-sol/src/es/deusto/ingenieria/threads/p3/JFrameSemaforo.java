package es.deusto.ingenieria.threads.p3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class JFrameSemaforo extends JFrame {
    private static final long serialVersionUID = 1L;

    // Componentes de la interfaz gráfica: labels y botones
    private JLabel lblLuzRoja = new JLabel("", JLabel.CENTER);
    private JLabel lblLuzAmarilla = new JLabel("", JLabel.CENTER);
    private JLabel lblLuzVerde = new JLabel("", JLabel.CENTER);
    private JButton btnIniciar = new JButton("Iniciar");
    private JButton btnPausar = new JButton("Pausar");
    private JButton btnDetener = new JButton("Detener");

    // Hilo que gestiona el funcionamiento del semáforo
    private HiloSemaforo hiloSemaforo;
    // Mapa para asociar las fases del semáforo a los labels
    private Map<Fase, JLabel> mapaLabels;
    // Variable para controlar si el semáforo está en pausa o no
    private boolean enPausa = false;

    // Enumeración para adefinir las fases del semáforo
    // con su duración y color asociados
    public enum Fase {
        VERDE(10, Color.GREEN), 
        AMARILLO(3, Color.YELLOW), 
        ROJO(10, Color.RED);

        private int duracion;
        private Color color;

        Fase(int duracion, Color color) {
            this.duracion = duracion;
            this.color = color;
        }

        public int getDuracion() {
            return duracion;
        }
        
        public Color getColor() {
			return color;
		}
    }

    public JFrameSemaforo() {
        // Se inicializa el mapa de fases y labels
        this.mapaLabels = new HashMap<>();
        this.mapaLabels.put(Fase.VERDE, lblLuzVerde);
        this.mapaLabels.put(Fase.AMARILLO, lblLuzAmarilla);
        this.mapaLabels.put(Fase.ROJO, lblLuzRoja);

        // Se configuran los labels
        this.configurarLabel(lblLuzRoja, Fase.ROJO);
        this.configurarLabel(lblLuzVerde, Fase.VERDE);
        this.configurarLabel(lblLuzAmarilla, Fase.AMARILLO);

        // Se definen los listeners de los botones
        btnIniciar.addActionListener(e -> iniciar());
        btnPausar.addActionListener(e -> pausar());
        btnDetener.addActionListener(e -> detener());

        // Se establece la configuración inicial de los botones
        this.resetearBotones();

        JPanel panelLuces = new JPanel();
        panelLuces.setLayout(new GridLayout(3, 1, 0, 10));
        panelLuces.add(lblLuzRoja);
        panelLuces.add(lblLuzAmarilla);
        panelLuces.add(lblLuzVerde);

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnIniciar);
        panelBotones.add(btnPausar);
        panelBotones.add(btnDetener);

        add(panelLuces, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setTitle("Simulador de Semáforo");
        setSize(300, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void configurarLabel(JLabel label, Fase fase) {
        label.setPreferredSize(new Dimension(100, 100));
        label.setBackground(Color.BLACK);
        label.setForeground(fase.getColor());
        label.setFont(new Font("Courier", Font.BOLD, 72));
        label.setOpaque(true);
    }

    private void resetearBotones() {
        btnIniciar.setEnabled(true);
        btnPausar.setEnabled(false);
        btnDetener.setEnabled(false);
        btnPausar.setText("Pausar");
    }

    private void iniciar() {
        btnIniciar.setEnabled(false);
        btnPausar.setEnabled(true);
        btnDetener.setEnabled(true);
        enPausa = false;
        
    	// Se crea el hilo que gestiona el semáforo y se inicia
        hiloSemaforo = new HiloSemaforo();
        hiloSemaforo.start();
    }

    private void pausar() {
    	// Se usa el bloque synchronized para asegurar la exclusión mutua
    	// al modificar la variable enPausa y notificar el hilo, si es necesario
        synchronized (hiloSemaforo) {
            enPausa = !enPausa;

            if (enPausa) {
                btnPausar.setText("Reanudar");
            } else {
                btnPausar.setText("Pausar");
                // Se notifica al hilo para que se reanude
                hiloSemaforo.notify();
            }
        }
    }

    private void detener() {
    	// Se comprueba que el hilo no sea nulo: ha sido iniciado
        if (hiloSemaforo != null) {
        	// Se interrumpe el hilo
            hiloSemaforo.interrupt();

            try {
                // Se espera a que el hilo termine
                hiloSemaforo.join();
            } catch (InterruptedException e) {
            	// Si se produce una excepcion, se interrumpe el hilo
            	hiloSemaforo.interrupt();
            } finally {
                resetearBotones();
            }
        }
    }

    private class HiloSemaforo extends Thread {
        @Override
        public void run() {
            // Array de fases para iterar sobre el de manera continua
            Fase[] fases = {Fase.VERDE, Fase.AMARILLO, Fase.ROJO};
            // Variable auxiliar para controlar la fase actual
            int indiceFases = 0;

            // Mientras el hilo no haya sido interrumpido
            while (!this.isInterrupted()) {
                // Obtener label activo a partir del la fase actual
                JLabel lblActivo = mapaLabels.get(fases[indiceFases]);
                // Obtener duracion de la fase
                int duracion = fases[indiceFases].getDuracion();

                // Se inicia la cuenta atrás
                for (int i = duracion; i > 0; i--) {
                    // Se usa el bloque synchronized para pausar el hilo
                    synchronized (this) {
                    	// Si el semáforo está en pausa, se espera
                    	// Aquí de puede usar tanto un if como un while
                    	if (enPausa) {
                            try {
                            	// Se espera a que se reanude el hilo
                                this.wait();
                            } catch (InterruptedException e) {
                                // si se produce una excepcion, se interrumpe el hilo                            	
                            	this.interrupt();
                            	// Se sale del bucle que cuenta los segundos
                                break;
                            }
                        }
                    }

                    // Variable final para usar en la expresión lambda
                    // es necesario porque no se puede utilizar variables
                    // que cambien de valor dentro o tras pasar por una lambda
                    // (como es el caso de i en este bucle)
                    final int tiempoRestante = i;

                    // Se actualiza el Label activo con el tiempo restante
                    SwingUtilities.invokeLater(() -> {
                        lblActivo.setText(String.valueOf(tiempoRestante));
                    });

                    try {
                        // Se detiene el Thread 1 seg.
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // Si se produce una excepcion, se interrumpe el hilo
                    	this.interrupt();
                    	// Se sale del bucle que cuenta los segundos
                    	break;
                    }
                }

                // Se borra el texto del label activo al terminar la cuenta atrás
                // porque se pasará a la siguiente fase
                SwingUtilities.invokeLater(() -> {
                    lblActivo.setText("");
                });

                // Se obtiene el índice de la siguiente fase
                indiceFases = (indiceFases + 1) % fases.length;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrameSemaforo semaforo = new JFrameSemaforo();
            semaforo.setVisible(true);
        });
    }
}