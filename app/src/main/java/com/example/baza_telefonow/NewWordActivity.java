package com.example.baza_telefonow;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class NewWordActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY =
            "com.example.android.wordlistsql.REPLY";
    private EditText mEditWordText;
    private EditText mEditProducerText;
    private EditText mEditAndVersionText;
    private EditText mEditWWWText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);
        mEditWordText = findViewById(R.id.edit_word);
        mEditProducerText = findViewById(R.id.edit_producer);
        mEditAndVersionText = findViewById(R.id.edit_and_version);
        mEditWWWText = findViewById(R.id.edit_www);
        final Button button = findViewById(R.id.button_save);
        final Button butCancel = findViewById(R.id.button_cancel);
        button.setOnClickListener(view -> {
            if (TextUtils.isEmpty(mEditWordText.getText().toString())||TextUtils.isEmpty(mEditProducerText.getText().toString())) {
                mEditWordText.setError(getString(R.string.error_word));
                return;
            }
            Intent replyIntent = new Intent();
            String word = mEditWordText.getText().toString();
            String producer = mEditProducerText.getText().toString();
            String andvder = mEditAndVersionText.getText().toString();
            String www = mEditWWWText.getText().toString();
            replyIntent.putExtra(EXTRA_REPLY, word);
            replyIntent.putExtra("producer", producer);
            replyIntent.putExtra("andver", andvder);
            replyIntent.putExtra("www", www);
            setResult(RESULT_OK, replyIntent);
            finish();
        });
        butCancel.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}