package com.tcc.saboresmiddleware.utils;

import org.springframework.stereotype.Service;

@Service
public class Rotas {
	
	
	public final String URL_BASE_COMPRAS = Dominios.producao ? "" : "http://localhost:8380/";
	public final String URL_BASE_FILTROS = Dominios.producao ? "http://www.erico1916.c41.integrator.host:10500" : "http://localhost:8080/";
	public final String URL_BASE_CLIENTES = Dominios.producao ? "" : "http://localhost:8280/";
	public final String URL_BASE_PRODUTOS = Dominios.producao ? "http://www.erico1916.c41.integrator.host:10499/" : "http://localhost:8180/";
	
	private String obterUrl(String urlBase, String url) {
		return urlBase+url;
	}
	
	public String obterUrlCompras(String url) {
		return obterUrl(URL_BASE_COMPRAS, url);
	}
	
	public String obterUrlFiltros(String url) {
		return obterUrl(URL_BASE_FILTROS, url);
	}
	
	public String obterUrlProdutos(String url) {
		return obterUrl(URL_BASE_PRODUTOS, url);
	}
	
	public String obterUrlClientes(String url) {
		return obterUrl(URL_BASE_CLIENTES, url);
	}
}
