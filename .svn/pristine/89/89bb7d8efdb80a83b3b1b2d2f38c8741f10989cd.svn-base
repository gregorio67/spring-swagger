/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : BaseClosableHttpClient.java
 * @Description : 
 *
 * @Author : LGCNS
 * @Since : 2017. 4. 20.
 *
 * @Copyright (c) 2018 EX All rights reserved.
 *-------------------------------------------------------------
 *              Modification Information
 *-------------------------------------------------------------
 * 날짜            수정자             변경사유 
 *-------------------------------------------------------------
 * 2018. 6. 22.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package com.lgcns.api.http;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import org.apache.http.config.Registry;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;

public class HttpClientFactory implements FactoryBean<CloseableHttpClient>, InitializingBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientFactory.class);

	private static final String[] _sslProtocols = {"TLSv1.1", "TLSv1.2"};

	private String[] sslProtocols;
	private boolean ssl;
	private String keyStoreLocation;
	private String trustStoreLocation;
	private String password;
	private int maxPool;
	private int connectTimeout;
	private int readTimeout;
	private int maxRetry;
	private List<HttpRequestInterceptor> firstInterceptors;
	private List<HttpRequestInterceptor> lastInterceptors;

	@Override
	public CloseableHttpClient getObject() throws Exception {

		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		
		/** Add intercepter **/
		if (firstInterceptors != null) {
			for (HttpRequestInterceptor interceptor : firstInterceptors) {
				clientBuilder.addInterceptorFirst(interceptor);
			}
		}

		if (lastInterceptors != null) {
			for (HttpRequestInterceptor interceptor : firstInterceptors) {
				clientBuilder.addInterceptorLast(interceptor);
			}
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Add Interceptor end");
		}

		SSLContext sslContext = null;
		SSLConnectionSocketFactory sslSocketFactory = null;

		/** With certification **/
		if (ssl) {
			sslContext = SSLContextBuilder
	                .create()
	                .loadKeyMaterial(ResourceUtils.getFile(keyStoreLocation), password.toCharArray(), password.toCharArray())
	                .loadTrustMaterial(ResourceUtils.getFile(trustStoreLocation), password.toCharArray())
	                .build();

	        sslSocketFactory = new SSLConnectionSocketFactory(
	        		sslContext,
	        		sslProtocols,
	                null,
	                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		}
		/** Validate certification **/
		else {
			/** Without certificate **/
			sslContext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
			sslSocketFactory = new SSLConnectionSocketFactory(
	        		sslContext,
	        		sslProtocols,
	                null,
	                NoopHostnameVerifier.INSTANCE);

		}	

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
	            .register("http", PlainConnectionSocketFactory.getSocketFactory())
	            .register("https", sslSocketFactory)
	            .build();

        HttpComponentsClientHttpRequestFactory requestFactory =  new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);

        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager( socketFactoryRegistry);
        connMgr.setMaxTotal(maxPool);
        clientBuilder.setConnectionManager(connMgr);
        CloseableHttpClient httpClient = clientBuilder.setRetryHandler(newRetryHandler(maxRetry)).build();

        return httpClient;
	}

	/** 
	 * Retry handler 
	 * @return
	 */
	private static HttpRequestRetryHandler newRetryHandler(final int maxRetry) {
		
		return new HttpRequestRetryHandler() {
			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext httpContext) {
				if (executionCount >= maxRetry) {
					return false;
				}

				if (exception instanceof InterruptedIOException) {
					return false;
				}

				if (exception instanceof UnknownHostException) {
					return false;
				}
				if (exception instanceof SSLException) {
					return false;
				}

				HttpClientContext clientContext = HttpClientContext.adapt(httpContext);
				HttpRequest request = clientContext.getRequest();
				boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
				if (idempotent) {
					return true;
				}
				return false;
			}
		};
	}
	
	
	@Override
	public Class<?> getObjectType() {
		return CloseableHttpClient.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

	public void setSslProtocols(String[] sslProtocols) {
		this.sslProtocols = sslProtocols;
		if (sslProtocols == null) {
			this.sslProtocols = _sslProtocols;
		}
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setMaxPool(int maxPool) {
		if (maxPool == 0) {
			this.maxPool = 100;
		}
		else {
			this.maxPool = maxPool;
		}
	}

	public void setKeyStoreLocation(String keyStoreLocation) {
		this.keyStoreLocation = keyStoreLocation;
	}

	public void setTrustStoreLocation(String trustStoreLocation) {
		this.trustStoreLocation = trustStoreLocation;
	}

	public void setConnectTimeout(int connectTimeout) {
		if (connectTimeout == 0) {
			this.connectTimeout = 5000;
		}
		else {
			this.connectTimeout = connectTimeout;
		}
	}

	public void setReadTimeout(int readTimeout) {
		if (readTimeout == 0) {
			this.readTimeout = 5000;
		}
		else {
			this.readTimeout = readTimeout;
		}
	}

	public void setMaxRetry(int maxRetry) {
		this.maxRetry = maxRetry;
	}

	public void setFirstInterceptors(List<HttpRequestInterceptor> firstInterceptors) {
		this.firstInterceptors = firstInterceptors;
	}

	public void setLastInterceptors(List<HttpRequestInterceptor> lastInterceptors) {
		this.lastInterceptors = lastInterceptors;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(ssl) {
			if (sslProtocols == null || keyStoreLocation == null || trustStoreLocation == null || password == null) {
				LOGGER.error("ssl protocol, key store, trust store and password should not be null, Please check your configuaration");
				throw new RuntimeException("ssl protocol, key store, trust store and password should not be null");
			}
		}
		
	}
}
