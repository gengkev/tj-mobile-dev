package io.github.gengkev.iondatapicker.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import io.github.gengkev.iondatapicker.R;
import io.github.gengkev.iondatapicker.network.DownloadImageTask;
import io.github.gengkev.iondatapicker.network.GetTokenTask;
import io.github.gengkev.iondatapicker.network.OAuthHelper;
import io.github.gengkev.iondatapicker.util.BetterAsyncTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private static final int OAUTH_LAUNCH_REQUEST = 1;
    private static final int PROFILE_PICK_REQUEST = 2;

    private TextView mAuthStatus;
    private TextView mSearchStatus;

    private String mState = "";
    private String mAccessToken;
    private String mRefreshToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mAuthStatus = (TextView) findViewById(R.id.auth_status);
        mSearchStatus = (TextView) findViewById(R.id.search_status);

        Button launchButton = (Button) findViewById(R.id.launch_button);
        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchOAuthRequest();
            }
        });

        Button profileButton = (Button) findViewById(R.id.profile_pick_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PickProfileActivity.class);
                intent.putExtra("access_token", mAccessToken);
                startActivityForResult(intent, PROFILE_PICK_REQUEST);
            }
        });

        /*
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Query: " + query, Toast.LENGTH_LONG).show();
        }
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OAUTH_LAUNCH_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                handleOAuthResponse(data);
            } else {
                Toast.makeText(this, "Bad result: " + resultCode, Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == PROFILE_PICK_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                String userid = data.getStringExtra(PickProfileActivity.EXTRA_USER_ID);
                if (userid == null) {
                    Log.e(TAG, "Received null userid :(");
                    Toast.makeText(this, "You didn't pick anything! :(", Toast.LENGTH_LONG).show();
                } else {
                    handlePickResponse(userid);
                }
            } else {
                Toast.makeText(this, "Bad result: " + resultCode, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        /*
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        */

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void launchOAuthRequest() {
        mState = String.format("darnxd{%d}", new Date().getTime());

        Intent intent = new Intent(MainActivity.this, OAuthActivity.class);
        String authUrl = OAuthHelper.makeAuthURL(mState);
        intent.putExtra(OAuthActivity.EXTRA_AUTH_URL, authUrl);

        startActivityForResult(intent, OAUTH_LAUNCH_REQUEST);
    }

    private void handleOAuthResponse(Intent data) {
        String code = data.getStringExtra("code");
        String state = data.getStringExtra("state");
        String error = data.getStringExtra("error");

        Log.d(TAG, "handleOAuthResponse: code=" + code + ", state=" + state + ", error=" + error);

        if (error != null) {
            Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error: " + error);
            return;
        }
        if (state == null || !state.equals(mState)) {
            Toast.makeText(this, "Invalid state: " + state, Toast.LENGTH_LONG).show();
            Log.e(TAG, "Invalid state: " + state);
            return;
        }

        Toast.makeText(this, "Got authorization code: " + code, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Got authorization code: " + code);

        new GetTokenTask(new BetterAsyncTask.ResultListener<Pair<String,String>>() {

            @Override
            public void onSuccess(Pair<String, String> tokens) {
                final String accessToken = tokens.first;
                final String refreshToken = tokens.second;

                Toast.makeText(MainActivity.this, "Got tokens: " + accessToken + ", " + refreshToken, Toast.LENGTH_SHORT).show();
                mAuthStatus.setText("Authorized, with access token: " + accessToken);

                mAccessToken = accessToken;
                mRefreshToken = refreshToken;
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(MainActivity.this, "Error: " + e, Toast.LENGTH_LONG).show();
            }

        }).execute(code);
    }


    private void handlePickResponse(String userid) {
        mSearchStatus.setText("Picked user id: " + userid);

        new DownloadImageTask(mAccessToken, new BetterAsyncTask.ResultListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                // pass data back to picking app
                Intent data = new Intent(Intent.ACTION_GET_CONTENT, uri);
                setResult(Activity.RESULT_OK, data);
                finish();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(MainActivity.this, "Error: " + e, Toast.LENGTH_LONG).show();
            }

        }).execute(userid);
    }
}
