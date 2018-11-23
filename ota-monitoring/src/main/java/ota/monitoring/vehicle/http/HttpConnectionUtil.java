package ota.monitoring.vehicle.http;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class HttpConnectionUtil {

	public static StringEntity buildJSONEntity(String json) {

		StringEntity requestEntity = null;
		try {
			requestEntity = new StringEntity(json);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		requestEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8"));

		return requestEntity;
	}

	public static UrlEncodedFormEntity buildURLEncodedEntity(List<BasicNameValuePair> params) {
		UrlEncodedFormEntity requestEntity = null;
		try {
			requestEntity = new UrlEncodedFormEntity(params);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		requestEntity.setContentEncoding("UTF-8");
		requestEntity.setContentType("application/x-www-form-urlencoded");

		return requestEntity;
	}
}
