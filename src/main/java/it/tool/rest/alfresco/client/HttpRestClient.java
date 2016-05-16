/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tool.rest.alfresco.client;

import java.io.IOException;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple HTTP Rest Client
 * 
 * @author Francesco Fornasari - Christian Tiralosi T.A.I Software Solution s.r.l
 */
public class HttpRestClient {

    private static final Logger logger = LoggerFactory.getLogger(HttpRestClient.class);
    private final CloseableHttpClient client;
    private final PoolingHttpClientConnectionManager httpConnectionManager = new PoolingHttpClientConnectionManager();
    private static final int DEFAULT_CONN_NUM = 100;
    private static final int DEFAULT_REQUEST_TIMEOUT = 300; //wait max 5 min
    private static final int DEFAULT_SOCKET_TIMEOUT = 300; //wait max 5 min

    public HttpRestClient(String user, String password) {

        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(DEFAULT_SOCKET_TIMEOUT * 1000).build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(DEFAULT_REQUEST_TIMEOUT * 1000)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT * 1000)
                .setConnectionRequestTimeout(DEFAULT_REQUEST_TIMEOUT * 1000).build();

        httpConnectionManager.setMaxTotal(DEFAULT_CONN_NUM);
        httpConnectionManager.setDefaultMaxPerRoute(DEFAULT_CONN_NUM);

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(AuthScope.ANY), new UsernamePasswordCredentials(user, password));
        client = HttpClients.custom().setConnectionManager(httpConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setDefaultSocketConfig(socketConfig)
                .setDefaultCredentialsProvider(credsProvider).build();
    }

    protected CloseableHttpClient getHttpClient() {
        return client;
    }

    protected byte[] fireRequest(HttpRequestBase request) throws Exception {

        byte[] result = null;
        try {
            try (CloseableHttpResponse response = getHttpClient().execute(request)) {

                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK && response.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED) {

                    String errorResult;
                    if (null != response.getEntity()) {
                        errorResult = EntityUtils.toString(response.getEntity());
                    } else {
                        errorResult = "Error on http request [" + response.getStatusLine().getReasonPhrase() + "]";
                    }
                    throw new Exception(errorResult);
                }

                result = EntityUtils.toByteArray(response.getEntity());

                if (logger.isDebugEnabled()) {
                    logger.debug("Http response [" + new String(result) + "]");
                }
            } finally {
                request.releaseConnection();
            }
        } catch (IOException | RuntimeException ex) {
            logger.error("Exception on http request",ex);
            throw ex;
        }

        return result;
    }

}
