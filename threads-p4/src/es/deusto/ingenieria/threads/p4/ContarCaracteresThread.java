package es.deusto.ingenieria.threads.p4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ContarCaracteresThread {

	private static final String RUTA = "resources/";

	public static void main(String[] args) {
		try {
			File carpeta = new File(RUTA);
			File[] archivos = carpeta.listFiles();

			for (File archivo : archivos) {
				if (archivo.isFile()) {
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

						System.out.println(String.format("'%s' \n - %d líneas \n - %d palabras \n - %d caracteres", archivo.getName(), numLineas, numPalabras, caracteres));
					} catch (Exception e) {
						System.err.println(String.format("Error al procesar '%s'", archivo.getName()));
					}
				}
			}
		} catch (Exception e) {
			System.err.println(String.format("Error al leer la carpeta '%s'", RUTA));
		}
	}
}
