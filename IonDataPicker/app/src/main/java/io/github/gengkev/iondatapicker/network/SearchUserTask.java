package io.github.gengkev.iondatapicker.network;

import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import io.github.gengkev.iondatapicker.model.User;
import io.github.gengkev.iondatapicker.util.BetterAsyncTask;

public class SearchUserTask extends BetterAsyncTask<String, Void, ArrayList<User>> {
    private static final String TAG = SearchUserTask.class.getName();

    private String mAccessToken;

    public SearchUserTask(String accessToken, ResultListener<ArrayList<User>> listener) {
        super(listener);
        mAccessToken = accessToken;
    }

    @Override
    protected ArrayList<User> doInBackground(String... params) {
        if (params.length != 1) {
            throw new IllegalArgumentException();
        }
        String input = params[0];

        ArrayList<User> users;
        try {
            users = OAuthHelper.searchUsers(input, mAccessToken);
        }
        catch (IOException | JSONException e) {
            Log.e(TAG, "Error searching: " + e);
            setException(e);
            return null;
        }
        return users;
    }
}
