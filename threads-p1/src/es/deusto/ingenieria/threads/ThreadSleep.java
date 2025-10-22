package es.deusto.ingenieria.threads;

public class ThreadSleep {

	public static void main(String[] args) {
		// Se crea un hilo que cuenta de 10 a 0
		Thread hilo = new Thread(() ->{
			for (int i = 10; i >= 0; i--) {
				System.err.println(String.format("- Hilo '%s' -> %d", Thread.currentThread().getName(), i));

				try {
					// Se duerme el hilo durante 0.5 seg.
					Thread.sleep(500);
				} catch (InterruptedException e) {
					//
				}
			}

			// Se muestra un mensaje al finalizar el run()
			System.err.println(String.format("- Hilo '%s' -> finalizado!", Thread.currentThread().getName()));
		});

		// Se inicia el hilo
		hilo.start();
		
		
		// El hilo principal cuenta de 0 a 10
		for (int i = 0; i < 10; i++) {
			System.out.println(String.format("* Hilo '%s' -> %d", Thread.currentThread().getName(), i));			
		}

		try {
			hilo.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// Se muestra un mensaje al finalizar el hilo principal
		System.out.println(String.format("* Hilo '%s' -> finalizado!", Thread.currentThread().getName()));
	}
}
