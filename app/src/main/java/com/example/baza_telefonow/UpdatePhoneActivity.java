package com.example.baza_telefonow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

public class UpdatePhoneActivity extends AppCompatActivity {
    private EditText mEditWordText;
    private EditText mEditProducerText;
    private EditText mEditAndVersionText;
    private EditText mEditWWWText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone);
        mEditWordText = findViewById(R.id.edit_word);
        mEditProducerText = findViewById(R.id.edit_producer);
        mEditAndVersionText = findViewById(R.id.edit_and_version);
        mEditWWWText = findViewById(R.id.edit_www);
        final Button button = findViewById(R.id.button_save);
        final Button butCancel = findViewById(R.id.button_cancel);
        final Button butOpenWebsite = findViewById(R.id.open_website);
        Intent main = getIntent();
        mEditWordText.setText(main.getStringExtra("model"));
        mEditProducerText.setText(main.getStringExtra("producer"));
        mEditAndVersionText.setText(main.getStringExtra("andver"));
        mEditWWWText.setText(main.getStringExtra("www"));
        button.setOnClickListener(view -> {
            if (TextUtils.isEmpty(mEditWordText.getText().toString())
                    || TextUtils.isEmpty(mEditProducerText.getText().toString())
                    || TextUtils.isEmpty(mEditAndVersionText.getText().toString())
                    ||TextUtils.isEmpty(mEditWWWText.getText().toString())) {
                mEditWordText.setError(getString(R.string.error_word));
                return;
            }
            Intent replyIntent = new Intent();
            String model = mEditWordText.getText().toString();
            String producer = mEditProducerText.getText().toString();
            String andvder = mEditAndVersionText.getText().toString();
            String www = mEditWWWText.getText().toString();
            replyIntent.putExtra("model", model);
            replyIntent.putExtra("producer", producer);
            replyIntent.putExtra("andver", andvder);
            replyIntent.putExtra("www", www);
            replyIntent.putExtra("id", main.getLongExtra("id",-1));
            setResult(RESULT_OK, replyIntent);
            finish();
        });
        butCancel.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        butOpenWebsite.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+mEditWWWText.getText().toString()));
            startActivity(browserIntent);
        });
    }
}