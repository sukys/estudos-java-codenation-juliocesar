package lsukys.codenation.dev.juliocesar.utils;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;

public class CriptoUtil {

	private static Logger log = LogUtil.getLogger(CriptoUtil.class);
	private static final String ALFABETO = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
	private static final List<String> ALFABETO_LIST = Arrays.asList(ALFABETO.split(""));

	/**
	 * Decifra um codigo informando o texto para decifrar e o numero de casas para
	 * conversão.
	 * 
	 * @param textoCifrado
	 * @param numeroCasas
	 * @return
	 */
	public static String decifrar(String textoCifrado, int numeroCasas) {
		log.debug("Decifrando a mensagem pelo algoritmo de Julio Cesar.");
		List<String> cifradoList = Arrays.asList(textoCifrado.split(""));
		String decifrado = "";
		for (String letter : cifradoList) {
			letter = letter.trim().toLowerCase();
			if (letter.isEmpty()) {
				decifrado = decifrado.concat(" ");
			} else if (!ALFABETO.contains(letter)) {
				decifrado = decifrado.concat(letter);
			} else {
				int posAlfabeto = ALFABETO_LIST.indexOf(letter);
				posAlfabeto = posAlfabeto - numeroCasas;
				if (posAlfabeto < 0) {
					posAlfabeto = 22 - posAlfabeto;
				}
				decifrado = decifrado.concat(ALFABETO_LIST.get(posAlfabeto));
			}
		}
		return decifrado;
	}

	/**
	 * Gera um hash do tipo SHA1 com a string informada
	 * 
	 */
	public static String gerarSha1(String texto) {
		log.debug("Cifrando a mensagem apra SHA1.");
		return DigestUtils.sha1Hex(texto.getBytes());
	}
}
