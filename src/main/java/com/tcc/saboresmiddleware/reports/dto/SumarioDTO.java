package com.tcc.saboresmiddleware.reports.dto;

import java.io.Serializable;

public class SumarioDTO implements Serializable {
	
	private static final long serialVersionUID = -516259738104463160L;
	private String index;
	private String empresa;
	private String pagina;
	
	public SumarioDTO() {}
	
	public String getIndex() 	{ return index; 	}
	public String getEmpresa() 	{ return empresa; 	}
	public String getPagina() 	{ return pagina; 	}
	public void setIndex(String index) 		{ this.index = index; 		}
	public void setEmpresa(String empresa) 	{ this.empresa = empresa; 	}
	public void setPagina(String pagina) 	{ this.pagina = pagina; 	}
	
}
