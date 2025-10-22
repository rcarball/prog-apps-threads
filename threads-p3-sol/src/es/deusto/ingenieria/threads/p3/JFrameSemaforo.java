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

    private JLabel lblLuzRoja = new JLabel("", JLabel.CENTER);
    private JLabel lblLuzAmarilla = new JLabel("", JLabel.CENTER);
    private JLabel lblLuzVerde = new JLabel("", JLabel.CENTER);
    private JButton btnIniciar = new JButton("Iniciar");
    private JButton btnPausar = new JButton("Pausar");
    private JButton btnDetener = new JButton("Detener");

    private HiloSemaforo hiloSemaforo;
    private Map<Fase, JLabel> mapaLabels;
    private boolean enPausa = false;

    public enum Fase {
        VERDE(10), AMARILLO(3), ROJO(10);

        private int duracion;

        Fase(int duracion) {
            this.duracion = duracion;
        }

        public int getDuracion() {
            return duracion;
        }
    }

    public JFrameSemaforo() {
        // Se inicializa el mapa de labels del semáforo
        this.mapaLabels = new HashMap<>();
        this.mapaLabels.put(Fase.VERDE, lblLuzVerde);
        this.mapaLabels.put(Fase.AMARILLO, lblLuzAmarilla);
        this.mapaLabels.put(Fase.ROJO, lblLuzRoja);

        // Se configuran los labels
        this.configurarLabel(lblLuzRoja, Color.RED);
        this.configurarLabel(lblLuzVerde, Color.GREEN);
        this.configurarLabel(lblLuzAmarilla, Color.YELLOW);

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

    private void configurarLabel(JLabel label, Color color) {
        label.setPreferredSize(new Dimension(100, 100));
        label.setBackground(Color.BLACK);
        label.setForeground(color);
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
    	// Se usa el bloque synchronized sobre el hiloSemaforo para reactivarlo
        synchronized (hiloSemaforo) {
            enPausa = !enPausa;

            if (enPausa) {
                btnPausar.setText("Reanudar");
            } else {
                btnPausar.setText("Pausar");
                // Se notifica al hilo para que se reanude
                hiloSemaforo.notifyAll();
            }
        }
    }

    private void detener() {
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
            // Secuencia de fases del semáforo
            Fase[] fases = {Fase.VERDE, Fase.AMARILLO, Fase.ROJO};
            // Variable auxiliar para controlar la fase actual
            int indiceFases = 0;

            // Mientras el hilo no haya sido interrumpido
            while (!this.isInterrupted()) {
                // Obtener label activo a partir del color de la fase
                JLabel lblActivo = mapaLabels.get(fases[indiceFases]);
                // Obtener duracion de la fase
                int duracion = fases[indiceFases].getDuracion();

                // Se inicia la cuenta atrás
                for (int i = duracion; i > 0; i--) {
                    // Se usa el bloque synchronized por si se quiere pausar el hilo
                    synchronized (this) {
                    	// Mientras el hilo esté en pausa se espera
                    	while (enPausa) {
                            try {
                            	// Se espera a que se reanude el hilo
                                this.wait();
                            } catch (InterruptedException e) {
                                // Si se intenta interrumpir durante la pausa, se sale del bucle
                            	this.interrupt();
                                break;
                            }
                        }
                    }

                    // Variable final para usar en la lambda
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
                indiceFases = (indiceFases + 1) % Fase.values().length;
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