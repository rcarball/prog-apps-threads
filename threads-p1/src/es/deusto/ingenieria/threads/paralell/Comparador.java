package es.deusto.ingenieria.threads.paralell;

public class Comparador {

	public static void main(String[] args) {
		// Ejecución secuencial
		Secuencial.ejecutar();
		// Ejecución concurrente con varios hilos y join()
		ParalelizacionBasica.ejecutar();
		// Ejecución concurrente con varios hilos y CountDownLatch
		ParalelizacionCountDownLatch.ejecutar();
		// Ejecución concurrente con ExecutorService (pool de hilos fino)
		ParalelizacionExecutorService.ejecutar();
		// Ejecución concurrente con ExecutorService (pool de hilos fijo) y Future
		ParalelizacionExecutorServiceFuture.ejecutar();
	}
}