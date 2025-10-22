package es.deusto.ingenieria.threads;




// Este ejemplo muestra como crear un nuevo hilo de ejecución
// para realizar una tarea de forma concurrente al hilo del
// programa principal.

// Se hace uso también del método Thread.sleep en ambos hilos
// para dormir los threads.

public class ThreadBasico {

	public static void main(String[] args) {
		// Se crea un hilo que cuenta de 10 a 0
		Thread hilo1 = new Thread("Hilo-Descendente") {
			@Override
			public void run() {
				for (int i = 10; i >= 0; i--) {
					System.out.println(String.format("+ '%s' -> %d", Thread.currentThread().getName(), i));
				}
			}
		};

		Thread hilo2 = new Thread(() -> {
			for (int i = 10; i >= 0; i--) {
				System.out.println(String.format("* '%s' -> %d", Thread.currentThread().getName(), i));
			}				
		}, "Hilo-Lambda-Descendente");		
		
		// Se inicia el hilo
		hilo1.start();
		hilo2.start();

		// El hilo principal cuenta de 0 a 10
		for (int i = 0; i < 10; i++) {
			System.err.println(String.format("* 'Hilo-%s' -> %d", Thread.currentThread().getName(), i));
		}
	}
}
