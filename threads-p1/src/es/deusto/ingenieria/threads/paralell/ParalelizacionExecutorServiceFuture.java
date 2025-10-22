package es.deusto.ingenieria.threads.paralell;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * En este ejemplo se muestra el uso del API Future de Java para
 * realizar las diferentes tareas en las que se particiona la suma
 * de manera paralela, obtener las sumas parciales y agregar
 * todos los resultados parciales posteriormente 
 */
public class ParalelizacionExecutorServiceFuture {

    // el número máximo hasta el que sumar desde cero
    private static final int MAX_NUM = 1000_000_000;
	// el número máximo de hilos de la CPU
	private static final int MAX_HILOS = Runtime.getRuntime().availableProcessors();
    // el número de tareas que se van a crear para procesar la suma
    private static final int NUM_TAREAS = 20;
    
    public static void ejecutar() {
		// Instante de tiempo inicial
		long instanteInicial = System.currentTimeMillis();
		// definimos el rango de las sumas parciales
		int rango = MAX_NUM / NUM_TAREAS;

    	// creamos el pool de hilos
        ExecutorService executor = Executors.newFixedThreadPool(MAX_HILOS);
        // creamos una lista para almacenar las tareas que vamos a lanzar
        List<Future<Long>> tareas = new ArrayList<Future<Long>>();

        // creamos las tareas, los hilos y los lanzamos
        for (int i = 0; i < NUM_TAREAS; i++) {
            // calculamos los rangos de inicio y fin de cada tarea
            int inicio = i * rango + 1;
            int fin = i == NUM_TAREAS - 1? fin = MAX_NUM : (i + 1) * rango;
            
            // Creamos la tarea desde el ExecutorService cun un lambda de Runnable
            Future<Long> tarea = executor.submit(() -> { 
            	long resultado = 0;
            	
                for (long v = inicio; v <= fin; v++) {
                    resultado += v;
                }
                
                return resultado;
            });
            
            // almacenamos la tarea lanzada para esperar a que termine
            tareas.add(tarea);
        }

        long suma = 0;
        
        // se inicia la parada del ExecutorService
        executor.shutdown();
        
        // sumamos los resultados parciales
        for (Future<Long> tarea : tareas) {
        	try {
				suma += tarea.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
        }
        
		System.out.println(String.format("%s\n - Resultado: %d", "Paralelización con Executor Service y Future", suma));

		// Convertir la duración a segundos
		long duracion = System.currentTimeMillis() - instanteInicial;
		SimpleDateFormat sdf = new SimpleDateFormat("ss.SSSS");

		System.out.println(String.format(" - Duración: %s seg.", sdf.format(new Date(duracion))));
    }
}
