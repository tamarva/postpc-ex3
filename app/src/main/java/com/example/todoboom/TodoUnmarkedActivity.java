package com.example.todoboom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TodoUnmarkedActivity extends AppCompatActivity {
    Todo todo;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_unmarked);
        Intent intent = getIntent();
        todo = intent.getParcelableExtra(MainActivity.KEY_TODO);

        ((TextView)findViewById(R.id.text_view_creation_time_stamp)).setText("Creation date: " + todo.getCreatedOn());
        ((TextView)findViewById(R.id.text_view_content)).setText("text: " + todo.getContent());
        ((TextView)findViewById(R.id.text_view_id)).setText("id: " + todo.getId());
        ((TextView)findViewById(R.id.text_view_edit_time_stamp)).setText("Edit date: " + todo.getLastEdit());


        editText = findViewById(R.id.editText);
        Button applyButton = findViewById(R.id.button_apply);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString().trim();
                if (text.length() != 0) {
                    editText.setText("");
                    todo.setContent(text);
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.KEY_UPDATE, todo);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(TodoUnmarkedActivity.this, "C'mon, say something", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        Button markButton = findViewById(R.id.button_mark_todo);
        markButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todo.changeDone();
                Intent intent = new Intent();
                intent.putExtra(MainActivity.KEY_UPDATE, todo);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}

