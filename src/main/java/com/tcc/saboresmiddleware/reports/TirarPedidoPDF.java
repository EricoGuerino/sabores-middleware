package com.tcc.saboresmiddleware.reports;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class TirarPedidoPDF implements Serializable
{
	
	private static final long serialVersionUID = 9219016209397236198L;
	
	private String telefone;
	private String razaoEmpresa;
	private String razaoCliente;
	private List<InnerItens> itens;
	private String nomeResponsavel;
	private String totalCompra;
	private String dataCompra;
	private String dataVencimento;
	private String prazo;
	private String dataImpressao;
	
	public TirarPedidoPDF(){}
	
	public TirarPedidoPDF(String telefone, String razaoEmpresa,
			String razaoCliente, List<InnerItens> itens,
			String nomeResponsavel, String totalCompra, String dataCompra,
			String dataVencimento, String prazo, String dataImpressao) {
		
		setTelefone(telefone);
		setRazaoEmpresa(razaoEmpresa);
		setRazaoCliente(razaoCliente);
		setItens(itens);
		setNomeResponsavel(nomeResponsavel);
		setTotalCompra(totalCompra);
		setDataCompra(dataCompra);
		setDataVencimento(dataVencimento);
		setPrazo(prazo);
		setDataImpressao(dataImpressao);
		getLogo();
	}

	public byte[] getTirarPedidoPDFBytes() {
		
		List<TirarPedidoPDF> lista = new ArrayList<>();
		lista.add(this);
		
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lista);
        JasperPrint impressao = null;
        byte[] byteArray = null;
        
        try {
            impressao = JasperFillManager.fillReport(getTirarPedidoPDF(), null, ds);
            byteArray = JasperExportManager.exportReportToPdf(impressao);
//            JasperViewer viewer = new JasperViewer(impressao, true);
//            viewer.setVisible(true);
            
        } catch (JRException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        return byteArray;
	}
	
	public InputStream getTirarPedidoPDF() {
		String jasperPath = "/pdf/jasper/pedidoPDF.jasper";
		InputStream is = TirarPedidoPDF.class.getResourceAsStream(jasperPath);
		
		return is;
	}
	
	public InputStream getLogo() {
		String resourcePath = "/img/sabores.jpg";
		return TirarPedidoPDF.class.getResourceAsStream(resourcePath);
	}
	
	public String getTelefone() 		{ return telefone; 			}
	public String getRazaoEmpresa() 	{ return razaoEmpresa; 		}
	public String getRazaoCliente() 	{ return razaoCliente; 		}
	public List<InnerItens> getItens() 	{ return itens; 			}
	public String getNomeResponsavel() 	{ return nomeResponsavel; 	}
	public String getTotalCompra() 		{ return totalCompra; 		}
	public String getDataCompra() 		{ return dataCompra; 		}
	public String getDataVencimento() 	{ return dataVencimento; 	}
	public String getPrazo() 			{ return prazo; 			}
	public String getDataImpressao() 	{ return dataImpressao; 	}

	public void setTelefone(String telefone) 				{ this.telefone = telefone; 								}
	public void setRazaoEmpresa(String razaoEmpresa) 		{ this.razaoEmpresa = razaoEmpresa; 		}
	public void setRazaoCliente(String razaoCliente) 		{ this.razaoCliente = razaoCliente; 		}
	public void setItens(List<InnerItens> itens) 			{ this.itens = itens; 						}
	public void setNomeResponsavel(String nomeResponsavel) 	{ this.nomeResponsavel = nomeResponsavel; 	}
	public void setTotalCompra(String totalCompra) 			{ this.totalCompra = totalCompra; 			}
	public void setDataCompra(String dataCompra) 			{ this.dataCompra = dataCompra; 			}
	public void setDataVencimento(String dataVencimento) 	{ this.dataVencimento = dataVencimento; 	}
	public void setPrazo(String prazo) 						{ this.prazo = prazo; 						}
	public void setDataImpressao(String dataImpressao) 		{ this.dataImpressao = dataImpressao; 		}

	public class InnerItens {
		
		private String cod;
		private String descricao;
		private String quant;
		private String unitario;
		private String total;
		
		public InnerItens(){}
		
		public InnerItens(String cod, String descricao, String quant, String unitario, String total) {
			setCod(cod);
			setDescricao(descricao);
			setQuant(quant);
			setUnitario(unitario);
			setTotal(total);
		}

		public String getCod() 			{ return cod; 		}
		public String getDescricao() 	{ return descricao; }
		public String getQuant() 		{ return quant; 	}
		public String getUnitario() 	{ return unitario; 	}
		public String getTotal() 		{ return total; 	}
		
		public void setDescricao(String descricao) 	{ this.descricao = descricao; 	}
		public void setCod(String cod) 				{ this.cod = cod; 				}
		public void setQuant(String quant) 			{ this.quant = quant; 			}
		public void setUnitario(String unitario) 	{ this.unitario = unitario; 	}
		public void setTotal(String total) 			{ this.total = total; 			}
	}	
}