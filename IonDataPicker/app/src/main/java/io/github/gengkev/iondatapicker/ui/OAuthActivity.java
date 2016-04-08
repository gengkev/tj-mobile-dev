package io.github.gengkev.iondatapicker.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import io.github.gengkev.iondatapicker.R;
import io.github.gengkev.iondatapicker.network.OAuthHelper;

public class OAuthActivity extends AppCompatActivity {
    private static final String TAG = OAuthActivity.class.getName();
    static final String EXTRA_AUTH_URL = "io.github.gengkev.iondatapicker.EXTRA_AUTH_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);

        Intent intent = getIntent();
        String url = intent.getStringExtra(EXTRA_AUTH_URL);

        WebView webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new OAuthWebViewClient());

        webview.loadUrl(url);
    }

    private void handleOnComplete(Uri uri) {
        String query = uri.getQuery();
        if (query == null) {
            query = "";
        }

        UrlQuerySanitizer sanitizer = new UrlQuerySanitizer();
        sanitizer.setAllowUnregisteredParamaters(true);
        sanitizer.setUnregisteredParameterValueSanitizer(UrlQuerySanitizer.getAllButNulLegal());
        sanitizer.parseQuery(query);

        String code = sanitizer.getValue("code");
        String state = sanitizer.getValue("state");
        String error = sanitizer.getValue("error");

        if (code == null || state == null) {
            Toast.makeText(this, "Invalid query string: " + query, Toast.LENGTH_LONG).show();
            Log.e(TAG, String.format("Invalid query string: %s (%s, %s, %s)", query, code, state, error));
            setResult(Activity.RESULT_CANCELED);
        }
        else {
            Intent data = new Intent();
            data.putExtra("code", code);
            data.putExtra("state", state);
            data.putExtra("error", error);
            setResult(Activity.RESULT_OK, data);
        }

        finish();
    }

    private class OAuthWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri redirectUri = Uri.parse(OAuthHelper.REDIRECT_URL);
            Uri uri = Uri.parse(url);
            if (uri.getHost().equals(redirectUri.getHost()) &&
                    uri.getPath().equals(redirectUri.getPath())) {
                handleOnComplete(uri);
                return true;
            }
            return false;
        }
    }
}
