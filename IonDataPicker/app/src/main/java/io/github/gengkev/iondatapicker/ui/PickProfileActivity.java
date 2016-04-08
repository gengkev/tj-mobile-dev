package io.github.gengkev.iondatapicker.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import io.github.gengkev.iondatapicker.R;
import io.github.gengkev.iondatapicker.model.User;
import io.github.gengkev.iondatapicker.network.SearchUserTask;
import io.github.gengkev.iondatapicker.util.BetterAsyncTask;

public class PickProfileActivity extends AppCompatActivity {
    private static final String TAG = PickProfileActivity.class.getName();
    public static final String EXTRA_USER_ID = "userid";

    private String mAccessToken;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_profile);

        Intent intent = getIntent();
        mAccessToken = intent.getStringExtra("access_token");
        if (mAccessToken == null) {
            Toast.makeText(this, "No access token!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mListView = (ListView) findViewById(R.id.result_list);

        final EditText editText = (EditText) findViewById(R.id.search_field);
        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch(editText.getText().toString());
            }
        });
    }

    private void doSearch(String input) {
        new SearchUserTask(mAccessToken, new BetterAsyncTask.ResultListener<ArrayList<User>>() {
            @Override
            public void onSuccess(ArrayList<User> users) {
                displaySearchResults(users);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(PickProfileActivity.this, "Search failed: " + e, Toast.LENGTH_LONG).show();
            }
        }).execute(input);
    }

    private void displaySearchResults(final ArrayList<User> users) {
        ArrayList<String> values = new ArrayList<>();
        for (User user : users) {
            values.add(user.fullName);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                values);

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = users.get(position);
                Log.i(TAG, "Selected item: " + user.fullName + " (" + user.id + ")");
                sendResult(user);
            }
        });
    }

    private void sendResult(User user) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USER_ID, String.valueOf(user.id));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
