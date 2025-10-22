package es.deusto.ingenieria.threads;

// En este ejemplo se muestra como se puede esperar a que
// termine la ejecución de otro hilo antes de continuar.

public class ThreadJoin {

    public static void main(String[] args) {      
		// Se crea un hilo que cuenta de 10 a 0
		Thread hiloDescendente = new Thread(() -> {
			for (int i = 10; i >= 0; i--) {
				System.err.println(String.format("- '%s' -> %d", Thread.currentThread().getName(), i));

				try {
					// Se duerme el hilo durante 0.5 seg.
					Thread.sleep(500);
				} catch (InterruptedException e) {
					//
				}
			}

			// Se muestra un mensaje al finalizar el run()
			System.err.println(String.format("- '%s' -> finalizado!", Thread.currentThread().getName()));
		}, "Hilo-Descendente");

		// Se inicia el hilo
		hiloDescendente.start();

		// El hilo principal cuenta de 0 a 10
		for (int i = 0; i < 10; i++) {
			System.out.println(String.format("* 'Hilo-%s' -> %d", Thread.currentThread().getName(), i));
			try {
				// Se duerme el hilo durante 0.5 seg.
				Thread.sleep(500);
			} catch (InterruptedException e) {
				//
			}
		}
		
		try {
			System.out.println(String.format("* 'Hilo-%s' -> esperando a que finalice '%s'", Thread.currentThread().getName(), hiloDescendente.getName()));
			// Se espera a que termine el hilo
			hiloDescendente.join();			
		} catch (InterruptedException e) {
		}

		// Se muestra un mensaje al finalizar el hilo principal
		System.out.println(String.format("* 'Hilo-%s' -> finalizado!", Thread.currentThread().getName()));
    }
}