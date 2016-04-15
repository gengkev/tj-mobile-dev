package io.github.gengkev.firebaseapp;

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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

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

        // yey firebase
        Firebase.setAndroidContext(this);
        final Firebase myFirebaseRef = new Firebase("https://pandowdy.firebaseio.com/");
        final Firebase messageRef = myFirebaseRef.child("message");

        // get refs to view elements
        Button pushButton = (Button) findViewById(R.id.push_button);
        final EditText inputBox = (EditText) findViewById(R.id.input_box);

        Button pullButton = (Button) findViewById(R.id.pull_button);
        final EditText outputBox = (EditText) findViewById(R.id.output_box);

        assert pushButton != null;
        assert inputBox != null;
        assert pullButton != null;
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
        }

        return super.onOptionsItemSelected(item);
    }
}
