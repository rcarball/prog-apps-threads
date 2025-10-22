package es.deusto.ingenieria.threads.paralell;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Queremos hacer una tarea compleja: sumar todos los números entre 0 y 10 millones.
 * 
 * En este ejemplo, se muestra el uso de un pool de hilos para garantizar que
 * como máximo hay el número máximo de hilos en paralelo. La tarea se descompone
 * en varios hilos que se ejecutan y suman los resultados parciales entre dos
 * límites. Cuando todos los hilos han terminado se suman estos resultados
 * parciales para obtener el resultado final.
 */
public class ParalelizacionCountDownLatch {
	// Clase interna que implementa la tarea de sumar valores
	// entre dos límites
    private static class TareaSumar implements Runnable {
        private int inicio;
        private int fin;
        private long resultado;
        private CountDownLatch latch;

        public TareaSumar(int inicio, int fin, CountDownLatch latch) {
            this.inicio = inicio;
            this.fin = fin;
            this.latch = latch;
        }

        public long getResultado() {
            return resultado;
        }

        @Override
        public void run() {
            resultado = 0;
            
            for (int i = inicio; i <= fin; i++) {
                resultado += i;
            }
            
            // Indicamos que esta tarea ha terminado
            latch.countDown();
        }
    }
	
	// el número máximo hasta el que sumar desde cero
	private static final int MAX_NUM = 1000_000_000;
	// el número de tareas que se van a crear para procesar la suma
	private static final int NUM_TAREAS = 20;

	public static void ejecutar() {
		// Instante de tiempo inicial
		long instanteInicial = System.currentTimeMillis();

		// Se usa un CountDownLatch para coordinar el fin de todos los hilos
		CountDownLatch latch = new CountDownLatch(NUM_TAREAS);
		
		// creamos un array de tareas
		TareaSumar[] tareas = new TareaSumar[NUM_TAREAS];
		
		int rango = MAX_NUM / NUM_TAREAS;
		
		// creamos las tareas, los hilos y los lanzamos
		for (int i = 0; i < NUM_TAREAS; i++) {
            int inicio = i * rango + 1;
            int fin = (i == NUM_TAREAS - 1) ? MAX_NUM : ((i + 1) * rango);

			tareas[i] = new TareaSumar(inicio, fin, latch);

			// Se lanza un hilo para cada tarea
			new Thread(tareas[i]).start();			
		}

		// esperamos a que todos los hilos terminen
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// sumamos los resultados parciales
		long suma = 0;
		
		for (int i = 0; i < NUM_TAREAS; i++) {
			// imprimir resultados parciales
			suma += tareas[i].getResultado();
		}

		System.out.println(String.format("%s\n - Resultado: %d", "Paralelización con CountDownLatch", suma));

		// Convertir la duración a segundos
		long duracion = System.currentTimeMillis() - instanteInicial;
		SimpleDateFormat sdf = new SimpleDateFormat("ss.SSSS");

		System.out.println(String.format(" - Duración: %s seg.", sdf.format(new Date(duracion))));
	}
}
