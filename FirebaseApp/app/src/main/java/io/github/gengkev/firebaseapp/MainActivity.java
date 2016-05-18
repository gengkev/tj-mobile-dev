package io.github.gengkev.firebaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    static Firebase mFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // yey firebase
        mFirebaseRef = new Firebase(getString(R.string.firebase_url));
        final Firebase messageRef = mFirebaseRef.child("message");

        // create intro text
        TextView introText = (TextView) findViewById(R.id.intro_text);
        assert introText != null;

        AuthData data = mFirebaseRef.getAuth();
        Map<String, Object> authData = data.getProviderData();
        introText.setText(getString(R.string.intro_text, (String)authData.get("email")));

        // get refs to view elements
        Button pushButton = (Button) findViewById(R.id.push_button);
        assert pushButton != null;
        final EditText inputBox = (EditText) findViewById(R.id.input_box);
        assert inputBox != null;

        Button pullButton = (Button) findViewById(R.id.pull_button);
        assert pullButton != null;
        final EditText outputBox = (EditText) findViewById(R.id.output_box);
        assert outputBox != null;

        // push data to firebase
        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = inputBox.getText().toString();
                messageRef.setValue(value);
            }
        });

        // pull data from firebase
        pullButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        outputBox.setText(snapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(FirebaseError err) {
                        Log.e(TAG, "Firebase error: " + err);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        } else if (id == R.id.action_logout) {
            mFirebaseRef.unauth();
            finish();
            return true;
        } else if (id == R.id.action_change_password) {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
