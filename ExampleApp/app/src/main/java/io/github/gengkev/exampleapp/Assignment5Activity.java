package io.github.gengkev.exampleapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
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
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    vibratorService.vibrate(20);
                    view.incrementAndUpdate();
                    return false;
                }
            });
        }
    }
}
