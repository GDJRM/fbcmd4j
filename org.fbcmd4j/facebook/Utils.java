package facebook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.auth.AccessToken;
import facebook4j.internal.org.json.JSONObject;

public class Utils {
	
	static final Logger logger = LogManager.getLogger(Utils.class);
	
	
	public static void obtenerAccessTokens(String folderName, String fileName, Properties props, Scanner scanner) {
	
		if(props.getProperty("oauth.appId").isEmpty() || props.getProperty("oauth.appSecret").isEmpty()) {
			System.out.println("Ingrese appId: ");
			props.setProperty("oauth.appId", scanner.nextLine());
			System.out.println("oauth.appSecret");
			props.setProperty("oauth.appSecret", scanner.nextLine());
		}
		

		
		try {
			URL url = new URL("https://graph.facebook.com/v2.12/device/login");
			
			Map<String, Object> data = new LinkedHashMap<>();
			
			data.put("access_token", "231297794085501|chzSpiDgS28u4I8kJGUjSwFdByU");
			data.put("scope", props.getProperty("oauth.permissions"));
			
			StringBuilder post = new StringBuilder();
			
			for(Map.Entry<String, Object> d : data.entrySet()) {
				
					if(post.length() != 0) { 
						post.append('&');
						}
					post.append(URLEncoder.encode(d.getKey(),"UTF-8"));
					post.append('=');
					post.append(URLEncoder.encode(String.valueOf(d.getValue()),"UTF-8"));
						
			}
			
			byte[] postDataBytes = post.toString().getBytes("UTF-8");
			
			HttpURLConnection  connect = (HttpURLConnection)url.openConnection();
			connect.setRequestMethod("POST");
			connect.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			connect.setDoOutput(true);
			connect.getOutputStream().write(postDataBytes);
			
			Reader in = new BufferedReader (new InputStreamReader(connect.getInputStream(),"UTF-8"));
			
			StringBuilder sb = new StringBuilder();
			for(int d; (d = in.read())>=0;) {
				sb.append((char)d);
			}
			
			String response = sb.toString();
			JSONObject obj = new JSONObject(response);
			String code = obj.getString("code");
			String userCode = obj.getString("user_code");
			
			System.out.println("Ingresa a la pagina https://www.facebook.com/device con el codigo: " +userCode);
			String accessToken ="";
			while(accessToken.isEmpty()) {
			try {
				TimeUnit.SECONDS.sleep(5);
			}catch(InterruptedException e){
				
				logger.error(e);
				
		}
			
			
			URL urldos = new URL("https://graph.facebook.com/v2.12/device/login_status");
			data = new LinkedHashMap<>();
			data.put("acces_token","231297794085501|chzSpiDgS28u4I8kJGUjSwFdByU" );
			data.put("code", code);
			
			post = new StringBuilder();
			for(Map.Entry<String, Object> d : data.entrySet()) {
				
					if(post.length()!=0) {
						post.append("&");
						}
					
						post.append(URLEncoder.encode(d.getKey(),"UTF-8"));
						post.append("=");
						post.append(URLEncoder.encode(String.valueOf(d.getValue()),"UTF-8"));
			}
			
			postDataBytes = post.toString().getBytes("UTF-8");
			
			HttpURLConnection connectdos = (HttpURLConnection)urldos.openConnection();
			connectdos.setRequestMethod("POST");
			connectdos.setRequestProperty("content-Type", "application/json; charset=UTF-8");
			connectdos.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
			connectdos.setDoOutput(true);
			connectdos.getOutputStream().write(postDataBytes);
			
			try {
				in = new BufferedReader(new InputStreamReader(connectdos.getInputStream(),"UTF-8"));
				sb = new StringBuilder();
				for(int d;(d=in.read())>=0;) {
					sb.append((char)d);
				}
				response= sb.toString();
				obj = new JSONObject(response);
				accessToken = obj.getString("access_token");
			}catch(IOException ignore) {
				logger.error(ignore);
			}
			
	}
	
	props.setProperty("oauth.accessToken", accessToken);
	saveProperties(folderName,fileName,props);

    logger.info("Configuracion guardada");
	}catch(Exception e) {
	logger.error(e);

}

}
		


public static void saveProperties(String folderName,String fileName,Properties props) throws IOException{
	Path configFile = Paths.get(folderName, fileName);
	props.store(Files.newOutputStream(configFile), "Generado por ObtenerAccessToken");	
}

public static Properties loadPropertiesFromFile(String folderName, String fileName) throws IOException{
	Properties props = new Properties();
	Path configFolder = Paths.get(folderName);
	Path configFile = Paths.get(folderName, fileName);
	if(Files.exists(configFile)) {
			logger.info("Creando archivo de configuracion");
			
				if(!Files.exists(configFolder)) {
					Files.createDirectory(configFolder);
				
						
					Files.copy(Utils.class.getResourceAsStream("fbcmd4j.properties"), configFile);
				}
		
				props.load(Files.newInputStream(configFile));
				BiConsumer<Object, Object> emptyProperty = (k,v)->{
						if(((String)v).isEmpty()) {
							logger.info("La propiedad"+k+"esta vacia");
						}
			
				 };
				 
				 props.forEach(emptyProperty);
	
	}else {
		logger.info("Creando nuevo archivo de configuracion.");
		Files.copy(Paths.get("facebook", "fbcmd4j.properties"),configFile);
	}
		
	return props;
}

public static Facebook configurarFacebook(Properties props) {
		Facebook face = new FacebookFactory().getInstance();
		logger.info("Configurando Instancia de Facebook");
		face.setOAuthAppId(props.getProperty("oauth.appId"), props.getProperty("oauth.appSecret"));
		face.setOAuthPermissions(props.getProperty("oauth.permissions"));
		if(props.getProperty("oauth.accessToken")!= null) {
				face.setOAuthAccessToken(new AccessToken(props.getProperty("oauth.accessToken"),null));
		}
		
		return face;
}


public static void publicarEstado(String estado, Facebook fb) throws FacebookException {
	
	try {
		fb.postStatusMessage(estado);
	}catch(FacebookException e) {
		logger.error(e);
	}
	
}

public static void compartirLink(String link, Facebook fb)throws MalformedURLException, FacebookException {
	try {
		fb.postLink(new URL(link));
	}catch(MalformedURLException e) {
		logger.error(e);
	}catch(FacebookException e){
		logger.error(e);
	}
	
}

public static String savePosts(String fileName, List<Post>posts) {
	File file = new File(fileName + ".txt");
	
	try {
		if(!file.exists()) {
			file.createNewFile();
		}
	
		FileOutputStream savefile = new FileOutputStream(file);
		for(Post p : posts) {
				String msg = "";
				
				if(p.getStory()!= null) {
					msg += "Story: " + p.getStory() + "\n";
				
				}
				if(p.getMessage()!= null) {
					msg += "Mensaje: " + p.getMessage()+"\n";
				}
				savefile.write(msg.getBytes());
		}
		
		savefile.close();
		logger.info("Posts guardados en " + file.getName() );
		System.out.println("Post guardados en archivo" + file.getName());
	}catch(IOException e) {
		logger.error(e);
	}
	return file.getName();
}

}


	
	
	
	
	

