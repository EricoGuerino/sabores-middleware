package com.tcc.saboresmiddleware.reports.dto;

import java.io.InputStream;
import java.io.Serializable;

public class ProdutoCatalogoDTO implements Serializable {
	
	private static final long serialVersionUID = -5272845712192657793L;
	private String nomeProduto1;
	private String nomeProduto2;
	private String nomeProduto3;
	
	private String descricaoProduto1;
	private String descricaoProduto2;
	private String descricaoProduto3;
	
	private InputStream imagemProduto1;
	private InputStream imagemProduto2;
	private InputStream imagemProduto3;
	
	private InputStream semLactose1;
	private InputStream semLactose2;
	private InputStream semLactose3;
	
	private InputStream semGluten1;
	private InputStream semGluten2;
	private InputStream semGluten3;
	
	private InputStream vegano1;
	private InputStream vegano2;
	private InputStream vegano3;
	
	public ProdutoCatalogoDTO() {}

	public String getNomeProduto1() 		{ return nomeProduto1; 		}
	public String getNomeProduto2() 		{ return nomeProduto2; 		}
	public String getNomeProduto3() 		{ return nomeProduto3; 		}
	public String getDescricaoProduto1() 	{ return descricaoProduto1; }
	public String getDescricaoProduto2() 	{ return descricaoProduto2; }
	public String getDescricaoProduto3() 	{ return descricaoProduto3; }
	public InputStream getImagemProduto1() 	{ return imagemProduto1; 	}
	public InputStream getImagemProduto2() 	{ return imagemProduto2; 	}
	public InputStream getImagemProduto3() 	{ return imagemProduto3; 	}
	public InputStream getSemLactose1() 	{ return semLactose1; 		}
	public InputStream getSemLactose2() 	{ return semLactose2; 		}
	public InputStream getSemLactose3() 	{ return semLactose3; 		}
	public InputStream getSemGluten1() 		{ return semGluten1; 		}
	public InputStream getSemGluten2() 		{ return semGluten2; 		}
	public InputStream getSemGluten3() 		{ return semGluten3; 		}
	public InputStream getVegano1() 		{ return vegano1; 			}
	public InputStream getVegano2() 		{ return vegano2; 			}
	public InputStream getVegano3() 		{ return vegano3; 			}

	public void setNomeProduto1(String nomeProduto1) 			{ this.nomeProduto1 = nomeProduto1; 			}
	public void setNomeProduto2(String nomeProduto2) 			{ this.nomeProduto2 = nomeProduto2; 			}
	public void setNomeProduto3(String nomeProduto3) 			{ this.nomeProduto3 = nomeProduto3; 			}
	public void setDescricaoProduto1(String descricaoProduto1) 	{ this.descricaoProduto1 = descricaoProduto1; 	}
	public void setDescricaoProduto2(String descricaoProduto2) 	{ this.descricaoProduto2 = descricaoProduto2; 	}
	public void setDescricaoProduto3(String descricaoProduto3) 	{ this.descricaoProduto3 = descricaoProduto3; 	}
	public void setImagemProduto1(InputStream imagemProduto1) 	{ this.imagemProduto1 = imagemProduto1; 		}
	public void setImagemProduto2(InputStream imagemProduto2) 	{ this.imagemProduto2 = imagemProduto2; 		}
	public void setImagemProduto3(InputStream imagemProduto3) 	{ this.imagemProduto3 = imagemProduto3; 		}
	public void setSemLactose1(InputStream semLactose1) 		{ this.semLactose1 = semLactose1; 				}
	public void setSemLactose2(InputStream semLactose2) 		{ this.semLactose2 = semLactose2; 				}
	public void setSemLactose3(InputStream semLactose3) 		{ this.semLactose3 = semLactose3; 				}
	public void setSemGluten1(InputStream semGluten1) 			{ this.semGluten1 = semGluten1; 				}
	public void setSemGluten2(InputStream semGluten2) 			{ this.semGluten2 = semGluten2; 				}
	public void setSemGluten3(InputStream semGluten3) 			{ this.semGluten3 = semGluten3; 				}
	public void setVegano1(InputStream vegano1) 				{ this.vegano1 = vegano1; 						}
	public void setVegano2(InputStream vegano2) 				{ this.vegano2 = vegano2; 						}
	public void setVegano3(InputStream vegano3) 				{ this.vegano3 = vegano3; 						}
	
}
