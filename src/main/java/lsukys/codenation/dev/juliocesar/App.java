package lsukys.codenation.dev.juliocesar;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lsukys.codenation.dev.juliocesar.entity.CodenationMessage;
import lsukys.codenation.dev.juliocesar.utils.ApiUtil;
import lsukys.codenation.dev.juliocesar.utils.CriptoUtil;
import lsukys.codenation.dev.juliocesar.utils.FileUtil;
import lsukys.codenation.dev.juliocesar.utils.LogUtil;

public class App {

	/**
	 * DEFINIR O TOKEN
	 */
	private static String TOKEN_VALUE = "";
	
	private static Logger log = LogUtil.getLogger(App.class);
	private static ObjectMapper mapper = new ObjectMapper();
	
	private static final Map<String, String> PARAMETROS = new HashMap<String, String>();
	private static final String FILENAME = "answer.json";
	private static final String URL_REQUEST ="https://api.codenation.dev/v1/challenge/dev-ps/generate-data";
	private static final String URL_POST = "https://api.codenation.dev/v1/challenge/dev-ps/submit-solution";
	

	/**
	 * Inicia a aplicação. 
	 * Caso o token nao esteja definido no código, informar por parâmetro. 
	 * Se necessário, mudar o nivel do log em src/main/resources/log4j.properties
	 * @param args
	 */
	public static void main(String[] args) { 
		log.info("Iniciando a aplicação.");
		if(TOKEN_VALUE.trim().isEmpty() && args.length > 0) {
			log.info("Definindo o token '{}' informado por parâmetro.", args[0]);
			TOKEN_VALUE = args[0];
		}
		
		if(TOKEN_VALUE == null || TOKEN_VALUE.trim().isEmpty()) {
			log.error(">>>>> É NECESSÁRIO INFORMAR O TOKEN <<<<<");
		}else {
			IniciarApp();
		}
		log.info("Aplicação Encerrada.");
	}

	
	/**
	 * Executa os comandos necessários para solução do desafio.
	 * 
	 */
	private static void IniciarApp() {

		PARAMETROS.put("token", TOKEN_VALUE);
		File arquivoSolucao = new File(FILENAME);
		
		String result = getMessage(arquivoSolucao);
		log.debug("Verificando a mensagem: \n {}", result.replaceAll(",\"", ",\n\t\""));
		
		CodenationMessage message = null;
		try {
			message = mapper.readValue(result, CodenationMessage.class);
		} catch (JsonProcessingException e) {
			log.error("Falha ao converter o json para Objeto.");
		}
		
		if(message != null && message.getDecifrado() == null || message.getDecifrado().trim().isEmpty()) {
			message.setDecifrado(CriptoUtil.decifrar(message.getCifrado(), message.getNumeroCasas()));
			arquivoSolucao  = FileUtil.saveToFile(FILENAME, message.toString());
		}
		
		if(message != null && message.getResumoCriptografico() == null || message.getResumoCriptografico().trim().isEmpty()) {
			message.setResumoCriptografico(CriptoUtil.gerarSha1(message.getDecifrado()));
			arquivoSolucao  = FileUtil.saveToFile(FILENAME, message.toString());
		}
		
		log.debug("Verificando arquivo: \n-------\n {} \n-------", FileUtil.checkFile(arquivoSolucao).replaceAll(",\"", ",\n\t\"") );
		
		String retorno = ApiUtil.sendFile(URL_POST, PARAMETROS, arquivoSolucao);
		
		if("200".equalsIgnoreCase(retorno)) {
			log.debug("Retorno ocorreu com sucesso.");
			FileUtil.deleteFile(arquivoSolucao);
		}
	
	}
	
	/**
	 * Recupera o conteudo da mensagem.
	 * Se o arquivo existir, recupera a partir do arquivo. 
	 * Se não existir, recupera da API.
	 * @param arquivo
	 * @return
	 */
	private static String getMessage(File arquivo) {
		String content = "";
		if(arquivo != null && Files.exists(arquivo.toPath()) ) {
			log.info("Recuperando mensagem a partir do arquivo.");
			content = FileUtil.checkFile(arquivo);
		} else {
			log.info("Arquivo inexistente. Efetuando requisição GET para recuperar a mensagem.");
			content = ApiUtil.getRequest(URL_REQUEST, PARAMETROS);
		}
		return content;
	}

}
