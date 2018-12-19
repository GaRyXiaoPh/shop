package cn.kt.mall.shop.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	private static final String CHARSET = "UTF-8";
	private static final int STATUS_200 = 200;
	private static final String TEXT_PLAIN = "text/plain";
	private static final String FORM_TYPE = "multipart";
	private static final ContentType CONTENT_TYPE = ContentType.create(TEXT_PLAIN, CHARSET);

	public static RequestConfig getRequestConfig() {
		return getRequestConfig(15000, 10000, 10000);
	}

	public static RequestConfig getRequestConfig(int contTimeout, int reqTimeout, int socketTimeout) {
		return RequestConfig.custom().setConnectTimeout(contTimeout).setConnectionRequestTimeout(reqTimeout)
				.setSocketTimeout(socketTimeout).build();
	}

	public static RequestConfig getShortRequestConf() {
		return getRequestConfig(10000, 5000, 5000);
	}

	/**
	 * 请求返回
	 * 
	 * @param httpResponse
	 * @return
	 */
	public static String getHttpResonse(CloseableHttpResponse httpResponse, String charset) {
		String respContent = "";
		try {
			if (httpResponse.getStatusLine().getStatusCode() == STATUS_200) {
				respContent = EntityUtils.toString(httpResponse.getEntity(), charset);
			} else {
				logger.info("http response:{}, body:{}", httpResponse,
						EntityUtils.toString(httpResponse.getEntity(), charset));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				httpResponse.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return respContent;
	}

	/**
	 * @param url
	 * @return
	 * @方法功能描述 发送一个GET请求并取得返回内容
	 */
	public static String get(String url, List<Header> headers) {
		String respContent = "";
		// 创建默认的httpClient实例
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// 用get方法发送http请求
			HttpGet httpGet = new HttpGet(url);
			httpGet.setConfig(getRequestConfig());
			// httpHead参数
			if (isNotEmpty(headers)) {
				for (Header header : headers) {
					httpGet.setHeader(header);
				}
			}
			// 发送get请求
			CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
			// 消息返回
			respContent = getHttpResonse(httpResponse, CHARSET);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return respContent;
	}

	/**
	 * 通过http读取网络数据
	 */
	public static InputStream getInputStream(String url) {
		InputStream is = null;
		try {
			URL urlGet = new URL(url);
			HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod("GET"); // 必须是get方式请求
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
			http.connect();
			// 获取文件转化为byte流
			is = http.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}

	/**
	 * @param url
	 *            请求地址
	 * @param headers
	 *            请求需要包含的http头信息
	 * @param params
	 *            请求需要提交的参数信息
	 * @param files
	 *            需要上传的文件对象
	 * @return 请求地址返回的内容
	 * @方法功能描述 发送httpPost请求并上传文件
	 */
	public static String post(String url, List<Header> headers, List<NameValuePair> params, List<String> inputFileName,
			List<File> files) {
		String respContent = "";
		// 创建默认的httpClient实例
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// 用post方法发送http请求
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(getRequestConfig());
			// httpHead参数
			if (isNotEmpty(headers)) {
				for (Header header : headers) {
					httpPost.setHeader(header);
				}
			}
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			// 参数列表
			if (isNotEmpty(params)) {
				for (NameValuePair param : params) {
					builder.addTextBody(param.getName(), param.getValue(), CONTENT_TYPE);
				}
			}
			// 上传文件列表
			if (isNotEmpty(inputFileName) && isNotEmpty(files)) {
				for (int i = 0; i < files.size(); i++) {
					builder.addBinaryBody(inputFileName.get(i), files.get(i), ContentType.DEFAULT_BINARY,
							files.get(i).getName());
				}
			} else if (isNotEmpty(files)) {
				for (File file : files) {
					builder.addBinaryBody("image", file, ContentType.DEFAULT_BINARY, file.getName());
				}
			}
			HttpEntity entity = builder.build();
			httpPost.setEntity(entity);
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
			respContent = getHttpResonse(httpResponse, CHARSET);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return respContent;
	}

	/**
	 * @param url
	 *            请求地址
	 * @param headers
	 *            请求需要包含的http头信息
	 * @param params
	 *            请求需要提交的参数信息
	 * @param inputFiles
	 *            需要上传的文件对象
	 * @return 请求地址返回的内容
	 * @方法功能描述 发送httpPost请求并上传文件
	 */
	public static String post(String url, List<Header> headers, List<NameValuePair> params,
			LinkedHashMap<String, InputStream> inputFiles) {
		String respContent = "";
		// 创建默认的httpClient实例
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// 用post方法发送http请求
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(getRequestConfig());
			// httpHead参数
			if (isNotEmpty(headers)) {
				for (Header header : headers) {
					httpPost.setHeader(header);
				}
			}
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			// 参数列表
			if (isNotEmpty(params)) {
				for (NameValuePair param : params) {
					builder.addTextBody(param.getName(), param.getValue(), CONTENT_TYPE);
				}
			}
			// 上传文件列表
			if (inputFiles != null && !inputFiles.isEmpty()) {
				for (Entry<String, InputStream> e : inputFiles.entrySet()) {
					String fileFullName = e.getKey();
					int point = fileFullName.indexOf(".");
					String fileName = point > 0 ? fileFullName.substring(0, point) : fileFullName;
					builder.addBinaryBody(fileName, e.getValue(), ContentType.DEFAULT_BINARY, fileFullName);
				}
			}
			HttpEntity entity = builder.build();
			httpPost.setEntity(entity);
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
			respContent = getHttpResonse(httpResponse, CHARSET);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return respContent;
	}

	/***
	 * @param url
	 *            请求URL
	 * @param headers
	 *            头信息
	 * @param params
	 *            参数信息
	 * @param msg
	 *            body体
	 * @param charSet
	 *            编码
	 * @return
	 */
	public static String post(String url, List<Header> headers, List<NameValuePair> params, String msg,
			String charSet) {
		String respContent = "";
		// 创建默认的httpClient实例
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// 用post方法发送http请求
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(getRequestConfig());
			// httpHead参数
			if (isNotEmpty(headers)) {
				for (Header header : headers) {
					httpPost.setHeader(header);
				}
			}
			httpPost.setEntity(new StringEntity(msg, charSet));
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
			respContent = getHttpResonse(httpResponse, charSet);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return respContent;
	}

	/**
	 * httpClient post 请求
	 * 
	 * @param url
	 *            请求URL
	 * @param headers
	 *            头信息
	 * @param msg
	 *            body体消息
	 * @param charSet
	 *            编码
	 * @return
	 */
	public static String post(String url, List<Header> headers, String msg, String charSet) {
		List<NameValuePair> params = null;
		return post(url, headers, params, msg, charSet);
	}

	/**
	 * httpClient post请求,并上传单个文件
	 * 
	 * @param url
	 *            请求URL地址
	 * @param params
	 *            参数，key=value
	 * @param inputFileName：
	 *            文件域
	 * @param file
	 *            对应的文件名
	 * @return 服务器返回信息
	 */
	public static String post(String url, List<NameValuePair> params, String inputFileName, File file) {
		List<String> listFileName = new ArrayList<String>();
		List<File> files = new ArrayList<File>();
		listFileName.add(inputFileName);
		files.add(file);
		return post(url, null, params, listFileName, files);
	}

	/**
	 * httpClient post请求
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param headers
	 *            hearder头数据，list格式
	 * @param params
	 *            参数，list格式
	 * @param files
	 *            上传文件
	 * @return 服务器返回信息
	 */
	public static String post(String url, List<Header> headers, List<NameValuePair> params, List<File> files) {
		return post(url, headers, params, null, files);
	}

	/**
	 * httpClient post请求
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param headers
	 *            hearder头数据，list格式
	 * @param params
	 *            参数，list格式
	 * @return 服务器返回信息
	 */
	public static String post(String url, List<Header> headers, List<NameValuePair> params) {
		List<String> str = null;
		List<File> files = null;
		return post(url, headers, params, str, files);
	}

	/**
	 * httpClient get请求
	 * 
	 * @param url
	 *            请求的URL地址
	 * @return 服务器返回信息
	 */
	public static String get(String url) {
		List<Header> headers = null;
		return get(url, headers);
	}

	/**
	 * @description get请求
	 */
	public static String get(String url, Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		if (url.contains("?")) {
			url = url + sb.toString().substring(0, sb.toString().length() - 1);
		} else {
			url = url + "?" + sb.toString().substring(0, sb.toString().length() - 1);
		}
		return get(url);
	}

	/**
	 * @param list
	 */
	private static boolean isNotEmpty(List<?> list) {
		if (list == null || list.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public static String logBuffer(HttpServletRequest request) {
		StringBuilder msg = new StringBuilder("");
		Map<String, String[]> params = request.getParameterMap();
		for (String name : params.keySet()) {
			String[] values = params.get(name);
			msg.append("{").append(name).append("=");
			for (int i = 0; i < values.length; i++) {
				String value = values[i];
				msg.append(value);
				if (i < values.length) {
					msg.append("[|]");
				}
			}
			msg.append("}");
		}
		return msg.toString();
	}

	/**
	 * 通过Http方式上传文件
	 * @description: 通过post的方式上传文件
	 */
	public static String uploadFileByPost(String url, File file, String fileName, List<NameValuePair> params) {
		{
			String respContent = "";
			// 创建默认的httpClient实例
			CloseableHttpClient httpClient = HttpClients.createDefault();
			try {
				// 用post方法发送http请求
				HttpPost httpPost = new HttpPost(url);
				httpPost.setConfig(getRequestConfig());
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
				if (isNotEmpty(params)) {
					for (NameValuePair param : params) {
						builder.addTextBody(param.getName(), param.getValue(), CONTENT_TYPE);
					}
				}
				// 上传文件
				builder.addBinaryBody("file", file, ContentType.DEFAULT_BINARY, fileName);
				HttpEntity entity = builder.build();
				httpPost.setEntity(entity);
				CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
				respContent = getHttpResonse(httpResponse, CHARSET);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			} finally {
				try {
					httpClient.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
			return respContent;
		}
	}

	/**
	 * multipart/form-data 表单 post提交
	 */
	public static String post(String url, List<NameValuePair> params) {
		return post(url, params, FORM_TYPE);
	}

	/**
	 * @description: 通用post请求
	 */
	public static String post(String url, List<NameValuePair> params, String formType) {
		String respContent = null;
		// 创建默认的httpClient实例
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// 用post方法发送http请求
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(getShortRequestConf());
			httpPost.setEntity(buildHttpEntity(params, formType));
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
			try {
				if (httpResponse.getStatusLine().getStatusCode() == STATUS_200) {
					respContent = EntityUtils.toString(httpResponse.getEntity(), CHARSET);
					respContent = respContent == null ? "" : respContent;
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			} finally {
				try {
					httpResponse.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return respContent;
	}

	/**
	 * 根据不同表单组装post请求参数
	 */
	private static HttpEntity buildHttpEntity(List<NameValuePair> params, String formType)
			throws UnsupportedEncodingException {
		// multipart/form-data 表单
		if (null == formType || formType.equals(FORM_TYPE)) {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			// 参数列表
			if (isNotEmpty(params)) {
				for (int i = 0; i < params.size(); i++) {
					NameValuePair param = params.get(i);
					builder.addTextBody(param.getName(), param.getValue(), CONTENT_TYPE);
				}
			}
			return builder.build();
		}
		// application/x-www-form-urlencoded表单
		return new UrlEncodedFormEntity(params, CHARSET);
	}

	public static void main(String[] args) {
	}

}
