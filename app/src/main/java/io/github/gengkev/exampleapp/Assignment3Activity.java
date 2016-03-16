package io.github.gengkev.exampleapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Assignment3Activity extends AppCompatActivity {
    private static final String TAG = Assignment3Activity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment3);

        final Button button = (Button) findViewById(R.id.assgn3_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = getString(R.string.assgn3_button_pressed);
                Toast.makeText(Assignment3Activity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        
        final TextView[] textViews = {
                (TextView) findViewById(R.id.assgn3_textView1),
                (TextView) findViewById(R.id.assgn3_textView2),
                (TextView) findViewById(R.id.assgn3_textView3),
                (TextView) findViewById(R.id.assgn3_textView4)
        };
        final int[] counts = new int[textViews.length];

        @SuppressLint("ShowToast")
        final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        for (int i = 0; i < textViews.length; i++) {
            final int index = i;
            final TextView textView = textViews[i];

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = getString(R.string.assgn3_textview_pressed,
                            textView.getText(), ++counts[index]);

                    toast.setText(text);
                    toast.show();

                    Log.d(TAG, text);
                }
            });
        }

    }
}
