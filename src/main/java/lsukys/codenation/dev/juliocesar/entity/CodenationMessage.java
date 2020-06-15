package lsukys.codenation.dev.juliocesar.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodenationMessage {
	
	@JsonProperty(value = "numero_casas")
	private Integer numeroCasas;

	@JsonProperty(value = "token")
	private String token;
	
	@JsonProperty(value = "cifrado")
	private String cifrado; 
	
	@JsonProperty(value = "decifrado")
	private String decifrado;
	
	@JsonProperty(value = "resumo_criptografico")
	private String resumoCriptografico;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"numero_casas\":");
		builder.append(numeroCasas);
		builder.append(",\"token\":\"");
		builder.append(token);
		builder.append("\",\"cifrado\":\"");
		builder.append(cifrado);
		builder.append("\",\"decifrado\":\"");
		builder.append(decifrado);
		builder.append("\",\"resumo_criptografico\":\"");
		builder.append(resumoCriptografico);
		builder.append("\"}");
		return builder.toString();
	}
	
	
	
}
