package io.github.gengkev.exampleapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Assignment6HelperActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment6_helper);

        TextView message = (TextView) findViewById(R.id.assgn6_message);

        // load message from intent
        Intent intent = getIntent();
        CharSequence item = intent.getCharSequenceExtra(Intent.EXTRA_TEXT);
        if (item == null) {
            item = "(no value)";
        }
        message.setText(item);

        // add submit listener
        Button submit = (Button) findViewById(R.id.assgn6_submit);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText entry = (EditText) findViewById(R.id.assgn6_entry);
        String value = entry.getText().toString();
        Log.i(Assignment6HelperActivity.class.getName(), "got value: " + value);

        // set result
        Intent result = new Intent(Intent.ACTION_DEFAULT);
        result.putExtra(Intent.EXTRA_TEXT, value);
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
