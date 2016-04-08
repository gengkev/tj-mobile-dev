package io.github.gengkev.iondatapicker.network;

import android.support.v4.util.Pair;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.github.gengkev.iondatapicker.util.BetterAsyncTask;


public class GetTokenTask extends BetterAsyncTask<String, Void, Pair<String, String>> {
    private static final String TAG = GetTokenTask.class.getName();

    public GetTokenTask(ResultListener<Pair<String, String>> listener) {
        super(listener);
    }

    @Override
    protected Pair<String, String> doInBackground(String... params) {
        String code = params[0];
        String result;
        try {
            result = OAuthHelper.getAuthToken(code);
        }
        catch (IOException e) {
            Log.e(TAG, "Error getting auth token", e);
            setException(e);
            return null;
        }

        String accessToken;
        String refreshToken;
        try {
            JSONObject obj = new JSONObject(result);
            accessToken = obj.getString("access_token");
            refreshToken = obj.getString("refresh_token");
        }
        catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON", e);
            setException(e);
            return null;
        }

        Log.d(TAG, "Got tokens: " + accessToken + ", " + refreshToken);

        return new Pair<>(accessToken, refreshToken);
    }
}
