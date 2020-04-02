package com.lgcns.api.helper;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.SSLException;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;

import com.lgcns.api.base.BaseMap;
import com.lgcns.api.util.JsonUtil;
import com.lgcns.api.util.XmlUtil;

public class HttpHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelper.class);

	/**
	 * Call post with name and value pair
	 * 
	 * @param URL
	 * @param param
	 * @param responseType
	 * @return
	 * @throws Exception
	 */
	public static <V, T> T post(String URL, V param, final Class<T> responseType) throws Exception {

		/** Convert param to name and value pair **/
		List<NameValuePair> sendParam = null;
		UrlEncodedFormEntity formEntity = null;

		if (param != null) {
			sendParam = toValuePair(param);
			formEntity = new UrlEncodedFormEntity(sendParam, Consts.UTF_8);
		}

		/** Http post **/
		HttpPost httpPost = new HttpPost(URL);
		httpPost.setHeader("ContentType", ContentType.APPLICATION_FORM_URLENCODED.toString());

		if (formEntity != null) {
			httpPost.setEntity(formEntity);
		}

		RetryTemplate retryTemplate = BeanHelper.getBean("retryTemplate");

		/** Get retry count **/
		Properties system = BeanHelper.getBean("system");
		final int maxRetry = Integer.parseInt((String) system.get("http.max.retry.count"));

		T response = retryTemplate.execute(new RetryCallback<T, Exception>() {

			@Override
			public T doWithRetry(RetryContext context) throws Exception {
				CloseableHttpClient httpClient = BeanHelper.getBean("httpClientFactory");
				HttpEntity responseEntity = null;
				/** Response handler **/		
				ResponseHandler<HttpEntity> responseHandler = newResponseHandler();
				try (CloseableHttpResponse httpResponse =  httpClient.execute(httpPost)) {
					responseEntity = responseHandler.handleResponse(httpResponse);
					T response = null;
					if (responseEntity != null) {
						ContentType contentType = null;
						contentType = ContentType.getOrDefault(responseEntity);
						response =  getResponse(responseEntity, responseType, contentType);						
					}
					return response;
				} catch (Exception ex) {
					String errmsg = String.format("Retry to call service :: [%s]] [%d]/[%d] times", URL,
							context.getRetryCount() + 1, maxRetry);
					LOGGER.error("ERROR :: {}", errmsg);
					throw new ConnectException(errmsg);
				}
				finally {
					if (httpClient != null) httpClient.close();
				}
			}
		});
				
		return response;
	}

	/**
	 * Call post JSON
	 * 
	 * @param URL
	 * @param param
	 * @param contentType
	 * @param responseType
	 * @return
	 * @throws Exception
	 */
	public static <T> T post(String URL, String param, ContentType contentType, Class<T> responseType) throws Exception {

		HttpPost httpPost = new HttpPost(URL);
		httpPost.setHeader("contentType", contentType.toString());

		/** Check request parameters **/
		if (param != null) {
			StringEntity entity = new StringEntity(param);
			entity.setContentType(contentType.toString());
			httpPost.setEntity(entity);
		}

		RetryTemplate retryTemplate = BeanHelper.getBean("retryTemplate");

		/** Retry count **/
		Properties system = BeanHelper.getBean("system");
		final int maxRetry = Integer.parseInt((String) system.get("http.max.retry.count"));
		T response = retryTemplate.execute(new RetryCallback<T, Exception>() {

			@Override
			public T doWithRetry(RetryContext context) throws Exception {
				CloseableHttpClient httpClient = BeanHelper.getBean("httpClientFactory");
				HttpEntity responseEntity = null;
				/** Response handler **/		
				ResponseHandler<HttpEntity> responseHandler = newResponseHandler();
				try (CloseableHttpResponse httpResponse =  httpClient.execute(httpPost)) {
					responseEntity = responseHandler.handleResponse(httpResponse);
					T response = null;
					if (responseEntity != null) {
						ContentType contentType = null;
						contentType = ContentType.getOrDefault(responseEntity);
						response =  getResponse(responseEntity, responseType, contentType);						
					}
					return response;
				} catch (Exception ex) {
					String errmsg = String.format("Retry to call service :: [%s]] [%d]/[%d] times", URL,
							context.getRetryCount() + 1, maxRetry);
					LOGGER.error("ERROR :: {}", errmsg);
					throw new ConnectException(errmsg);
				}
				finally {
					if (httpClient != null) httpClient.close();
				}
			}
		});

		return response;
	}

	/**
	 * 
	 * @param URL
	 * @param param
	 * @param filePath
	 * @param responseType
	 * @return
	 * @throws Exception
	 */
	public static <V, T> T multiPartPost(String URL, V param, List<String> files, final Class<T> responseType)
			throws Exception {

		/** Multipart builder **/
		MultipartEntityBuilder builder = null;
		if (param != null && files != null) {
			builder = toMultiPart(param, files);
		}

		HttpPost httpPost = new HttpPost(URL);
		httpPost.setHeader("ContentType", ContentType.MULTIPART_FORM_DATA.toString());
		/** Set multiparts to HTTP post **/
		if (builder != null) {
			HttpEntity multipart = builder.build();
			httpPost.setEntity(multipart);
		}

		/** Get HttpClient and RestTemplate **/
		RetryTemplate retryTemplate = BeanHelper.getBean("retryTemplate");

		/** Retry count **/
		Properties system = BeanHelper.getBean("system");
		final int maxRetry = Integer.parseInt((String) system.get("http.max.retry.count"));

		T response = retryTemplate.execute(new RetryCallback<T, Exception>() {

			@Override
			public T doWithRetry(RetryContext context) throws Exception {
				CloseableHttpClient httpClient = BeanHelper.getBean("httpClientFactory");
				HttpEntity responseEntity = null;
				/** Response handler **/		
				ResponseHandler<HttpEntity> responseHandler = newResponseHandler();
				try (CloseableHttpResponse httpResponse =  httpClient.execute(httpPost)) {
					responseEntity = responseHandler.handleResponse(httpResponse);
					T response = null;
					if (responseEntity != null) {
						ContentType contentType = null;
						contentType = ContentType.getOrDefault(responseEntity);
						response =  getResponse(responseEntity, responseType, contentType);						
					}
					return response;
				} catch (Exception ex) {
					String errmsg = String.format("Retry to call service :: [%s]] [%d]/[%d] times", URL,
							context.getRetryCount() + 1, maxRetry);
					LOGGER.error("ERROR :: {}", errmsg);
					throw new ConnectException(errmsg);
				}
				finally {
					if (httpClient != null) httpClient.close();
				}
			}
		});

		return response;
	}

	/**
	 * HTTP get
	 * 
	 * @param URL
	 * @param param
	 * @param contentType
	 * @param responseType
	 * @return
	 * @throws Exception
	 */
	public static <V, T> T get(String URL, V param, Class<T> responseType) throws Exception {

		HttpGet httpGet = new HttpGet(URL);
		httpGet.setHeader("contentType", ContentType.TEXT_PLAIN.toString());

		List<NameValuePair> sendParam = null;

		if (param != null) {
			sendParam = toValuePair(param);
		}

		/** Set parameter for GET method **/
		if (sendParam != null) {
			URI uri = new URIBuilder(httpGet.getURI()).addParameters(sendParam).build();
			httpGet.setURI(uri);
		}

		RetryTemplate retryTemplate = BeanHelper.getBean("retryTemplate");

		Properties system = BeanHelper.getBean("system");
		final int maxRetry = Integer.parseInt((String) system.get("http.max.retry.count"));

		
		T response = retryTemplate.execute(new RetryCallback<T, Exception>() {
			@Override
			public T doWithRetry(RetryContext context) throws Exception {

				/** Response handler **/		
				ResponseHandler<HttpEntity> responseHandler = newResponseHandler();
				CloseableHttpClient httpClient = BeanHelper.getBean("httpClientFactory");
				HttpEntity responseEntity = null;
				try(CloseableHttpResponse httpResponse =  httpClient.execute(httpGet)) {
					responseEntity = responseHandler.handleResponse(httpResponse);
					ContentType contentType = null;
					contentType = ContentType.getOrDefault(responseEntity);
					return getResponse(responseEntity, responseType, contentType);
				} catch (Exception ex) {
					String errmsg = String.format("Retry to call service :: [%s]] [%d]/[%d] times", URL,
							context.getRetryCount() + 1, maxRetry);
					LOGGER.error("ERROR :: {}", errmsg);
					throw new ConnectException(errmsg);
				}
				finally {
					httpClient.close();
				}
			}
		});


		return response;
	}

	/**
	 * Return response handler
	 * 
	 * @return
	 * @throws Exception
	 */
	public static ResponseHandler<HttpEntity> newResponseHandler() throws Exception {
		return new ResponseHandler<HttpEntity>() {
			@Override
			public HttpEntity handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity responseEntity = response.getEntity();
					return responseEntity;
				} else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}
		};
	}

	/**
	 * Return HTTP response
	 * 
	 * @param responseEntity
	 * @param responseType
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getResponse(HttpEntity responseEntity, Class<T> responseType, ContentType contentType)
			throws Exception {
		T response = null;

		/** If response type is String **/
		if ((T) responseType instanceof String) {
			response = (T) EntityUtils.toString(responseEntity);
		} else {
			/** content type is JSON **/
			if (contentType.getMimeType().contains("json")) {
				response = JsonUtil.toObject(EntityUtils.toString(responseEntity), responseType);
			}
			/** Contents type is XML **/
			else if (contentType.getMimeType().contains("xml")) {
				response = XmlUtil.toObject(EntityUtils.toString(responseEntity), responseType);
			}
		}

		return response;
	}

	/**
	 * Return retry handler
	 * 
	 * @return
	 */
	public static HttpRequestRetryHandler newRetryHandler() throws Exception {
		Properties system = BeanHelper.getBean("system");
		final int maxRetry = Integer.parseInt((String) system.get("http.max.retry.count"));
		return newRetryHandler(maxRetry);
	}

	/**
	 * 
	 * @param maxRetry
	 * @return
	 */
	public static HttpRequestRetryHandler newRetryHandler(final int maxRetry) {

		return new HttpRequestRetryHandler() {

			@Override
			public boolean retryRequest(IOException exception, int execCount, HttpContext httpcontext) {
				if (execCount >= maxRetry) {
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

				HttpClientContext clientContext = HttpClientContext.adapt(httpcontext);
				HttpRequest request = clientContext.getRequest();
				boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
				if (idempotent) {
					return true;
				}
				return false;
			}
		};
	}

	/**
	 * Return name value pair from object
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static List<NameValuePair> toValuePair(Object object) throws Exception {

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		if (object instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) object;
			for (String key : map.keySet()) {
				param.add(new BasicNameValuePair(key, String.valueOf(map.get(key))));
			}
		}
		/** if BaseMap **/
		else if (object instanceof BaseMap) {
			BaseMap map = (BaseMap) object;
			for (Object key : map.keySet()) {
				param.add(new BasicNameValuePair((String) key, map.getString((String) key)));
			}
		}
		/** if value object **/
		else {
			Field[] fields = FieldUtils.getAllFields(object.getClass());
			for (Field field : fields) {
				String value = String.valueOf(FieldUtils.readField(field, object, true));
				param.add(new BasicNameValuePair(field.getName(), value));
			}
		}
		return param;
	}

	@SuppressWarnings("unchecked")
	public static MultipartEntityBuilder toMultiPart(Object object, List<String> files) throws Exception {

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		if (object instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) object;
			for (String key : map.keySet()) {
				builder.addTextBody(key, String.valueOf(map.get(key)));
			}
		}
		/** if BaseMap **/
		else if (object instanceof BaseMap) {
			BaseMap map = (BaseMap) object;
			for (Object key : map.keySet()) {
				builder.addTextBody((String) key, map.getString((String) key));
			}
		}
		/** if value object **/
		else {
			Field[] fields = FieldUtils.getAllFields(object.getClass());
			for (Field field : fields) {
				String value = String.valueOf(FieldUtils.readField(field, object, true));
				builder.addTextBody(field.getName(), value);
			}
		}

		/** Add File **/
		for (String file : files) {
			Path path = Paths.get(file);
			builder.addBinaryBody(path.getFileName().toString(), new File(file), ContentType.APPLICATION_OCTET_STREAM,
					file);
		}
		return builder;
	}
}
