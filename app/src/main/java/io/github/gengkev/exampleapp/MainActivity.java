package io.github.gengkev.exampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

        final Class[] assgnActivities = {
                Assignment3Activity.class,
                Assignment4Activity.class,
                Assignment5Activity.class,
                Assignment6Activity.class
        };
        final String[] assgnTitles = {
                getString(R.string.assgn3_label),
                getString(R.string.assgn4_label),
                getString(R.string.assgn5_label),
                getString(R.string.assgn6_label)
        };
        final String[] assgnNums = {
                getString(R.string.assgn3_num),
                getString(R.string.assgn4_num),
                getString(R.string.assgn5_num),
                getString(R.string.assgn6_num)
        };


        // RecyclerView stuff
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        MainAdapter adapter = new MainAdapter(assgnNums, assgnTitles);
        recyclerView.setAdapter(adapter);

        // set click listener
        adapter.setClickListener(new MainAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(MainActivity.this, assgnActivities[position]);
                startActivity(intent);
            }
        });
    }

}
