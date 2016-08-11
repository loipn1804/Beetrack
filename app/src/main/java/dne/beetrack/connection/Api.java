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

    public void getListHistory(ApiCallback callback, long account_id) {
        try {
            String url = URL + "session/getAllHistory/" + account_id;
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

    public void getSessionDetail(ApiCallback callback, long session_id) {
        try {
            String url = URL + "session/detail/" + session_id;
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

    public void confirmSession(ApiCallback callback, long session_id, long account_id) {
        try {
            String url = URL + "session/confirm";
            GenericUrl requestUrl = new GenericUrl(url);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();

            Map<String, String> parameters = new HashMap<>();
            parameters.put("session_id", session_id + "");
            parameters.put("account_id", account_id + "");
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

    public void getListAssetBySession(ApiCallback callback, long session_id) {
        try {
            String url = URL + "asset/getBySession/" + session_id;
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

    public void doScan(ApiCallback callback, long account_id, String list) {
        try {
            String url = URL + "asset/doScan";
            GenericUrl requestUrl = new GenericUrl(url);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();

            Map<String, String> parameters = new HashMap<>();
            parameters.put("account_id", account_id + "");
            parameters.put("list", list);
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

    public void getListCategory(ApiCallback callback, long company_id) {
        try {
            String url = URL + "info/getCategories/" + company_id;
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

    public void getListDepartment(ApiCallback callback, long company_id) {
        try {
            String url = URL + "info/getDepartments/" + company_id;
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

    public void getListWarehouse(ApiCallback callback, long company_id) {
        try {
            String url = URL + "info/getWarehouses/" + company_id;
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

    public void editAsset(ApiCallback callback, long asset_id, String field_name, String data) {
        try {
            String url = URL + "asset/update";
            GenericUrl requestUrl = new GenericUrl(url);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();

            Map<String, String> parameters = new HashMap<>();
            parameters.put("asset_id", asset_id + "");
            parameters.put(field_name, data);
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

    public void getServerTime(ApiCallback callback) {
        try {
            String url = URL + "info/getServerTime";
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
