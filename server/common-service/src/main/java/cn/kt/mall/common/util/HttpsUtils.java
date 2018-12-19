package cn.kt.mall.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpsUtils {
	private static Logger logger = LoggerFactory.getLogger(HttpsUtils.class);
	static CloseableHttpClient httpClient;
	static CloseableHttpResponse httpResponse;

	public static CloseableHttpClient createSSLClientDefault() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
			return HttpClients.custom().setDefaultRequestConfig(requestConfig).setSSLSocketFactory(sslsf).build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return HttpClients.createDefault();

	}

	/**
	 * 发送https请求
	 *
	 * @throws Exception
	 */
	public static String sendByHttp(Map<String, Object> params, String url) {
		try {
			logger.info("send request to url: {}",url);
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> listNVP = new ArrayList<NameValuePair>();
			if (params != null) {
				for (String key : params.keySet()) {
					listNVP.add(new BasicNameValuePair(key, params.get(key).toString()));
				}
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(listNVP, "UTF-8");
			httpPost.setEntity(entity);
			httpClient = HttpsUtils.createSSLClientDefault();
			httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				String jsObject = EntityUtils.toString(httpEntity, "UTF-8");
				return jsObject;
			} else {
				logger.error("获取实时汇率超时或出错");
				return null;
			}
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if(httpResponse != null) {
					httpResponse.close();
				}
				if(httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				logger.error("请求流关闭出错");
			}
		}
	}

	/**
	 * 发送https请求
	 *
	 * @throws Exception
	 */
	public static String getByHttp(String url) {
		try {
			logger.info("send request to url: {}",url);
			HttpGet httpGet = new HttpGet(url);
			httpClient = HttpsUtils.createSSLClientDefault();
			httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				String jsObject = EntityUtils.toString(httpEntity, "UTF-8");
				return jsObject;
			} else {
				logger.error("获取实时汇率超时或出错");
				return null;
			}
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if(httpResponse != null) {
					httpResponse.close();
				}
				if(httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				logger.error("请求流关闭出错");
			}
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(HttpsUtils.sendByHttp(null, "https://api.dobitrade.com/mbt"));
	}
}
