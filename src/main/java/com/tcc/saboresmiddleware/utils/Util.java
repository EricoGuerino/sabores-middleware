package com.tcc.saboresmiddleware.utils;

import java.net.URI;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;

public class Util {
	public static BodyBuilder buildResponse(HttpStatus status) {
		BodyBuilder body = ResponseEntity.status(status);
		//body.header("Access-Control-Allow-Origin", "*");
		body.contentType(MediaType.APPLICATION_JSON);
		//body.header("content-type", "application/json;charset=UTF-8");
		return body;
	}
	
	public static BodyBuilder buildResponse(URI uri) {
		BodyBuilder body = ResponseEntity.created(uri);
		//body.header("Access-Control-Allow-Origin", "*");
		body.contentType(MediaType.APPLICATION_JSON);
		return body;
	}
	
	public static String dataDigitos(Date data)
	{
		return formatarDateTime(data).replaceAll("/", "").replaceAll(":", "").replaceAll(" ", "");
	}
	
	public static String formatarDateTime(Date valor) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return sdf.format(valor);
	}
	
	public static String formatarDate(Date valor) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(valor);
	}
	
	public static String formatarTime(Date valor) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(valor);
	}
	
	public static String formatarDoubleParaMoeda(Double valor)
	{
		return NumberFormat.getCurrencyInstance().format(valor);
	}
}
