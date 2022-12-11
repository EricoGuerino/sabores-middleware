package com.tcc.saboresmiddleware.dto;

import java.io.Serializable;

import org.apache.tomcat.util.codec.binary.Base64;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArquivoDTO implements Serializable {

	private static final long serialVersionUID = 4926588865365455812L;
	private byte[] arquivo;
	private String fileName;
	
	public ArquivoDTO() {}
	public ArquivoDTO(byte[] arquivo, String fileName) {
		setArquivo(arquivo);
		setFileName(fileName);
	}
	
	public byte[] getArquivo() 	{ return arquivo; 	}
	public String getFileName() { return fileName; 	}
	
	public void setArquivo(byte[] arquivo) 		{ this.arquivo = arquivo; 	}
	public void setFileName(String fileName) 	{ this.fileName = fileName; }
	
	@JsonProperty("dadosBase64")
	public String getDadosBase64() {
		if (arquivo != null) {
			return Base64.encodeBase64String(arquivo);
		}
		
		return "";
	}
}
