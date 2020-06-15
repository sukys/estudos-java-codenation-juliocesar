package lsukys.codenation.dev.juliocesar.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiUtil {

	private static Logger log = LogUtil.getLogger(ApiUtil.class);
	
	/**
	 * Converte um mapa com os parâmetros em uma String.
	 * 
	 * @param parametros
	 * @return
	 * 
	 */
	public static String recuperarParametros(String url, Map<String, String> parametros) {
		log.debug("Contruindo URL com os parametros passados.");
		StringBuilder retorno = new StringBuilder(url);
		retorno.append("?");
		try {
			for (Map.Entry<String, String> item : parametros.entrySet()) {
				retorno.append("&");
				retorno.append(URLEncoder.encode(item.getKey(), "UTF-8"));
				retorno.append("=");
				retorno.append(URLEncoder.encode(item.getValue(), "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			log.error("Retornando a url >> Erro ao efetuar o encode dos parametros: {} ", e.getMessage());
			return url;
		}
		
		log.debug("Url contruida com sucesso.");
		return retorno.toString().replaceFirst("&", "");
	}

	/**
	 * Método para efetuar uma requisição GET. Retorna o resultado da requisição em
	 * forma de String.
	 * 
	 * @return
	 */
	public static String getRequest(String url, Map<String, String> parametros) {
		log.debug("Inciando a requisição GET");
		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = null;
		try {
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(recuperarParametros(url, parametros)))
					.build();
			log.debug("Enviando a requisição.");
			response = client.send(request, HttpResponse.BodyHandlers.ofString());

		} catch (IOException | InterruptedException e) {
			log.error("Erro so enviar a solicitação: {} ", e.getMessage());
		}
		log.debug("Requisição GET realizada. Retornando o conteúdo.");
		return response.body();
	}

	
	public static String sendFile(String url, Map<String, String> parametros, File file) {
		log.debug("Iniciando o procedimento para envio do arquivo via POST.");
		
		OkHttpClient client = new OkHttpClient();
		RequestBody formBody;
		Response response;
		url =  ApiUtil.recuperarParametros(url, parametros);
    	String status = "0";
        
        try {
        	log.debug("Criando o corpo da requisição.");
        	formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("answer", file.getName(),
                        // RequestBody.create(MediaType.parse("application/json"), file))
                    	RequestBody.create(Files.readAllBytes(file.toPath()), MediaType.parse("application/json")))
                    .addFormDataPart("Content-Type", "multipart/form-data")
                    .build();
        	
        	Request request = new Request.Builder().url(url).post(formBody).build();
        	log.debug("Enviando a requisição POST.");
        	response = client.newCall(request).execute();
        	status = String.valueOf(response.code());
			
        	log.debug("Requisição efetuada. Verificando retorno:");
			log.debug("Codigo: {}", status);
			log.debug("Body: \n{}", response.body().string());
			log.debug("Message: \n{}", response.message());
		} catch (IOException e) {
			log.error("Falha ao enviar a requisição: {} ", e.getMessage());
		}
        log.debug("Requisição POST encerrada com status '{}' .", status);
        return status;
	}
	
}
