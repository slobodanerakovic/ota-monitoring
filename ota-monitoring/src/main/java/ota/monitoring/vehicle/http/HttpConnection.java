package ota.monitoring.vehicle.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

@Component
public class HttpConnection {

	private static final Logger LOG = LoggerFactory.getLogger(HttpConnection.class);
	private final int SOCKET_TIMEOUT = 30 * 1000;
	private final int CONNECTION_TIMEOUT = 30 * 1000;
	private final int REQUEST_TIMEOUT = 30 * 1000;

	public boolean post(final String url, final AbstractHttpEntity entity) {

		RequestConfig requestConfig = prepareRequestCongfig();

		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(requestConfig);
		httppost.setHeader(entity.getContentType());
		httppost.setEntity(entity);

		logRequest(httppost);

		return handleResponse(httppost);
	}

	private boolean handleResponse(HttpRequestBase httpRequest) {
		InputStream instream = null;
		String content = "";
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();

			// Execute the method.
			HttpResponse response = httpClient.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				LOG.error("Http communication  failed: " + response.getStatusLine());
				return false;
			}

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				instream = entity.getContent();
				content = CharStreams.toString(new InputStreamReader(instream, Charsets.UTF_8));
				Closeables.close(instream, false);
			}
		} catch (Exception e) {
			Throwables.throwIfUnchecked(e);
		} finally {
			try {
				if (instream != null)
					instream.close();
			} catch (IOException e) {
				Throwables.throwIfUnchecked(e);
			}
		}
		// LOG.info("Http Response = {}", content);

		return true;
	}

	private void logRequest(HttpPost httppost) {
		try {
			LOG.info("URL: {}, params: {}", httppost.getURI(), EntityUtils.toString(httppost.getEntity()));
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	}

	private RequestConfig prepareRequestCongfig() {
		// Request configuration can be overridden at the request level.
		// They will take precedence over the one set at the client level.
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT)
				.setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(REQUEST_TIMEOUT).build();

		// Execution context can be customized locally.
		HttpClientContext context = HttpClientContext.create();
		// Contextual attributes set the local context level will take
		// precedence over those set at the client level.
		context.setAttribute("http.protocol.version", HttpVersion.HTTP_1_1);

		return requestConfig;
	}
}
