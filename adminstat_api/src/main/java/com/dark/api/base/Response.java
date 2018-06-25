package com.dark.api.base;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.dark.api.json.serializer.ResponseSerializer;
import com.dark.common.domain.Pager;

@JsonSerialize(using = ResponseSerializer.class)
public class Response implements Serializable {

	private static final long serialVersionUID = 1888689158247849995L;
	
	private String callback;
	
	private int status = 2000;

	private Object result;
	
	private String message;

	private Error error;

	private Pager pager;

	private String debug;

	private Response(Object result) {
		super();
		this.result = result;
	}
	
	private Response(int status,Object result) {
		super();
		this.status = status;
		this.result = result;
	}

	private Response(Object result, Pager pager) {
		super();
		this.result = result;
		this.pager = pager;
	}
	private Response(String callback,int status,Object result) {
		super();
		this.callback=callback;
		this.status = status;
		this.result = result;
	}

	private Response(String callback,Object result, Pager pager) {
		super();
		this.callback=callback;
		this.result = result;
		this.pager = pager;
	}

	private Response(Error error) {
		super();
		this.error = error;
	}

	public static Response success(Object result) {
		return new Response(result);
	}
	
	public static Response warn(Object result) {
		return new Response(3000,result);
	}

	public static Response success(Object result, Pager pager) {
		return new Response(result, pager);
	}

	public static Response error(Error error) {
		return new Response(error);
	}
	
	public static Response error(Object result) {
		return new Response(1000,result);
	}
	public static Response success(String callback,Object result) {
		return new Response(callback,2000,result);
	}
	
	public static Response warn(String callback,Object result) {
		return new Response(callback,3000,result);
	}

	public static Response success(String callback,Object result, Pager pager) {
		return new Response(callback,result, pager);
	}
	
	public static Response error(String callback,Object result) {
		return new Response(callback,1000,result);
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object t) {
		this.result = t;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public int getStatus() {
		if (this.error != null) {
			return this.error.getCode();
		}
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}
	public static Response out(Object o){
		return new Response(0, o);
	}
}
