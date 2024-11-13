package ui;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
    private String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData login(UserData userData) throws ServerException {
        //this method expects a userData object with email = null
        LoginData loginData = new LoginData(userData.username(), userData.password());
        return makeRequest("POST", "/session", loginData, AuthData.class);
    }

    public AuthData registerUser(UserData userData) throws ServerException {
        AuthData authData = makeRequest("POST", "/user", userData, AuthData.class);
        return authData;

    }

    public void clearDataBases() throws ServerException {
        makeRequest("DELETE", "/db", null, null);
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ServerException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ServerException(ex.getMessage(), 500);
        }
    } //this code appears to


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());

            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ServerException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ServerException("failure: " + status, status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
