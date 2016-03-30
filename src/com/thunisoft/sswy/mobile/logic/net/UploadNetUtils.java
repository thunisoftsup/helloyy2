package com.thunisoft.sswy.mobile.logic.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.thunisoft.sswy.mobile.Susong51Application;
import com.thunisoft.sswy.mobile.cache.CourtCache;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.exception.SSWYException;
import com.thunisoft.sswy.mobile.exception.SSWYNetworkException;
import com.thunisoft.sswy.mobile.logic.AuthLogic;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.util.ConfigUtils_;
import com.thunisoft.sswy.mobile.util.SSWYConstants;
import com.thunisoft.sswy.mobile.util.ServiceUtils;
import com.thunisoft.sswy.mobile.util.StringUtils;

/**
 * 访问服务端数据的工具类
 * 
 * @author gewx
 * 
 */
@EBean(scope = Scope.Singleton)
public class UploadNetUtils {
	private static final String TAG = "NetUtils";
	public static final String CHARSET_DEFAULT = HTTP.UTF_8;

	private static final int DEFAULT_CONN_TIMEOUT = 60 * 1000;
	private static final int DEFAULT_SOCKET_TIMEOUT = 10 * 1000;
	private static final int DEFAULT_HOST_CONNECTIONS = 40;
	private static final int DEFAULT_MAX_CONNECTIONS = 100;
	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 2048;

	private String sid = null;
	private String serverAddress = null;

	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private final WriteLock wLock = lock.writeLock();
	private final ReadLock rLock = lock.readLock();

	@Pref
	ConfigUtils_ configUtils;
	@Bean
	LoginCache loginCache;
	@Bean
	CourtCache courtCache;

	@Bean
	AuthLogic authLogic;

	@App
	Susong51Application application;

	public String getMainAddress() {
		if (serverAddress == null) {
			StringBuilder sb = new StringBuilder();
			int port = configUtils.serverPort().get();
			sb.append("http://").append(configUtils.serverHost().get());
			if (port != 80) {
				sb.append(":").append(port);
			}
			String ctx = configUtils.context().get();
			if (StringUtils.isNotBlank(ctx)) {
				sb.append("/").append(ctx);
			}
			serverAddress = sb.toString();
		}
		return serverAddress;
	}

	public String getServerAddress() {
		boolean zj = courtCache.isZj();
		String address = null;
		if (zj) {
			address = courtCache.getSsfwUrl("");
			if (!address.startsWith("http://")) {
				address = "http://" + address;
			}
		} else {
			address = getMainAddress();
		}
		Log.i(TAG, "当前地址：" + address);
		return address;
	}

	public String getAttachAddress() {
		return getServerAddress() + "/store";
	}

	private Map<String, HttpClient> clientMap = new HashMap<String, HttpClient>();

	String currentSsfwUrl = null;

	private synchronized HttpClient getHttpClient(String ssfwUrl) {
		HttpClient httpClient = clientMap.get(ssfwUrl);
		currentSsfwUrl = ssfwUrl;
		if (httpClient == null) {
			HttpParams httpParams = new BasicHttpParams();

			// 从连接池中取连接的超时时间
			ConnManagerParams.setTimeout(httpParams, 5 * 1000);
			// 连接超时
			HttpConnectionParams.setConnectionTimeout(httpParams,
					DEFAULT_CONN_TIMEOUT);
			// 请求超时
			HttpConnectionParams.setSoTimeout(httpParams,
					DEFAULT_SOCKET_TIMEOUT);

			ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
					new ConnPerRouteBean(DEFAULT_HOST_CONNECTIONS));
			ConnManagerParams.setMaxTotalConnections(httpParams,
					DEFAULT_MAX_CONNECTIONS);

			HttpProtocolParams.setUseExpectContinue(httpParams, true);
			HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);

			HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);

			HttpClientParams.setRedirecting(httpParams, true);

			HttpProtocolParams.setUserAgent(httpParams,
					System.getProperty("http.agent"));

			HttpConnectionParams.setTcpNoDelay(httpParams, false);

			HttpConnectionParams.setSocketBufferSize(httpParams,
					DEFAULT_SOCKET_BUFFER_SIZE);

			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			ClientConnectionManager manager = new ThreadSafeClientConnManager(
					httpParams, schemeRegistry);
			httpClient = new DefaultHttpClient(manager, httpParams);
			Log.e(TAG, "新建HttpClient:" + ssfwUrl);
			clientMap.put(ssfwUrl, httpClient);
			((AbstractHttpClient) httpClient)
					.setRedirectHandler(new DefaultRedirectHandler() {
						@Override
						public boolean isRedirectRequested(
								HttpResponse response, HttpContext context) {
							boolean isRedirect = super.isRedirectRequested(
									response, context);
							if (!isRedirect) {
								int responseCode = response.getStatusLine()
										.getStatusCode();
								if (responseCode == 301 || responseCode == 302) {
									return true;
								}
							}
							return isRedirect;
						}
					});
		}

		return httpClient;
	}

	public String post(String url, MultipartEntity entity)
			throws SSWYException, UnsupportedEncodingException {
		rLock.lock();
		try {
			final String sid = this.sid;
			if (sid != null) {
				entity.addPart("sid",
						new StringBody(sid, Charset.forName(CHARSET_DEFAULT)));
			}
		} finally {
			rLock.unlock();
		}

		String result = "";
		try {
			Log.i(TAG, url);
			result = doPost(url, entity);
			Log.i(TAG, "result:" + result);
			result = relogin(url, entity, result);
			ServiceUtils.startServiceTimeService(application.getBaseContext());
		} catch (SSWYNetworkException e) {
			Log.e(TAG, "网络连接出错1", e);
			throw new SSWYNetworkException(SSWYConstants.ERROR_NETWORK, e);
		} catch (IOException e) {
			Log.e(TAG, "IOException:", e);
			if (e instanceof SocketException) {
				try {
					if (url.indexOf("/pro/") < 0) {
						result = doPost(url, entity);
					} else {
						Log.e(TAG, "sharesession超时，重新登录");
						result = relogin(url, entity,
								SSWYConstants.RE_LOGIN_FLAG);
					}
				} catch (Exception e1) {
					Log.e(TAG, "网络连接出错2", e1);
					throw new SSWYNetworkException(SSWYConstants.ERROR_NETWORK,
							e);
				}
				return result;
			} else {
				Log.e(TAG, "网络连接出错2", e);
				throw new SSWYNetworkException(SSWYConstants.ERROR_NETWORK, e);
			}

		} catch (Exception e) {
			Log.e(TAG, "未知系统错误", e);
			throw new SSWYException(SSWYConstants.ERROR_UNKNOW, e);
		}
		return result;
	}

	public String doPost(String url, MultipartEntity entity)
			throws ParseException, IOException, SSWYNetworkException {
		String ssfwUrl = getMainAddress();
		if (url.indexOf(ssfwUrl) < 0) {
			ssfwUrl = getServerAddress();
		}
		Log.e(TAG, "ssfwUrl:" + ssfwUrl);
		Log.i("一次请求:", "url:" + url);
		// 创建POST请求
		HttpPost post = new HttpPost(url);
		if (!ssfwUrl.equals(currentSsfwUrl)) {
			Log.e(TAG, "服务地址已切换");
			loginCache.setSessionId("");
		} else {
			if (StringUtils.isNotBlank(loginCache.getSessionId())) {
				Log.i("传入的JSESSIONID:", loginCache.getSessionId());
				post.setHeader("Cookie",
						"JSESSIONID=" + loginCache.getSessionId());
			}
		}
		post.setEntity(entity);
		// 发送请求
		HttpClient client = getHttpClient(ssfwUrl);
		HttpResponse response = client.execute(post);
		int code = response.getStatusLine().getStatusCode();
		if (code != HttpStatus.SC_OK) {
			Log.e(TAG, "响应状态错误：" + response.getStatusLine().getStatusCode());
			throw new SSWYNetworkException(SSWYConstants.ERROR_NETWORK);
		} else {
			CookieStore cookieStore = ((DefaultHttpClient) client)
					.getCookieStore();
			List<Cookie> cookies = cookieStore.getCookies();
			for (Cookie cookie : cookies) {
				if ("JSESSIONID".equals(cookie.getName())) {
					loginCache.setSessionId(cookie.getValue());
					Log.i("JSESSIONID:", cookie.getValue());
				}

			}
		}

		HttpEntity resEntity = response.getEntity();
		String result = (resEntity == null) ? null : EntityUtils.toString(
				resEntity, CHARSET_DEFAULT);
		return result;
	}

	int reloginedTimes = 0;

	private String relogin(String url, MultipartEntity entity, String result)
			throws Exception {
		if (SSWYConstants.RE_LOGIN_FLAG.equals(result)) {
			return login(url, entity, result);
		} else {// 网上立案新增加的接口，session失效，返回的重新登录与之前不同，
			if (StringUtils.isNotBlank(result)) {
				int code = NetUtils.DEFAULT_CODE;
				String msg = "";
				try {
					JSONObject json = new JSONObject(result);
					code = json.getInt("code");
					msg  = json.getString("message");
				} catch (JSONException e) {
					// 此处code只用于判断是否需要重新登录，不做异常处理
				}
				if (NetUtils.RE_LOGIN_CODE == code&&StringUtils.isNotBlank(msg)&&!msg.equals("保存数据失败")) {
					return login(url, entity, result);
				}
			}
		}
		return result;
	}

	private String login(String url, MultipartEntity entity, String result)
			throws Exception {
		Log.i(TAG, "url:" + url);
		Log.i(TAG, "logined:" + loginCache.isLogined());
		// 重新登录
		if (loginCache.isLogined()) {
			reloginedTimes++;
			Log.i(TAG, "reloginedTimes:" + reloginedTimes);
			if (reloginedTimes > 3) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("success", false);
				jsonObject.put("message", SSWYConstants.ERROR_UNKNOW);
				reloginedTimes = 0;
				return jsonObject.toString();
			}
			BaseResponse tempBr = authLogic.getTempSid();
			BaseResponse br = authLogic.login(tempBr.getTempSid(), null,
					loginCache.getUserName(), loginCache.getPassword(),
					loginCache.getUserType(), null, null, null);
			if (br.isSuccess()) {
				reloginedTimes = 0;
				Log.i(TAG, "重新访问url：" + url);
				result = _post(url, entity); // 使用初始的参数
			}
		}
		return result;
	}

	private String _post(String url, MultipartEntity entity) throws Exception {
		rLock.lock();
		try {
			final String sid = this.sid;
			if (sid != null) {
				entity.addPart("sid",
						new StringBody(sid, Charset.forName(CHARSET_DEFAULT)));
			}
			return doPost(url, entity);
		} finally {
			rLock.unlock();
		}
	}

	public void lockWrite() {
		wLock.lock();
	}

	public void unlockWrite() {
		wLock.unlock();
	}

	public void setSId(String value) {
		this.sid = value;
	}
}
