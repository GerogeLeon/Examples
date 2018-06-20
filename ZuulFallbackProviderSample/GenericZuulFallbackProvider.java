package com.example.Zullfallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

public class GenericZuulFallbackProvider implements ZuulFallbackProvider {
	
	private String responseBody = "{\"message\":\"Service Unavailable. Please try after sometime\"}";
	private HttpHeaders headers = null;
	private String route = null;
	private int rawStatusCode = 503;
	private HttpStatus statusCode = HttpStatus.SERVICE_UNAVAILABLE;
	private String statusText = "Service Unavailable";

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public int getRawStatusCode() {
		return rawStatusCode;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public void setRawStatusCode(int rawStatusCode) {
		this.rawStatusCode = rawStatusCode;
	}

	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}

	public void setHeaders(HttpHeaders headers) {
		this.headers = headers;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	@Override
	public ClientHttpResponse fallbackResponse() {
		return new ClientHttpResponse(){

			@Override
			public InputStream getBody() throws IOException {
				if (responseBody == null) 
					responseBody ="{\"message\":\"Service Unavailable. Please try after sometime\"}";
				return new ByteArrayInputStream(responseBody.getBytes());
			}

			@Override
			public HttpHeaders getHeaders() {
				if (headers == null) { 
					headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
				}
				return headers;
			}

			@Override
			public void close() {
				
			}

			@Override
			public int getRawStatusCode() throws IOException {
				return rawStatusCode;
			}

			@Override
			public HttpStatus getStatusCode() throws IOException {
				if (statusCode == null) 
					statusCode = HttpStatus.SERVICE_UNAVAILABLE;
				return statusCode;
			}

			@Override
			public String getStatusText() throws IOException {
				if (statusText == null)
					statusText = "Service Unavailable";
				return statusText;
			}
			
		};
	}

	@Override
	public String getRoute() {
		if (route == null) 
			route = "route";
		return route;
	}

}
