package es.deusto.ingenieria.threads.p4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ContarCaracteresThread {

	private static final String RUTA = "resources/";

	public static void main(String[] args) {
		try {						
			File carpeta = new File(RUTA);
			File[] archivos = carpeta.listFiles();

			System.out.println(String.format("Iniciando proceso de %d archivos\n", archivos.length));
			
			// Lista que almacena los hilos para poder esperar a que terminen
			List<Thread> hilos = new ArrayList<>();
			
			for (File archivo : archivos) {
				if (archivo.isFile()) {
					// Crear un hilo por cada archivo
					Thread hilo = new Thread(() -> {
						// El hilo procesa cada archivo
						String linea;

						int caracteres = 0;
						int numLineas = 0;
						int numPalabras = 0;

						try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
							while ((linea = br.readLine()) != null) {
								numLineas++;
								caracteres += linea.length();
								numPalabras += linea.split(" ").length;
							}

							// Dormimos el hilo entre 5-10 segundos para simular un proceso largo
							// Generamos un número aleatorio entre 5 y 10 y lo multiplicamos por 1000
							Thread.sleep((new Random().nextInt(5) + 5) * 1000);
							
							System.out.println(String.format("%s: '%s' \n - %d líneas \n - %d palabras \n - %d caracteres", Thread.currentThread().getName(), archivo.getName(), numLineas, numPalabras, caracteres));
						} catch (Exception e) {
							System.err.println(String.format("Error al procesar '%s'", archivo.getName()));
						}
					});
					
					hilos.add(hilo);
					hilo.start();				
				}
			}
			
			// Esperamos a que todos los hilos terminen
			hilos.forEach(hilo -> {
				try {
					hilo.join();
				} catch (InterruptedException e) {
					System.err.println(String.format("Error al esperar a que termine '%s'", hilo.getName()));
				}
			});
			
			System.out.println("\nProceso finalizado");
		} catch (Exception e) {
			System.err.println(String.format("Error al leer la carpeta '%s'", RUTA));
		}
	}
}
