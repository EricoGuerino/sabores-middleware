package com.tcc.saboresmiddleware.resource;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
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
	public ResponseEntity<?> wsUtils(@RequestBody Map<String,Object> params) {
		Map<String,Object> mapRetorno = new HashMap<String,Object>();
		Object retorno = null;
		try {
			String method = (String)params.get("method");
			String url = (String)params.get("url");
			//Map<String,Object> parametros = JSON.parse(String)params.get("body");
			String body = (String)params.get("body");
			Map<String,Object> parametros = null;
			if (body != null && body != "") {
				Gson gson = new Gson();
				parametros = gson.fromJson(body, Map.class);
			}
			RestTemplate rest = new RestTemplate();
			if (method.equals("GET")) {
				retorno = (Object)rest.getForObject(url, Object.class);
			} else if (method.equals("POST")) {
				retorno = (Map<String,Object>)rest.postForObject(url, null, Object.class, parametros);
			} else if (method.equals("PUT")) {
				rest.put(url, null, parametros);
			} else if (method.equals("DELETE")) {
				rest.delete(url);
			}
			
			mapRetorno.put("mensagem", "");
			mapRetorno.put("ok", true);
			mapRetorno.put("retorno", retorno);
			return Util.buildResponse(HttpStatus.OK).body(retorno);
		} catch (Exception e) {
			mapRetorno.put("mensagem", e.getMessage());
			mapRetorno.put("ok", false);
			return Util.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR).body(retorno);
		}
	}
	
}
