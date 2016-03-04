package io.github.gengkev.exampleapp;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Assignment5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment5);

        final MyTextView[] views = {
                (MyTextView) findViewById(R.id.assgn5_view1),
                (MyTextView) findViewById(R.id.assgn5_view2),
                (MyTextView) findViewById(R.id.assgn5_view3),
                (MyTextView) findViewById(R.id.assgn5_view4)
        };

        final Vibrator vibratorService = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        for (final MyTextView view : views) {
            final Context context = this;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibratorService.vibrate(50);
                    view.incrementAndUpdate();
                }
            });
        }
    }
}
