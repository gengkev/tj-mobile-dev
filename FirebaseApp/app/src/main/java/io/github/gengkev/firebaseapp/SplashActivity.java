package io.github.gengkev.firebaseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

public class SplashActivity extends AppCompatActivity {
    private final int LOGIN_ACTIVITY_CODE = 0;
    private final int REGISTER_ACTIVITY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        WebView background = (WebView) findViewById(R.id.troll_background_webview);
        assert background != null;
        background.getSettings().setJavaScriptEnabled(true);
        background.loadUrl("file:///android_asset/lel.html");


        // login button
        final Button loginButton = (Button) findViewById(R.id.login_button);
        assert loginButton != null;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivityForResult(intent, LOGIN_ACTIVITY_CODE);
            }
        });

        // sign up button
        final Button registerButton = (Button) findViewById(R.id.register_button);
        assert registerButton != null;
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                startActivityForResult(intent, REGISTER_ACTIVITY_CODE);
            }
        });

        Firebase ref = new Firebase(getString(R.string.firebase_url));
        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    openMainActivity();
                } else {
                    // user is not logged in; do nothing
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    void openMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
