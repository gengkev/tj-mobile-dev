package io.github.gengkev.exampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Assignment6Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment6);

        // load list of planets into ArrayAdapter
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.assgn6_items_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attach adapter to spinner
        final Spinner spinner = (Spinner) findViewById(R.id.assgn6_spinner);
        spinner.setAdapter(adapter);

        // attack onclick listener to button
        Button button = (Button) findViewById(R.id.assgn6_select_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = spinner.getSelectedItemPosition();
                CharSequence item = adapter.getItem(itemPosition);
                Intent intent = new Intent(Assignment6Activity.this, Assignment6HelperActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, item);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String value = "0";
        if (data != null) {
            value = data.getStringExtra(Intent.EXTRA_TEXT);
        }

        TextView view = (TextView) findViewById(R.id.assgn6_result);
        view.setText(value);
    }
}
