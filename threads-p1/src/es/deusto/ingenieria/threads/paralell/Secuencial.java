package es.deusto.ingenieria.threads.paralell;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Queremos hacer una tarea compleja: sumar todos los números entre 0 y 10 millones.
 * 
 * Este ejemplo muestra una tarea larga que no aprovecha la posibilidad de
 * paralelismo que ofrece una CPU con varios núcleos.
 * La tarea se ejecuta en un solo hilo (principal).
 */
public class Secuencial {
	// Número máximo de la suma
	private static final int MAX_NUM = 1000_000_000;

	public static void ejecutar() {
		// Instante de tiempo inicial
		long instanteInicial = System.currentTimeMillis();
		
		long suma = 0;

		for (int i = 1; i <= MAX_NUM; i++) {
			suma += i;
		}

		System.out.println(String.format("%s\n - Resultado: %d", "Secuencial", suma));

		// Convertir la duración a segundos
		long duracion = System.currentTimeMillis() - instanteInicial;
		SimpleDateFormat sdf = new SimpleDateFormat("ss.SSSS");

		System.out.println(String.format(" - Duración: %s seg.", sdf.format(new Date(duracion))));
	}
}