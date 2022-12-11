package com.tcc.saboresmiddleware.reports.dto;

import java.io.Serializable;

public class ProdutoDTO implements Serializable {

	private static final long serialVersionUID = 8019704532119135887L;
	private String index;
	private String preco;
	private String nome;
	
	public ProdutoDTO() {}
	
	public String getIndex() 	{ return index; }
	public String getPreco() 	{ return preco; }
	public String getNome() 	{ return nome;	}
	public void setIndex(String index) 	{ this.index = index; }
	public void setPreco(String preco) 	{ this.preco = preco; }
	public void setNome(String nome) 	{ this.nome = nome; }
}
