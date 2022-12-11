package com.tcc.saboresmiddleware.reports;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.tcc.saboresmiddleware.reports.dto.CatalogoDTO;
import com.tcc.saboresmiddleware.reports.dto.ProdutoDTO;
import com.tcc.saboresmiddleware.reports.dto.SumarioDTO;
import com.tcc.saboresmiddleware.services.ServicosFacade;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class CatalogoPDF implements Serializable {

	private static final long serialVersionUID = -8961864675192944584L;
	private List<CatalogoDTO> listaCatalogo;
	private List<SumarioDTO> listaSumario;
	private List<ProdutoDTO> listaPrecos;
	
	public CatalogoPDF() {}
	public CatalogoPDF(List<CatalogoDTO> lista) {
		setListaCatalogo(lista);
	}
	public CatalogoPDF(List<CatalogoDTO> lista, List<SumarioDTO> listaSumario, List<ProdutoDTO> listaProdutos) {
		setListaCatalogo(lista);
		setListaSumario(listaSumario);
	}
	
	public List<CatalogoDTO> getListaCatalogo() { return listaCatalogo; }
	public List<SumarioDTO> getListaSumario() 	{ return listaSumario; 	}
	public List<ProdutoDTO> getListaPrecos() 	{ return listaPrecos; 	}
	
	public void setListaCatalogo(List<CatalogoDTO> listaCatalogo) 	{ this.listaCatalogo = listaCatalogo; 	}
	public void setListaSumario(List<SumarioDTO> listaSumario) 		{ this.listaSumario = listaSumario; 	}
	public void setListaPrecos(List<ProdutoDTO> listaPrecos) 		{ this.listaPrecos = listaPrecos; 		}
	
	public byte[] getCatalogoPDFBytes() throws IOException {
		List<CatalogoPDF> lista = new ArrayList<>();
		lista.add(this);
		
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lista);
        byte[] byteArray = null;
        Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("SUBREPORT_DIR", "/pdf/jasper/");
        String subreport = CatalogoPDF.class.getResource("/pdf/jasper/catalogo.jasper").getPath();
        String sub_subreport = CatalogoPDF.class.getResource("/pdf/jasper/sub_report_catalogo.jasper").getPath();
        parameters.put("SUBREPORT_CATALOGO", subreport);
        parameters.put("SUB_SUBREPORT_CATALOGO", sub_subreport);
        
        FileInputStream backgroundCatalogoFile= new FileInputStream(new File(ServicosFacade.class.getResource("/img/background_catalogo.png").getPath()));
		InputStream backgroundCatalogoImage = new ByteArrayInputStream(IOUtils.toByteArray(backgroundCatalogoFile));
		
		FileInputStream capaCatalogoFile = new FileInputStream(new File(ServicosFacade.class.getResource("/img/capa_catalogo_centralizado.jpg").getPath()));
		InputStream capaCatalogoImage = new ByteArrayInputStream(IOUtils.toByteArray(capaCatalogoFile));
		
        parameters.put("IMAGEM_CAPA", capaCatalogoImage);
        parameters.put("CATALOGO_BACKGROUND", backgroundCatalogoImage);
        
        try {
        	byteArray = JasperRunManager.runReportToPdf(getCatalogoPDFJasper(), parameters, ds);
        } catch (JRException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        return byteArray;
	}
	
	public InputStream getCatalogoPDFJasper() {
		String jasperPath = "/pdf/jasper/catalogo_master.jasper";
		InputStream is = TirarPedidoPDF.class.getResourceAsStream(jasperPath);
		
		return is;
	}
}
