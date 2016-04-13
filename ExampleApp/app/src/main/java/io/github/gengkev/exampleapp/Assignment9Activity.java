package io.github.gengkev.exampleapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Assignment9Activity extends AppCompatActivity {
    private static final String TAG = Assignment9Activity.class.getName();

    private TextView mStatusText;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment9);

        mStatusText = (TextView) findViewById(R.id.assgn9_status_text);
        mProgressBar = (ProgressBar) findViewById(R.id.assgn9_progressBar);

        new ProgressTask().execute();
    }

    class ProgressTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mStatusText.setText(R.string.assgn9_initial_text);
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i <= 100; i++) {
                try {
                    Thread.sleep(10 + (int) (Math.random() * 200));
                } catch (InterruptedException e) {
                    Log.e(TAG, "Sleep interrupted", e);
                }
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressBar.setProgress(values[0]);
            mStatusText.setText(getString(R.string.assgn9_progress_text, 1.0 * values[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mStatusText.setText(getString(R.string.assgn9_done_text));
        }
    }
}
