package facebook;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.ResponseList;

public class Main {
	 static final Logger logger = LogManager.getLogger(Main.class);
	 private static final String CONFIG_DIR = "config";
	 private static final String CONFIG_FILE = "fbcmd4j.properties";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.info("Iniciando app");
		Facebook fb = null;
		Properties props = null;
		
		//Carga propiedades
		try {
			props = Utils.loadPropertiesFromFile(CONFIG_DIR, CONFIG_FILE);
		}catch(IOException ex) {
			logger.error(ex);
		}
		
		//Cargando Menu
		int seleccion;
		try(Scanner  scanner = new Scanner(System.in)) {
			while(true) {
			//Menu
			fb = Utils.configurarFacebook(props);
			System.out.format("Cliente Simple de Facebook" + "\n");
			System.out.println("Opciones: ");
			System.out.println("0) Cargar configuracion");
			System.out.println("1) Publicar Estado");
			System.out.println("2) Cargar NewsFeed");
			System.out.println("3) Obtener wall");
			System.out.println("4) Publicar Link ");
			System.out.println("5) Salir ");
			System.out.println("\n Ingresa una opcion: ");
			
			try {
				seleccion = scanner.nextInt();
				scanner.nextLine();
				
				switch(seleccion) {
						case 0:
							Utils.obtenerAccessTokens(CONFIG_DIR, CONFIG_FILE, props, scanner);
							props= Utils.loadPropertiesFromFile(CONFIG_DIR, CONFIG_FILE);
							break;
				
						case 1:
							System.out.println("¿Qué estas pensando?");
							String estado = scanner.nextLine();
							Utils.publicarEstado(estado, fb);
							break;
							
						case 2:
							System.out.println("Cargando NewsFeed...");
							ResponseList<Post> newsFeed = fb.getFeed();
							newsFeed.forEach(System.out::println);
							Utils.savePosts("Newsfeed", newsFeed);
							break;
							
						case 3:
							System.out.println("Cargando Wall...");
							ResponseList<Post> wall = fb.getPosts();
							wall.forEach(System.out::println);
							Utils.savePosts("Wall", wall);
							
							
						case 4:
							System.out.println("Compartir link: ");
							String link = scanner.nextLine();
							Utils.compartirLink(link, fb);
							break;
							
						case 5:
							System.exit(0);
						default:
							logger.error("Opcion invalida");
							break;
				}
			}catch(InputMismatchException ex) {
				System.out.println("Ocurrio un error, favor de revisar log.");
				logger.error("Opcion invalida. s%. \n", ex.getClass());
				scanner.next();	
			}catch(FacebookException ex) {
				System.out.println("Ocurrio un error, favor de revisar log.");
				logger.error(ex.getErrorMessage());
				scanner.next();
			}catch(NoSuchFileException io) {
				System.out.println("Archivo de configuracion no existe.");
			}catch(Exception ex) {
				System.out.println("Ocurrio un error, favor de revisar log.");
				logger.error(ex);
				scanner.next();
			}
		}
	}catch(Exception ex) {
		logger.error(ex);
	}

	}

}
