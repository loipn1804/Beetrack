package dne.beetrack.connection;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpEncoding;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import dne.beetrack.connection.callback.ApiCallback;

/**
 * Created by loipn on 7/5/2016.
 */
public class Api {
    private String URL = Variable.BASE_URL + "api/";
    private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private int TIME_OUT = 30 * 1000;

    public void login(ApiCallback callback, String username, String password) {
        try {
            String url = URL + "login";
            GenericUrl requestUrl = new GenericUrl(url);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();

            Map<String, String> parameters = new HashMap<>();
            parameters.put("username", username);
            parameters.put("password", password);
            HttpContent content = new UrlEncodedContent(parameters);

            HttpRequest request = requestFactory.buildPostRequest(requestUrl, content);
            request.setReadTimeout(TIME_OUT);
            request.setConnectTimeout(TIME_OUT);

            HttpResponse response = request.execute();
            if (response.isSuccessStatusCode()) {
                callback.onSuccess(response.parseAsString());
            } else {
                callback.onFail(response.getStatusCode() + ":" + response.getStatusMessage());
            }
        } catch (IOException e) {
            callback.onFail(e.getMessage());
        }
    }

    public void getListSession(ApiCallback callback, long account_id) {
        try {
            String url = URL + "session/getAllPending/" + account_id;
            GenericUrl requestUrl = new GenericUrl(url);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();

            HttpRequest request = requestFactory.buildGetRequest(requestUrl);
            request.setReadTimeout(TIME_OUT);
            request.setConnectTimeout(TIME_OUT);

            HttpResponse response = request.execute();
            if (response.isSuccessStatusCode()) {
                callback.onSuccess(response.parseAsString());
            } else {
                callback.onFail(response.getStatusCode() + ":" + response.getStatusMessage());
            }
        } catch (IOException e) {
            callback.onFail(e.getMessage());
        }
    }
}
