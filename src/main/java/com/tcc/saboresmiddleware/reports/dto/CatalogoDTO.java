package com.tcc.saboresmiddleware.reports.dto;

import java.io.Serializable;
import java.util.List;

public class CatalogoDTO implements Serializable {
	
	private static final long serialVersionUID = -542720621808889194L;
	private String empresa;
	private List<ProdutoCatalogoDTO> produtos;
	
	public CatalogoDTO() {}
	
	public String getEmpresa() 						{ return empresa; 	}
	public List<ProdutoCatalogoDTO> getProdutos() 	{ return produtos; 	}
	
	public void setEmpresa(String empresa) 						{ this.empresa = empresa; 	}
	public void setProdutos(List<ProdutoCatalogoDTO> produtos) 	{ this.produtos = produtos; }
	
}
