package ui;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData login(UserData userData) throws ServerException {
        //this method expects a userData object with email = null
        LoginData loginData = new LoginData(userData.username(), userData.password());
        return makeRequest("POST", "/session", loginData, AuthData.class, null);
    }

    public void logout(AuthData authData) throws ServerException {
        String[] authHeader = {"authorization", authData.authToken()};
        makeRequest("DELETE", "/session", null, null, authHeader);
    }

    public AuthData registerUser(UserData userData) throws ServerException {
        return makeRequest("POST", "/user", userData, AuthData.class, null);
    }

    public int createGame(AuthData authData, String gameName) throws ServerException {
        String[] authHeader = {"authorization", authData.authToken()};
        GameName thisGameName = new GameName(gameName);
        GameData gameData = makeRequest("POST", "/game", thisGameName, GameData.class, authHeader);
        return gameData.gameID();
    }

    public Collection<GameData> listGames(AuthData authData) throws ServerException {
        GameListWrapper gamesList;
        String[] authHeader = {"authorization", authData.authToken()};
        gamesList = makeRequest("GET", "/game", null, GameListWrapper.class, authHeader);
        return gamesList.items;
    }

    public void joinGame(AuthData authData, JoinGameInput joinGameInput) throws ServerException {
        String[] authHeader = {"authorization", authData.authToken()};
        makeRequest("PUT", "/game", joinGameInput, null, authHeader);
    }

    public void clearDataBases() throws ServerException {
        makeRequest("DELETE", "/db", null, null, null);
    }

    static class GameListWrapper {
        @SerializedName("items")  // Use the actual key name in your JSON
        List<GameData> items;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass,
                              String[] headerString) throws ServerException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (headerString != null) {
                //headerString must be a two element String array.
                http.addRequestProperty(headerString[0], headerString[1]);
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ServerException ex) {
            throw new ServerException(ex.getMessage(), ex.getrCode());
        } catch (Exception ex) {
            throw new ServerException(ex.getMessage(), 500);
        }
    }


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
