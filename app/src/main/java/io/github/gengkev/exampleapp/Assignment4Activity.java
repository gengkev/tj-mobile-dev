package io.github.gengkev.exampleapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Assignment4Activity extends AppCompatActivity {
    private static final int N = 7;
    private String[] counterStrings;
    private String countFormat;

    // temporary
    private TextView[] counterViews;
    private int[] counts;

    // persistent
    private TextView[] counterViewsPersist;
    private int[] countsPersist;


    private int[] getCounts() {
        int[] arr = new int[N];
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        for (int i = 0; i < N; i++) {
            arr[i] = sharedPref.getInt("count" + i, 0);
        }
        return arr;
    }

    private void putCounts(int[] arr) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        for (int i = 0; i < N; i++) {
            editor.putInt("count" + i, arr[i]);
        }
        editor.apply();
    }

    private void displayCounts() {
        for (int i = 0; i < N; i++) {
            counterViews[i].setText(String.format(
                    countFormat, counterStrings[i], counts[i]));
            counterViewsPersist[i].setText(String.format(
                    countFormat, counterStrings[i], countsPersist[i]));
        }
    }

    private void update(int i) {
        counts[i]++;
        countsPersist[i]++;
        displayCounts();
        putCounts(countsPersist);

        Toast.makeText(this,
                String.format(countFormat, counterStrings[i], countsPersist[i]),
                Toast.LENGTH_SHORT
        ).show();
    }

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment4);

        countFormat = getString(R.string.assgn4_count_label);
        counterStrings = new String[] {
                getString(R.string.assgn4_onCreate),
                getString(R.string.assgn4_onStart),
                getString(R.string.assgn4_onResume),
                getString(R.string.assgn4_onPause),
                getString(R.string.assgn4_onStop),
                getString(R.string.assgn4_onRestart),
                getString(R.string.assgn4_onDestroy)
        };

        counterViews = new TextView[] {
                (TextView) findViewById(R.id.assgn4_onCreate_counter),
                (TextView) findViewById(R.id.assgn4_onStart_counter),
                (TextView) findViewById(R.id.assgn4_onResume_counter),
                (TextView) findViewById(R.id.assgn4_onPause_counter),
                (TextView) findViewById(R.id.assgn4_onStop_counter),
                (TextView) findViewById(R.id.assgn4_onRestart_counter),
                (TextView) findViewById(R.id.assgn4_onDestroy_counter)
        };
        counterViewsPersist = new TextView[] {
                (TextView) findViewById(R.id.assgn4_onCreate_counterPersist),
                (TextView) findViewById(R.id.assgn4_onStart_counterPersist),
                (TextView) findViewById(R.id.assgn4_onResume_counterPersist),
                (TextView) findViewById(R.id.assgn4_onPause_counterPersist),
                (TextView) findViewById(R.id.assgn4_onStop_counterPersist),
                (TextView) findViewById(R.id.assgn4_onRestart_counterPersist),
                (TextView) findViewById(R.id.assgn4_onDestroy_counterPersist)
        };

        counts = new int[N];
        countsPersist = getCounts();

        Button clearTemp = (Button) findViewById(R.id.clearTemp);
        clearTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counts = new int[N];
                displayCounts();
            }
        });

        Button clearPersist = (Button) findViewById(R.id.clearPersist);
        clearPersist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countsPersist = new int[N];
                displayCounts();
                putCounts(countsPersist);
            }
        });

        update(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        update(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        update(2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        update(3);
    }

    @Override
    protected void onStop() {
        super.onStop();
        update(4);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        update(5);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        update(6);
    }
}
