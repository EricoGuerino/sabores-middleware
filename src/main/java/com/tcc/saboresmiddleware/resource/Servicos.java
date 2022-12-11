package com.tcc.saboresmiddleware.resource;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.tcc.saboresmiddleware.dto.ArquivoDTO;
import com.tcc.saboresmiddleware.utils.Util;

@RestController
@RequestMapping(value = "/servicos")
public class Servicos {
	
	@Autowired
	private com.tcc.saboresmiddleware.services.ServicosFacade servicosFacade;
	
	@RequestMapping(value = "/tirarPedidoPDF/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> obterPedidoPDF(@PathVariable("id") Long id) {
		Map<String, Object> retorno = new HashMap<String,Object>();
		try {
			ArquivoDTO dto = servicosFacade.exportarPedidoPDF(id);
			retorno.put("mensagem", "");
			retorno.put("ok", true);
			retorno.put("pedidoPDF", dto);
			return Util.buildResponse(HttpStatus.OK).body(retorno);
		} catch (Exception e) {
			retorno.put("mensagem", e.getMessage());
			retorno.put("ok", false);
			return Util.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR).body(retorno);
		}
	}
	
	@RequestMapping(value = "/catalogoPDF", method = RequestMethod.POST)
	public ResponseEntity<?> obterCatalogoPDF(@RequestBody Map<String,Object> mapCatalogo) {
		Map<String, Object> retorno = new HashMap<String,Object>();
		try {
			ArquivoDTO dto = servicosFacade.criarCatalogoPDF(mapCatalogo);
			retorno.put("mensagem", "");
			retorno.put("ok", true);
			retorno.put("catalogoPDF", dto);
			return Util.buildResponse(HttpStatus.OK).body(retorno);
		} catch (Exception e) {
			retorno.put("mensagem", e.getMessage());
			retorno.put("ok", false);
			return Util.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR).body(retorno);
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> wsUtils(Map<String,Object> params) {
		Map<String,Object> retorno = null;
		try {
			String method = (String)params.get("method");
			String url = (String)params.get("url");
			Map<String,Object> parametros = (Map<String,Object>)params.get("body");
			
			RestTemplate rest = new RestTemplate();
			if (method.equals("GET")) {
				retorno = (Map<String,Object>)rest.getForObject(url, Object.class);
			} else if (method.equals("POST")) {
				retorno = (Map<String,Object>)rest.postForObject(url, null, Object.class, parametros);
			} else if (method.equals("PUT")) {
				rest.put(url, null, parametros);
			} else if (method.equals("DELETE")) {
				rest.delete(url);
			}
			
			retorno.put("mensagem", "");
			retorno.put("ok", true);
			retorno.put("retorno", retorno);
			return Util.buildResponse(HttpStatus.OK).body(retorno);
		} catch (Exception e) {
			retorno.put("mensagem", e.getMessage());
			retorno.put("ok", false);
			return Util.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR).body(retorno);
		}
	}
	
}
