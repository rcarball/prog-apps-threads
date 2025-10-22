/**
 * Ejemplo generado con Claude Sonet 4.5 enfocado a la comprensión
 * de los conceptos de pausa y reanudación de hilos usando wait() y notify()  
 */
package es.deusto.ingenieria.threads;

/**
 * Hilo que realiza una cuenta atrás desde un número dado de segundos,
 * con la capacidad de pausar y reanudar la cuenta.
 */
public class ThreadWaitNotify extends Thread {
	private int segundos;
	private boolean pausado = false;

	public ThreadWaitNotify(int segundos) {
		this.segundos = segundos;
	}
	
	@Override
	public void run() {
		for (int i = segundos; i >= 0; i--) {
			synchronized (this) {
				// Mientras esté pausado, esperar
				while (pausado) {
					try {
						System.out.println("⏸️  Cuenta en pausa...");
						wait(); // Libera el monitor y espera
						System.out.println("▶️  Reanudando cuenta...");
					} catch (InterruptedException e) {
						return;
					}
				}
			}

			// Fuera del bloque synchronized: mostrar y esperar
			System.out.println("Segundos restantes: " + i);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return;
			}
		}
		System.out.println("✅ Cuenta atrás finalizada!");
	}

	public synchronized void pausar() {
		pausado = true;
		// No hace falta notify sólo cambiamos el estado
	}

	public synchronized void reanudar() {
		pausado = false;
		notify(); // Despierta al hilo y se continúa desde wait()
	}

	public static void main(String[] args) {
		ThreadWaitNotify cuenta = new ThreadWaitNotify(10);
		cuenta.start();

		try {
			Thread.sleep(3000); // Dejar contar 3 segundos
			cuenta.pausar();    // PAUSAR

			Thread.sleep(3000); // Esperar 3 segundos en pausa
			cuenta.reanudar();  // REANUDAR

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}