package io.github.gengkev.exampleapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

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

        final Button[] assgnButtons = new Button[] {
                (Button) findViewById(R.id.assgn3_launch_button),
                (Button) findViewById(R.id.assgn4_launch_button),
                (Button) findViewById(R.id.assgn5_launch_button)
        };
        final Class[] assgnActivities = {
                Assignment3Activity.class,
                Assignment4Activity.class,
                Assignment5Activity.class
        };

        for (int i = 0; i < assgnButtons.length; i++) {
            final int index = i;
            assgnButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, assgnActivities[index]);
                    startActivity(intent);
                }
            });
        }
    }

}
