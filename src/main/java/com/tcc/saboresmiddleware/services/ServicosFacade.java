package com.tcc.saboresmiddleware.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tcc.saboresmiddleware.dto.ArquivoDTO;
import com.tcc.saboresmiddleware.reports.CatalogoPDF;
import com.tcc.saboresmiddleware.reports.TirarPedidoPDF;
import com.tcc.saboresmiddleware.reports.dto.CatalogoDTO;
import com.tcc.saboresmiddleware.reports.dto.ProdutoCatalogoDTO;
import com.tcc.saboresmiddleware.reports.dto.ProdutoDTO;
import com.tcc.saboresmiddleware.reports.dto.SumarioDTO;
import com.tcc.saboresmiddleware.utils.Dominios;
import com.tcc.saboresmiddleware.utils.Rotas;
import com.tcc.saboresmiddleware.utils.Util;

@Service
public class ServicosFacade {
	
	@Autowired
	private Rotas rotas;
	
	@SuppressWarnings("unchecked")
	public ArquivoDTO exportarPedidoPDF(Long idPedido) throws IOException {
		
		Map<String,Object> compraMap = consumesGetServices(rotas.obterUrlCompras("/compras/"+idPedido.toString()));
		Integer idCliente = (Integer)compraMap.get("idCliente");
		Map<String,Object> clienteMap = consumesGetServices(rotas.obterUrlClientes("/clientes/"+idCliente.toString()));;
		String tipoCliente = (String)clienteMap.get("tipoCliente");
		String nome = tipoCliente.equals("FISICA") 
				? (String)clienteMap.get("nome") 
				: (String)clienteMap.get("nomeFantasia");
		String nomeArquivo = "pedido_pdf_"
				+ nome.replaceAll(" ", "_").toLowerCase() + Util.dataDigitos(new Date()) + ".pdf";
		
		List<TirarPedidoPDF.InnerItens> listaInner = new ArrayList<>();
		TirarPedidoPDF.InnerItens inner = null;
		
		Double subtotal = 0D;
		Double subtotalCompra = 0D;
		List<Map<String,Object>> itens = (List<Map<String,Object>>)compraMap.get("itensCompra");
		for (Map<String, Object> mp : itens) {
			Integer idProduto = (Integer)mp.get("idProduto");
			Integer quantidade = (Integer)mp.get("quantidade");
			Double preco = (Double)mp.get("preco");
			Map<String,Object> produtoMap = consumesGetServices(rotas.obterUrlProdutos("/produtos/"+idProduto.toString()));
			subtotal = quantidade * preco;
			subtotalCompra += subtotal;
			inner = new TirarPedidoPDF().new InnerItens(
					idProduto.toString(), 
					(String)produtoMap.get("nome"), 
					quantidade.toString(), 
					Util.formatarDoubleParaMoeda(preco), 
					Util.formatarDoubleParaMoeda(subtotal)); 
			subtotal = 0D;
			listaInner.add(inner);
		}
		
		
		TirarPedidoPDF pdf = new TirarPedidoPDF(
				Dominios.proprietarioTelefone, 
				Dominios.empresaNome.toUpperCase(), 
				nome.toUpperCase(), 
				listaInner, 
				Dominios.proprietarioNome.toUpperCase(), 
				Util.formatarDoubleParaMoeda(subtotalCompra), 
				(String)compraMap.get("dataFmt"), 
				(String)compraMap.get("dataFmt"), 
				Dominios.prazo.toString(), 
				Util.formatarDateTime(new Date()));
		
		ArquivoDTO dto = new ArquivoDTO();
		dto.setFileName(nomeArquivo);
		try {
			dto.setArquivo(pdf.getTirarPedidoPDFBytes());
		} catch (Exception e) {
			e.getMessage();
		}
//		getResposta().setContentType("application/pdf");
//		getResposta().setHeader("Content-disposition", "attachment;filename="+getFileNamePedido());
//		getResposta().getOutputStream().write(pdf.getTirarPedidoPDFBytes());
//		getResposta().getOutputStream().flush();
		return dto;
	}
	
	@SuppressWarnings("unchecked")
	public ArquivoDTO criarCatalogoPDF(Map<String,Object> mapProdutos) throws IOException {
		CatalogoPDF catalogo = new CatalogoPDF();
		List<CatalogoDTO> listaCatalogo = new ArrayList<>();
		List<Integer> listaProdutos = mapProdutos.get("produtos") != null ? (List<Integer>)mapProdutos.get("produtos") : new ArrayList<Integer>();
		List<Integer> listaFabricantes = mapProdutos.get("fabricantes") != null ? (List<Integer>)mapProdutos.get("fabricantes") : new ArrayList<Integer>();
		List<Integer> listaCategorias = mapProdutos.get("categorias") != null ? (List<Integer>)mapProdutos.get("categorias") : new ArrayList<Integer>();
		Integer precoMinimo = mapProdutos.get("precoMinimo") != null ? (Integer)mapProdutos.get("precoMinimo") : 0;
		Integer precoMaximo = mapProdutos.get("precoMaximo") != null ? (Integer)mapProdutos.get("precoMaximo") : 0;
		
		Boolean filtrarProdutos = !listaProdutos.isEmpty();
		Boolean filtrarFabricantes = !listaProdutos.isEmpty();
		Boolean filtrarCategorias = !listaProdutos.isEmpty();
		Boolean filtrarPrecoMinimo = precoMinimo > 0;
		Boolean filtrarPrecoMaximo = precoMaximo > 0;
		
		List<Map<String,Object>> produtos = consumesGetServicesLista(rotas.obterUrlProdutos("/produtos"));
		Set<Integer> produtosManter = new HashSet<Integer>();
		
		for (Map<String,Object> mp : produtos) {
			Integer idProduto = (Integer)mp.get("id");
			Integer idFabricante = (Integer)mp.get("fabricante");
			List<Integer> categorias = (List<Integer>)mp.get("categorias");
			Double preco = (Double)mp.get("preco");
			if (filtrarProdutos) {
				for (Integer p : listaProdutos) {
					if (idProduto.equals(p)) {
						produtosManter.add(idProduto);
					}
				}
			}
			
			if (filtrarFabricantes) {
				for (Integer f : listaFabricantes) {
					if (idFabricante.equals(f)) {
						produtosManter.add(idProduto);
					}
				}
			}
			
			if (filtrarCategorias) {
				for (Integer c : listaCategorias) {
					if (categorias.contains(c)) {
						produtosManter.add(idProduto);
					}
				}
			}
			
			if (filtrarPrecoMinimo) {
				if (preco >= precoMinimo) {
					produtosManter.add(idProduto);
				}
			}
			
			if (filtrarPrecoMaximo) {
				if (preco <= precoMaximo) {
					produtosManter.add(idProduto);
				}
			}
		}
		
		List<Map<String,Object>> mapCategorias = consumesGetServicesLista(rotas.obterUrlFiltros("/categorias"));
		List<SumarioDTO> listaSumario = new ArrayList<SumarioDTO>();
		List<ProdutoDTO> listaPrecos = new ArrayList<ProdutoDTO>();
		
		int indexPrecos = 1;
		int indexSumario = 1;
		int somatorioPaginas = 2;
		for (Map<String,Object> fab : consumesGetServicesLista(rotas.obterUrlFiltros("fabricantes"))) {
			Integer id = (Integer)fab.get("id");
			String fabricante = (String)fab.get("descricao");
			CatalogoDTO catalogoDTO = new CatalogoDTO();
			List<ProdutoCatalogoDTO> produtosCatalogo = new ArrayList<>();
			ProdutoCatalogoDTO produtoDTO = null;
			
			int count = 0;
			int qtdProdutosFabricante = 0;
			for (Map<String,Object> mp : produtos) {
				
				Integer idProduto = (Integer)mp.get("id");
				Integer idFabricante = (Integer)mp.get("fabricante");
				List<Integer> categorias = (List<Integer>)mp.get("categorias");
				String nome = (String)mp.get("nome");
				String descricao = (String)mp.get("descricao");
				
				@SuppressWarnings("unused")
				Double preco = (Double)mp.get("preco");
				@SuppressWarnings("unused")
				Double peso = (Double)mp.get("peso");
				
				if (!produtosManter.isEmpty() && !produtosManter.contains(idProduto)) {
					continue;
				}
				
				if (!id.equals(idFabricante)) {
					continue;
				}
				
				Map<String,Object> mapImagemProduto = consumesGetServices(rotas.obterUrlProdutos("imagens/produto/"+idProduto));
				String imagemBase64 = (String)mapImagemProduto.get("dadosBase64");
				byte[] dados = Base64.decodeBase64(imagemBase64);
				
				Boolean semLactose = Boolean.FALSE;
				Boolean semGluten = Boolean.FALSE;
				Boolean vegano = Boolean.FALSE;
				
				for (Integer _cat : categorias) {
					Map<String,Object> categAtual = null;
					for (Map<String,Object> mapCat : mapCategorias) {
						if (_cat.equals((Integer)mapCat.get("id"))) {
							categAtual = mapCat;
						}
					}
					String descricaoCategoria = (String)categAtual.get("descricao");
					if (descricaoCategoria.toLowerCase().contains("lactose")) {
						semLactose = Boolean.TRUE;
					}
					if (descricaoCategoria.toLowerCase().contains("gluten")
							|| descricaoCategoria.toLowerCase().contains("glúten")) {
						semGluten = Boolean.TRUE;
					}
					if (descricaoCategoria.toLowerCase().contains("vegan")) {
						vegano = Boolean.TRUE;
					}
					
				}
				
				InputStream imagemProduto = new ByteArrayInputStream(dados);
				FileInputStream fileSemLactose = new FileInputStream(new File(ServicosFacade.class.getResource("/img/iconeSemLactose.jpg").getPath()));
				FileInputStream fileSemGluten = new FileInputStream(new File(ServicosFacade.class.getResource("/img/iconeSemGluten.jpg").getPath()));
				FileInputStream fileVegano= new FileInputStream(new File(ServicosFacade.class.getResource("/img/iconeVegano.jpg").getPath()));
				
				InputStream iconeSemLactose = new ByteArrayInputStream(IOUtils.toByteArray(fileSemLactose));	
				InputStream iconeSemGluten = new ByteArrayInputStream(IOUtils.toByteArray(fileSemGluten));
				InputStream iconeVegano = new ByteArrayInputStream(IOUtils.toByteArray(fileVegano));
				if (count == 0) {
					produtoDTO = new ProdutoCatalogoDTO();
					produtoDTO.setNomeProduto1(nome);
					produtoDTO.setDescricaoProduto1(descricao);
					produtoDTO.setImagemProduto1(imagemProduto);
					produtoDTO.setSemLactose1(semLactose ? iconeSemLactose : null);
					produtoDTO.setSemGluten1(semGluten ? iconeSemGluten : null);							
					produtoDTO.setVegano1(vegano ? iconeVegano : null);
					produtosCatalogo = new ArrayList<ProdutoCatalogoDTO>();
					produtosCatalogo.add(produtoDTO);
					catalogoDTO = new CatalogoDTO();
					catalogoDTO.setEmpresa(fabricante);
					catalogoDTO.setProdutos(produtosCatalogo);
					listaCatalogo.add(catalogoDTO);
				} else if (count == 1) {
					produtoDTO.setNomeProduto2(nome);
					produtoDTO.setDescricaoProduto2(descricao);
					produtoDTO.setImagemProduto2(imagemProduto);
					produtoDTO.setSemLactose2(semLactose ? iconeSemLactose : null);
					produtoDTO.setSemGluten2(semGluten ? iconeSemGluten : null);							
					produtoDTO.setVegano2(vegano ? iconeVegano : null);							
				} else if (count == 2) {
					produtoDTO.setNomeProduto3(nome);
					produtoDTO.setDescricaoProduto3(descricao);
					produtoDTO.setImagemProduto3(imagemProduto);
					produtoDTO.setSemLactose3(semLactose ? iconeSemLactose : null);
					produtoDTO.setSemGluten3(semGluten ? iconeSemGluten : null);							
					produtoDTO.setVegano3(vegano ? iconeVegano : null);							
				}
				
				iconeVegano.close();
				iconeSemGluten.close();
				iconeSemLactose.close();
				imagemProduto.close();
				count++;
				if (count == 3) {
					count = 0;
				}
				
				qtdProdutosFabricante++;
				ProdutoDTO precoDTO = new ProdutoDTO();
				
				precoDTO.setIndex(indexPrecos <= 9 ? "0"+String.valueOf(indexPrecos) : String.valueOf(indexPrecos));
				precoDTO.setNome(nome);
				NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("PT", "BR"));
				precoDTO.setPreco(nf.format(preco));
				listaPrecos.add(precoDTO);
				indexPrecos++;
			}
			
			if (qtdProdutosFabricante > 0) {
				SumarioDTO sumarioDTO = new SumarioDTO();
				sumarioDTO.setIndex(indexSumario <= 9 ? "0"+String.valueOf(indexSumario) : String.valueOf(indexSumario));
				sumarioDTO.setEmpresa(fabricante);
				int qtdPaginas = qtdProdutosFabricante % 3 == 0 
						? qtdProdutosFabricante / 3 
						: (qtdProdutosFabricante / 3)+(1);
				Boolean primeiroItem = somatorioPaginas == 2;
				if (primeiroItem) {
					sumarioDTO.setPagina("03");
					somatorioPaginas = 3+qtdPaginas;
				} else {
					somatorioPaginas += qtdPaginas;
					sumarioDTO.setPagina(somatorioPaginas <= 9 ? "0"+String.valueOf(somatorioPaginas) : String.valueOf(somatorioPaginas));					
				}
				listaSumario.add(sumarioDTO);
				indexSumario++;
			}

		}
		
		SumarioDTO sumarioDTO = new SumarioDTO();
		sumarioDTO.setEmpresa("Tabela de Preços");
		sumarioDTO.setIndex(indexSumario <= 9 ? "0"+String.valueOf(indexSumario) : String.valueOf(indexSumario));
		sumarioDTO.setPagina(somatorioPaginas <= 9 ? "0"+String.valueOf(somatorioPaginas) : String.valueOf(somatorioPaginas));
		listaSumario.add(sumarioDTO);
		
		catalogo.setListaSumario(listaSumario);
		catalogo.setListaCatalogo(listaCatalogo);
		catalogo.setListaPrecos(listaPrecos);
		String nomeArquivo = "catalogo_" + Util.dataDigitos(new Date()) + ".pdf";
		
		ArquivoDTO dto = new ArquivoDTO();
		dto.setFileName(nomeArquivo);
		try {
			dto.setArquivo(catalogo.getCatalogoPDFBytes());
		} catch (Exception e) {
			e.getMessage();
		}
//		getResposta().setContentType("application/pdf");
//		getResposta().setHeader("Content-disposition", "attachment;filename="+getFileNamePedido());
//		getResposta().getOutputStream().write(pdf.getTirarPedidoPDFBytes());
//		getResposta().getOutputStream().flush();
		return dto;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> consumesGetServices(String url) {
		RestTemplate rest = new RestTemplate();
		return (Map<String,Object>)rest.getForObject(url, Object.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> consumesGetServicesLista(String url) {
		RestTemplate rest = new RestTemplate();
		return (List<Map<String,Object>>)rest.getForObject(url, Object.class);
	}
}
