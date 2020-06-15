package lsukys.codenation.dev.juliocesar.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;

public class FileUtil {
	
	private static Logger log = LogUtil.getLogger(FileUtil.class);
	
	/**
	 * Salva o o conteudo no arquivo para uso posterior.
	 * 
	 * @param fileContent
	 */
	public static File saveToFile(String filename, String fileContent) {
		log.debug("Salvando o arquivo '{}'.", filename);
		File arquivo = null;
		try {
			arquivo = new File(filename);
			Path path = Paths.get(arquivo.getAbsolutePath());
			byte[] strToBytes = fileContent.getBytes();
			Files.write(path, strToBytes);
		} catch (IOException e) {
			log.error("Erro ao escrever o arquivo.");
		}
		return arquivo;
	}

	/**
	 * Verifica o arquivo escrito, retornando o seu conteúdo.
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static String checkFile(File file) {
		log.debug("Verificando o conteudo do arquivo '{}'.", file.getName());
		if (!Files.exists(file.toPath())) {
			log.error("Arquivo informado não existe. Retornando 'null'.");
			return null;
		}
		StringBuilder conteudo = new StringBuilder();
		log.info("Lendo o conteudo do arquivo.");
		try {
			for (String line : Files.readAllLines(file.toPath())) {
				conteudo.append(line);
				conteudo.append("\n");
			}
		} catch (IOException e) {
			log.error("Erro ao ler o arquivo '{}'.", file.getName());
		}

		log.debug("Arquivo verificado. Retornando o conteúdo.");
		return conteudo.toString();
	}
	
	/**
	 * Apaga o arquivo.
	 * @param file
	 */
	public static void deleteFile(File file) {
		log.debug("Apagando o arquivo.");
		try {
			Files.delete(file.toPath());
			log.warn("Arquivo apagado.");
		} catch (IOException e) {
			log.error("Falha ao deletar o arquivo '{}' .", file.getName());
		}		
	}
	
}
