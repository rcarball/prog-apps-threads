package es.deusto.ingenieria.threads;

public class ThreadInterrupt1 {

	public static void main(String[] args) {
		// Se crea un hilo que cuenta de 10 a 0
		Thread hiloDescendente = new Thread(() ->{
			for (int i = 10; i >= 0; i--) {				
				if (Thread.currentThread().isInterrupted()) {
					System.err.println(String.format("- '%s' -> interrumpido!", Thread.currentThread().getName()));
					break;
				}
				
				System.err.println(String.format("- '%s' -> %d", Thread.currentThread().getName(), i));

				try {
					// Se duerme el hilo durante 0,5 seg.
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// Se caprtura la excepción y se muestra un mensaje
					System.err.println(String.format("- '%s' -> Excepcion: %s", Thread.currentThread().getName(), e.getMessage()));
					// Se intenta deterner el hilo actual
					Thread.currentThread().interrupt();
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
		}
		
		// Se le pide al hilo descendente que se detenga
        System.out.println(String.format("* Pidiendo parar a '%s' desde el 'Hilo-%s'", hiloDescendente.getName(), Thread.currentThread().getName()));
        hiloDescendente.interrupt();

		// Se muestra un mensaje al finalizar el hilo principal
		System.out.println(String.format("* 'Hilo-%s' -> finalizado!", Thread.currentThread().getName()));
	}
}
