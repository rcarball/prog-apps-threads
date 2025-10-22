package es.deusto.ingenieria.threads.paralell;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Queremos hacer una tarea compleja: sumar todos los números entre 0 y 10
 * millones.
 * 
 * En este ejemplo, la tarea se descompone en varios hilos que se ejecutan y
 * suman los resultados parciales entre dos límites. Cuando todos los hilos han
 * terminado se suman los resultados parciales.
 */
public class ParalelizacionBasica {
	// Clase interna que implementa la tarea de sumar valores
	// entre dos límites
	private static class TareaSumar implements Runnable {
	    private int inicio;
	    private int fin;
	    private long resultado;

	    public TareaSumar(int inicio, int fin) {
	        this.inicio = inicio;
	        this.fin = fin;
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
	    }
	}
	
	// el número máximo hasta el que sumar desde cero
	private static final int MAX_NUM = 1000_000_000;
	// el número de hilos que se van a crear en el pool
	private static final int MAX_HILOS = 20;

	public static void ejecutar() {
		// Instante de tiempo inicial
		long instanteInicial = System.currentTimeMillis();
		// definimos el rango de las sumas parciales
		int rango = MAX_NUM / MAX_HILOS;

		// creamos un array de hilos
		Thread[] hilos = new Thread[MAX_HILOS];
		// creamos un array de tareas
		TareaSumar[] tareas = new TareaSumar[MAX_HILOS];

		// creamos las tareas, los hilos y los lanzamos
		for (int i = 0; i < MAX_HILOS; i++) {
			// calculamos los rangos de inicio y fin de cada tarea
			int inicio = i * rango + 1;
			int fin = (i == MAX_HILOS - 1) ? MAX_NUM : ((i + 1) * rango);

			tareas[i] = new TareaSumar(inicio, fin);
			hilos[i] = new Thread(tareas[i]);
			hilos[i].start();
		}

		// esperamos a que todos los hilos terminen
		for (int i = 0; i < MAX_HILOS; i++) {
			try {
				hilos[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// sumamos los resultados parciales
		long suma = 0;

		for (int i = 0; i < MAX_HILOS; i++) {
			// imprimir resultados parciales
			suma += tareas[i].getResultado();
		}

		System.out.println(String.format("%s\n - Resultado: %d", "Paralelización básica con join()", suma));

		// Convertir la duración a segundos
		long duracion = System.currentTimeMillis() - instanteInicial;
		SimpleDateFormat sdf = new SimpleDateFormat("ss.SSSS");

		System.out.println(String.format(" - Duración: %s seg.", sdf.format(new Date(duracion))));
	}
}
