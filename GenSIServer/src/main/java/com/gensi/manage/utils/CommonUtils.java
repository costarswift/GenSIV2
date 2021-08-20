package com.gensi.manage.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class CommonUtils {
//	private static final int SOCKET_TIMEOUT = 90000;
//	private static final int CONNECT_TIMEOUT = 15000;
//	private static final int CONNECT_POOL_TIMEOUT = 5000;
//
//	private static final int ATOMIC_MIN_VALUE = 10000;
//	private static final int ATOMIC_MAX_VALUE = 99999;

	private static Logger logger = Logger.getLogger(com.gensi.manage.utils.CommonUtils.class);
	private static AtomicInteger sequenceNo = new AtomicInteger(10000);

	// 信任管理器
//	private static X509TrustManager trustManager = new X509TrustManager() {
//		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//		}
//
//		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//		}
//
//		public X509Certificate[] getAcceptedIssuers() {
//			return null;
//		}
//	};

	public static void threadSleep(long millis) {
		try {
			TimeUnit.MILLISECONDS.sleep(millis);
			// Thread.sleep(millis);
		} catch (InterruptedException e) {
		}

		return;
	}

	// public static void randomSleep(long minMillis, long maxMillis)
	// {
	// long value = 0;
	// Random objRandom = null;
	//
	// try
	// {
	// objRandom = new Random();
	// value = objRandom.nextLong();
	// value = value % maxMillis;
	// if(value < minMillis)
	// value = minMillis;
	//
	// Thread.sleep(value);
	// }
	// catch(InterruptedException e)
	// {
	// logger.warn(e.getMessage(), e);
	// }
	//
	// return;
	// }

	public static void randomTimerSleep(long minMillis, long maxMillis) {
		long value = 0;
		Random objRandom = null;
		Calendar nowCalendar = null;

		try {
			// 工作时间进行休眠
			nowCalendar = Calendar.getInstance();
			if (nowCalendar.get(Calendar.HOUR_OF_DAY) > 7 && nowCalendar.get(Calendar.HOUR_OF_DAY) < 20) {
				objRandom = new Random();
				value = objRandom.nextLong();
				value = value % maxMillis;
				if (value < minMillis)
					value = minMillis;
			} else {
				value = 50;
			}

			// Thread.sleep(value);
			TimeUnit.MILLISECONDS.sleep(value);
		} catch (InterruptedException ignore) {
		}

		return;
	}

	public static String formatDate() {
		String dateString = "";
		Date nowDate = null;
		SimpleDateFormat sdfDate = null;

		sdfDate = new SimpleDateFormat("yyyyMMdd");
		nowDate = new Date();
		dateString = sdfDate.format(nowDate);

		return dateString;
	}

	/**
	 * 输出指定格式的当前时间的字符串表示形式
	 * 
	 * @param formatPattern
	 * @return
	 */
	public static String formatDateNow(String formatPattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatPattern);
		return simpleDateFormat.format(new Date());
	}

	public static String formatYYMMDDHHMMSSDate() {
		String dateString = "";
		Date nowDate = null;
		SimpleDateFormat sdfDate = null;

		sdfDate = new SimpleDateFormat("yyMMddHHmmss");
		nowDate = new Date();
		dateString = sdfDate.format(nowDate);

		return dateString;
	}

	public static String generateSequence() {
		String seqNo = "";
		Date nowDate = null;
		SimpleDateFormat sdfDate = null;

		sdfDate = new SimpleDateFormat("yyMMddHHmmss");
		nowDate = new Date();
		seqNo = sdfDate.format(nowDate) + sequenceNo.incrementAndGet();
		seqNo = Long.toHexString(Long.parseLong(seqNo));

		return seqNo;
	}

	public static String sendHttpBodyRequest(String requestUrl, String requestJson) {
		int statusCode = 0;
		HttpPost httpPost = null;
		StringEntity reqEntity = null;
		HttpEntity responseEntity = null;
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse httpResponse = null;
		String resultData = "";
		logger.info("---requestUrl---" + requestUrl);
		logger.info("---requestJson---" + requestJson);
		
		try {
			// Ppf037981
			// requestJson = "{\"body\": {\"fundsInfo\": {\"fundsAccount\": \"077941140\", \"fundsPassword\": \"Ppf037981\", " +
			// "\"imgVerificationCode\": \"string\", \"verificationCode\": \"string\" }, \"idCard\": \"string\", \"loanId\": \"mlyy201608091443132476223\",
			// \"remark\": \"string\", \"requestType\": \"2\", \"searchType\": \"0\", \"userName\": \"test\" }, \"header\": { \"serviceCode\":
			// \"auth_credit_account\" } }";
			httpclient = HttpClients.createDefault();

			reqEntity = new StringEntity(requestJson, Consts.UTF_8);
			reqEntity.setContentType("application/json");
			// httpPost = new HttpPost("http://192.168.56.53:2222/bigdata/credit");
			httpPost = new HttpPost(requestUrl);
			httpPost.setEntity(reqEntity);

			long lStartTime = 0, lCostTime = 0;
			lStartTime = System.currentTimeMillis();
			httpResponse = httpclient.execute(httpPost);
			statusCode = httpResponse.getStatusLine().getStatusCode();
			lCostTime = System.currentTimeMillis() - lStartTime;
			logger.info("sendHttpBodyRequest - requestTime[" + lCostTime + "] statusCode = " + statusCode);

			if (statusCode == HttpStatus.OK.value()) {
				responseEntity = httpResponse.getEntity();
				resultData = EntityUtils.toString(responseEntity, "utf-8");
				logger.info(resultData);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		} finally {
			try {
				if (httpclient != null)
					httpclient.close();
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
			httpclient = null;
		}

		return resultData;
	}

	public static String sendHttpParameterRequest(String requestUrl, String reqParameter, String requestJson) {
		int statusCode = 0;
		HttpPost httpPost = null;
		HttpEntity responseEntity = null;
		CloseableHttpClient httpclient = null;
		List<NameValuePair> formParams = null;
		UrlEncodedFormEntity formEntity = null;
		CloseableHttpResponse httpResponse = null;
		String resultData = "";
		logger.info("---requestUrl---" + requestUrl);

		try {
			// Ppf037981
			httpclient = HttpClients.createDefault();

			formParams = new ArrayList<NameValuePair>();
			formParams.add(new BasicNameValuePair(reqParameter, requestJson));
			formEntity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);

			httpPost = new HttpPost(requestUrl);
			// httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httpPost.setEntity(formEntity);
			// logger.info(EntityUtils.toString(formEntity));
			logger.info("---attribute---" + reqParameter + " => " + requestJson);
			long lStartTime = 0, lCostTime = 0;
			lStartTime = System.currentTimeMillis();
			httpResponse = httpclient.execute(httpPost);
			statusCode = httpResponse.getStatusLine().getStatusCode();
			lCostTime = System.currentTimeMillis() - lStartTime;
			logger.info("requestTime[" + lCostTime + "] statusCode = " + statusCode);

			if (statusCode == HttpStatus.OK.value()) {
				responseEntity = httpResponse.getEntity();
				resultData = EntityUtils.toString(responseEntity, "utf-8");
				logger.info("responseEntity-resultData:" + resultData);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		} finally {
			try {
				if (httpclient != null)
					httpclient.close();
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
			httpclient = null;
		}

		return resultData;
	}

	public static String sendHttpParameterRequest(String requestUrl, Map<String, Object> params) {
		int statusCode = 0;
		HttpPost httpPost = null;
		HttpEntity responseEntity = null;
		CloseableHttpClient httpclient = null;
		List<NameValuePair> formParams = null;
		UrlEncodedFormEntity formEntity = null;
		CloseableHttpResponse httpResponse = null;
		String resultData = "";
		logger.info("---requestUrl---" + requestUrl);

		try {
			httpclient = HttpClients.createDefault();
			formParams = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				formParams.add(new BasicNameValuePair(key, params.get(key).toString()));
				logger.info("---attribute---" + key + " => " + params.get(key));
			}

			formEntity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);

			httpPost = new HttpPost(requestUrl);
			// httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httpPost.setEntity(formEntity);

			long lStartTime = 0, lCostTime = 0;
			lStartTime = System.currentTimeMillis();
			httpResponse = httpclient.execute(httpPost);
			statusCode = httpResponse.getStatusLine().getStatusCode();
			lCostTime = System.currentTimeMillis() - lStartTime;
			logger.info("requestTime[" + lCostTime + "] statusCode = " + statusCode);

			if (statusCode == HttpStatus.OK.value()) {
				responseEntity = httpResponse.getEntity();
				resultData = EntityUtils.toString(responseEntity, "utf-8");
				logger.info("responseEntity-resultData:" + resultData);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		} finally {
			try {
				if (httpclient != null) {
					httpclient.close();
				}
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
			httpclient = null;
		}

		return resultData;
	}

	public static void inputstreamToFile(InputStream ins, File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
	}

	public String inputStreamToString(InputStream is) {
		String result = null;
		try {
			// BufferedReader in = new BufferedReader(new InputStreamReader(is));
			// StringBuffer buffer = new StringBuffer();
			// String line = "";
			// while ((line = in.readLine()) != null){
			// buffer.append(line);
			// }
			// result = buffer.toString();
			ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
			byte[] str_b = new byte[1024];
			int i = -1;
			while ((i = is.read(str_b)) > 0) {
				outputstream.write(str_b, 0, i);
			}
			result = outputstream.toString();

		} catch (IOException e) {

			logger.warn(e.getMessage(), e);
		}
		return result;
	}

//	public static boolean downloadFileByHttp(MyChromeDriver webDriver, String fileUrl, String saveFilename) {
//		HttpGet httpGet = null;
//		Header headerInfo = null;
//		byte[] fileData = null;
//		boolean resultStatus = false;
//		CookieStore cookieStore = null;
//		FileOutputStream fosDest = null;
//		HttpEntity responseEntity = null;
//		int statusCode = 0, retryCount = 0;
//		CloseableHttpResponse httpResponse = null;
//		CloseableHttpClient httpclient;
//		HttpClientContext clientContext;
//
//		retryCount = 0;
//		httpclient = HttpClients.createDefault();
//		clientContext = HttpClientContext.create();
//		do {
//			resultStatus = false;
//			try {
//				// 重试之前休眠
//				if (retryCount > 0)
//					CommonUtils.randomTimerSleep(500, 3000);
//
//				httpGet = new HttpGet(fileUrl);
//				// httpGet.addHeader("Accept", "image/png, image/svg+xml, image/*;q=0.8, */*;q=0.5");
//				cookieStore = getSessionCookie(webDriver);
//				clientContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
//
//				long lStartTime = 0, lCostTime = 0;
//				lStartTime = System.currentTimeMillis();
//				// logger.debug("downloadFileByHttp - clientContext.cookies:" + clientContext.getCookieStore().getCookies());
//				// httpGet.setConfig(requestConfig);
//				httpResponse = httpclient.execute(httpGet, clientContext);
//				statusCode = httpResponse.getStatusLine().getStatusCode();
//				// logger.debug("getImageByHttp - clientContext.cookies:" + clientContext.getCookieStore().getCookies());
//				lCostTime = System.currentTimeMillis() - lStartTime;
//				logger.info("downloadFileByHttp - fileUrl[" + fileUrl + "] - statusCode = " + statusCode + " requestTime[" + lCostTime + "]- Content-Type[" + headerInfo + "]");
//
//				if (statusCode == HttpStatus.OK.value()) {
//					responseEntity = httpResponse.getEntity();
//
//					headerInfo = httpResponse.getFirstHeader("Content-Type");
//					logger.debug("downloadFileByHttp - fileUrl[" + fileUrl + "] - Content-Type[" + headerInfo + "]");
//					fileData = EntityUtils.toByteArray(responseEntity);
//					fosDest = new FileOutputStream(new File(saveFilename));
//					fosDest.write(fileData);
//					resultStatus = true;
//
//				}
//			} catch (java.net.SocketTimeoutException e) {
//				// imageCode = "";
//				logger.warn("downloadFileByHttp - fileUrl[" + fileUrl + "] - SocketTimeoutException");
//			} catch (Exception e) {
//				// imageCode = "";
//				logger.warn("downloadFileByHttp - fileUrl[" + fileUrl + "] - Exception - " + e.toString());
//
//			} finally {
//				try {
//					if (fosDest != null)
//						fosDest.close();
//				} catch (Exception e) {
//					logger.warn("要保存的文件关闭失败", e);
//				}
//				try {
//					if (httpResponse != null)
//						httpResponse.close();
//				} catch (Exception e) {
//					logger.warn("response关闭失败", e);
//				}
//				httpResponse = null;
//				fosDest = null;
//			}
//
//			retryCount++;
//		} while (resultStatus == false && retryCount < 3);
//
//		return resultStatus;
//	}

//	public static boolean downloadFileByHttps(MyChromeDriver webDriver, String fileUrl, String saveFilename) {
//		HttpGet httpGet = null;
//		Header headerInfo = null;
//		byte[] fileData = null;
//		boolean resultStatus = false;
//		CookieStore cookieStore = null;
//		FileOutputStream fosDest = null;
//		HttpEntity responseEntity = null;
//		int statusCode = 0, retryCount = 0;
//		CloseableHttpResponse httpResponse = null;
//		CloseableHttpClient httpclient;
//		HttpClientContext clientContext;
//		SSLContext sslContext = null;
//		RequestConfig requestConfig = null;
//		SSLConnectionSocketFactory sslConnectionFactory = null;
//
//		try {
//			sslContext = SSLContext.getInstance("TLS");
//			// 初始化SSL上下文
//			sslContext.init(null, new TrustManager[] { trustManager }, null);
//			// SSL套接字连接工厂,NoopHostnameVerifier为信任所有服务器
//			sslConnectionFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
//
//			// // 注册http套接字工厂和https套接字工厂
//			// Registry<ConnectionSocketFactory> regConnectionFactory = RegistryBuilder.<ConnectionSocketFactory>create()
//			// .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", sslConnectionFactory).build();
//
//			// httpclient = HttpClients.custom().setSSLSocketFactory(sslConnectionFactory).setMaxConnTotal(50).setMaxConnPerRoute(50)
//			// .setDefaultRequestConfig(requestConfig).build();
//			// httpclient = HttpClients.createDefault();
//		} catch (Exception e) {
//			logger.warn(e.toString(), e);
//		}
//
//		retryCount = 0;
//		clientContext = HttpClientContext.create();
//		requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECT_POOL_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
//		httpclient = HttpClients.custom().setSSLSocketFactory(sslConnectionFactory).setDefaultRequestConfig(requestConfig).build();
//		do {
//			resultStatus = false;
//			try {
//				// 重试之前休眠
//				if (retryCount > 0)
//					CommonUtils.randomTimerSleep(500, 3000);
//
//				httpGet = new HttpGet(fileUrl);
//				// httpGet.addHeader("Accept", "image/png, image/svg+xml, image/*;q=0.8, */*;q=0.5");
//				cookieStore = getSessionCookie(webDriver);
//				clientContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
//
//				long lStartTime = 0, lCostTime = 0;
//				lStartTime = System.currentTimeMillis();
//				// logger.debug("downloadFileByHttp - clientContext.cookies:" + clientContext.getCookieStore().getCookies());
//				// httpGet.setConfig(requestConfig);
//				httpResponse = httpclient.execute(httpGet, clientContext);
//				statusCode = httpResponse.getStatusLine().getStatusCode();
//				// logger.debug("getImageByHttp - clientContext.cookies:" + clientContext.getCookieStore().getCookies());
//				lCostTime = System.currentTimeMillis() - lStartTime;
//				logger.info("downloadFileByHttps - fileUrl[" + fileUrl + "] - statusCode = " + statusCode + " requestTime[" + lCostTime + "]- Content-Type[" + headerInfo + "]");
//
//				if (statusCode == HttpStatus.OK.value()) {
//					responseEntity = httpResponse.getEntity();
//
//					headerInfo = httpResponse.getFirstHeader("Content-Type");
//					logger.debug("downloadFileByHttps - fileUrl[" + fileUrl + "] - Content-Type[" + headerInfo + "]");
//					fileData = EntityUtils.toByteArray(responseEntity);
//					fosDest = new FileOutputStream(new File(saveFilename));
//					fosDest.write(fileData);
//					resultStatus = true;
//
//				}
//			} catch (java.net.SocketTimeoutException e) {
//				// imageCode = "";
//				logger.warn("downloadFileByHttps - fileUrl[" + fileUrl + "] - SocketTimeoutException");
//			} catch (Exception e) {
//				// imageCode = "";
//				logger.warn("downloadFileByHttps - fileUrl[" + fileUrl + "] - Exception - " + e.toString());
//
//			} finally {
//				try {
//					if (fosDest != null)
//						fosDest.close();
//				} catch (Exception e) {
//					logger.warn("要保存的文件关闭失败", e);
//				}
//				try {
//					if (httpResponse != null)
//						httpResponse.close();
//				} catch (Exception e) {
//					logger.warn("response关闭失败", e);
//				}
//				httpResponse = null;
//				fosDest = null;
//			}
//
//			retryCount++;
//		} while (resultStatus == false && retryCount < 3);
//
//		return resultStatus;
//	}

	// 通过浏览器查看cookie内容，导入设置
//	public static CookieStore getSessionCookie(MyChromeDriver webDriver) {
//		Set<Cookie> cookieSet = null;
//		CookieStore cookieStore = null;
//		BasicClientCookie resultCookie = null;
//		String cookieName = "", cookieValue = "";
//
//		cookieStore = new BasicCookieStore();
//		cookieSet = webDriver.manage().getCookies();
//		for (Cookie objCookie : cookieSet) {
//			// 创建httpclient定义的cookie
//			cookieName = objCookie.getName();
//			cookieValue = objCookie.getValue();
//			resultCookie = new BasicClientCookie(cookieName, cookieValue);
//			resultCookie.setDomain(objCookie.getDomain());
//			resultCookie.setExpiryDate(objCookie.getExpiry());
//			resultCookie.setPath(objCookie.getPath());
//			resultCookie.setExpiryDate(objCookie.getExpiry());
//			cookieStore.addCookie(resultCookie);
//		}
//
//		return cookieStore;
//	}

	public static void main(String[] args) {
//		try {
			// CommonUtils.sendHttpRequest("http://192.168.52.213:8080/portal/doCredit/data", "{}");
			// String requestUrl = "http://218.76.63.90:8010/portal/doCredit/data";
//			String requestUrl = "http://192.168.52.105:8080/fyd/doCredit/data";
			// String requestJson = FileUtils.readFileToString(new File("D:/Project/rsp_json.txt"));

//			com.gensi.manage.utils.CommonUtils.sendHttpParameterRequest(requestUrl, "creditData", "testInfo");
//		} catch (Exception e) {
//			logger.warn(e.getMessage(), e);
//		}
	}

	public static void convertFileEncoding(String srcFilename, String strEncoding, String dstFilename, String dstEncoding) {
		// FileUtils.
	}

	/**
	 * 取得当月天数
	 */
	public static int getCurrentMonthLastDay() {
		Calendar a = Calendar.getInstance();
		return getMonthLastDay(a);
	}

	/**
	 * 取得某个时间的当月的天数
	 */
	public static int getMonthLastDay(Calendar a) {
		a.add(Calendar.MONTH, 1);// 时间流逝一个月
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.add(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		// int maxDate = a.getActualMaximum(Calendar.DAY_OF_MONTH);
		return maxDate;
	}

	/**
	 * 得到指定月的天数
	 */
	public static int getMonthLastDay(int year, int month) {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.add(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	// public static String getImageCode(WebDriver webDriver, WebElement imageElement)
	// {
	// String imgCode = "";
	// File screenFile = null, outImgfile = null;
	// String imageFilename = "d:/webmagic/data/shixin/image/shixin_image.jpg";
	//
	// try
	// {
	// screenFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
	// Point imgPoint = imageElement.getLocation();
	// int width = imageElement.getSize().getWidth();
	// int height = imageElement.getSize().getHeight();
	//
	// Rectangle rect = new Rectangle(width, height);
	// BufferedImage img = ImageIO.read(screenFile);
	// BufferedImage dest = img.getSubimage(imgPoint.getX(), imgPoint.getY(), rect.width, rect.height);
	// ImageIO.write(dest, "jpg", screenFile);
	// // Thread.sleep(1000);
	// FileUtils.copyFile(screenFile, new File(imageFilename));
	// }
	// catch (Exception eImage)
	// {
	// eImaglogger.warn(e.getMessage(), e);
	// }
	//
	// // CommonUtils.loadDLL("liblept168.dll");//娉ㄦ剰鍔犺浇鍏堝悗椤哄簭
	// // CommonUtils.loadDLL("libtesseract304.dll");//娉ㄦ剰鍔犺浇鍏堝悗椤哄簭
	//
	// // logger.info(LoadLibs.getTesseractLibName() + "\n\n\n");
	// // logger.info(new File(System.getenv("TESSDATA_PREFIX"), "./tessdata/eng.traineddata").exists());
	// // logger.info(System.getenv("TESSDATA_PREFIX"));
	//
	// // File tessdataFile = new File(System.getenv("TESSDATA_PREFIX"), "./tessdata/eng.traineddata");
	// // if(tessdataFile.exists())
	// // {
	// // logger.info("name - " + tessdataFile.getName());
	// // logger.info("Path - " + tessdataFile.getPath());
	// // logger.info("AbsolutePath - " + tessdataFile.getAbsolutePath());
	// // }
	// // System.load("C:/Users/ftoul107/AppData/Local/Temp/gsdll64.dll");
	// // System.load("C:/Users/ftoul107/AppData/Local/Temp/libtesseract304.dll");
	//
	// outImgfile = new File(imageFilename);
	// ITesseract instance = new Tesseract();
	// // ITesseract instance = new Tesseract1();
	// try
	// {
	// imgCode = instance.doOCR(outImgfile);
	// imgCode = imgCode.replaceAll("\n|\r", "");
	// // String fileName = file.toString().substring(file.toString().lastIndexOf("\\")+1);
	// // logger.info("鍥剧墖鍚嶏細" + imageFilename + "锛岃瘑鍒粨鏋滐細" + imgCode);
	// }
	// catch (TesseractException e)
	// {
	// logger.warn(e.getMessage(), e);
	// }
	//
	// return imgCode;
	// }
	/**
	 * tess4j辨认英文验证码图片
	 * 
	 * @param tessdataPath
	 * @param imageFilename
	 * @return
	 */
//	public static String recognizeImage(String tessdataPath, String imageFilename) {
//		String imgCode = "";
//		File outImgfile = null;
//
//		try {
//			outImgfile = new File(imageFilename);
//			ITesseract instance = new Tesseract();
//
//			instance.setDatapath(tessdataPath);
//
//			imgCode = instance.doOCR(outImgfile);
//			imgCode = imgCode.replaceAll("\n|\r", "");
//		} catch (TesseractException e) {
//			logger.warn(e.getMessage(), e);
//		}
//
//		return imgCode;
//	}

	/**
	 * tess4j辨认中文验证码图片
	 * 
	 * @param tessdataPath
	 * @param imageFilename
	 * @return
	 */
//	public static String recognizeChiImage(String tessdataPath, String imageFilename) {
//		String imgCode = "";
//		File outImgfile = null;
//
//		try {
//			outImgfile = new File(imageFilename);
//			ITesseract instance = new Tesseract();
//
//			instance.setDatapath(tessdataPath);
//			instance.setLanguage("chi_sim");
//
//			imgCode = instance.doOCR(outImgfile);
//			imgCode = imgCode.replaceAll("\n|\r", "");
//		} catch (TesseractException e) {
//			logger.warn(e.getMessage(), e);
//		}
//
//		return imgCode;
//	}

	// public static void loadDll(String libFullName)
	// {
	// try
	// {
	// String nativeTempDir = System.getProperty("java.io.tmpdir");
	// logger.info("nativeTempDir - " + nativeTempDir);
	//
	// InputStream in = null;
	// FileOutputStream writer = null;
	// BufferedInputStream reader = null;
	// String libFilename = nativeTempDir + File.separator + libFullName;
	// logger.info("libFilename - " + libFilename);
	//
	// File extractedLibFile = new File(libFilename);
	// if (!extractedLibFile.exists())
	// {
	// try
	// {
	// in = Tesseract.class.getResourceAsStream("/" + libFullName);
	// Tesseract.class.getResource(libFullName);
	//
	// reader = new BufferedInputStream(in);
	// writer = new FileOutputStream(extractedLibFile);
	// byte[] buffer = new byte[1024];
	// while (reader.read(buffer) > 0)
	// {
	// writer.write(buffer);
	// buffer = new byte[1024];
	// }
	// in.close();
	// writer.close();
	//
	// logger.info(extractedLibFile.toString());
	// System.load(extractedLibFile.toString());
	// }
	// catch (IOException e)
	// {
	// logger.warn(e.getMessage(), e);
	// }
	// finally
	// {
	// if (in != null)
	// {
	// in.close();
	// }
	// if (writer != null)
	// {
	// writer.close();
	// }
	// }
	// }
	// else
	// {
	// logger.info("System.load - " + extractedLibFile.toString());
	// logger.info("Platform.RESOURCE_PREFIX - " + Platform.RESOURCE_PREFIX);
	// System.load(extractedLibFile.toString());
	// }
	// }
	// catch (IOException e)
	// {
	// logger.warn(e.getMessage(), e);
	// }
	// }

	// public static void downloadFile(String strUrl, String imageFilename)
	// {
	// int readCount = 0, imageSize = 0;
	// File imageFile = null;
	// byte[] byteData = null, imageData = null;
	// FileOutputStream outStream = null;
	//
	// try
	// {
	//
	// URL url = new URL(strUrl); // 鎵撳紑閾炬帴
	// HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	// urlConnection.setConnectTimeout(5 * 1000); // 閫氳繃杈撳叆娴佽幏鍙栧浘鐗囨暟鎹� InputStream inStream = conn.getInputStream();
	//
	// // //寰楀埌鍥剧墖鐨勪簩杩涘埗鏁版嵁锛屼互浜岃繘鍒跺皝瑁呭緱鍒版暟鎹紝鍏锋湁閫氱敤鎬�
	// InputStream inStream = urlConnection.getInputStream();
	// imageData = new byte[1024 * 64];
	// byteData = new byte[1024];
	// do
	// {
	// readCount = inStream.read(byteData);
	// if (readCount > 0)
	// {
	// System.arraycopy(byteData, 0, imageData, imageSize, readCount);
	// imageSize = imageSize + readCount;
	// }
	// }
	// while (readCount > 0);
	//
	// // imageData = new byte[imageSize];
	// // fileData = readInputStream(inStream); // new涓�涓枃浠跺璞＄敤鏉ヤ繚瀛樺浘鐗囷紝榛樿淇濆瓨褰撳墠宸ョ▼鏍圭洰褰�
	//
	// imageFile = new File(imageFilename); // 鍒涘缓杈撳嚭娴�
	// outStream = new FileOutputStream(imageFile); // 鍐欏叆鏁版嵁 // // //鍏抽棴杈撳嚭娴�
	// outStream.write(imageData, 0, imageSize);
	// outStream.close();
	// }
	// catch (Exception e)
	// {
	// logger.warn(e.getMessage(), e);
	// }
	// finally
	// {
	// try
	// {
	// if (outStream != null)
	// {
	// outStream.close();
	// }
	//
	// }
	// catch (IOException e)
	// {
	//
	// logger.warn(e.getMessage(), e);
	//
	// }
	//
	// }
	//
	// }
	/**
	 * 判断是否是字母和数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isLetter(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		// 判断是否是数字类型
		String strpat = "[0-9a-zA-Z]+";
		Pattern pattern = Pattern.compile(strpat);
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断一个字符串是不为空
	 * 
	 * @param input
	 * @return 为空返回true,不为空返回false
	 */
	public static boolean isStringEmpty(String input) {
		Boolean res = false;
		if (input == null || input.trim().equals("")) {
			res =  true;
		}
		return res;
	}

	/**
	 * 判断一个字符串是否为空，或者为null
	 * 
	 * @param input
	 * @return 字符串是否为空或者为null返回true,否则返回false
	 */
	public static boolean isNullOrEmpty(String input) {
		if (input == null || input.replaceAll("\\s*", "").length() == 0) {
			return true;
		}
		return false;
	}
}
