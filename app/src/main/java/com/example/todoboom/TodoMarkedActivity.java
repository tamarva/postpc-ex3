package com.example.todoboom;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class TodoMarkedActivity extends Activity {
    Todo todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_marked);
        Intent intent = getIntent();
        todo = intent.getParcelableExtra(MainActivity.KEY_TODO);

        ((TextView)findViewById(R.id.text_view_creation_time_stamp)).setText("Creation date: " + todo.getCreatedOn());
        ((TextView)findViewById(R.id.text_view_content)).setText("text: " + todo.getContent());
        ((TextView)findViewById(R.id.text_view_id)).setText("id: " + todo.getId());
        ((TextView)findViewById(R.id.text_view_edit_time_stamp)).setText("Edit date: " + todo.getLastEdit());

        Button button = findViewById(R.id.button_delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(TodoMarkedActivity.this)
                        .setTitle("WARNING!")
                        .setMessage("NO TURNING BACK")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.putExtra(MainActivity.KEY_DEL, true);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });

        Button button_unmark = findViewById(R.id.button_unmark);
        button_unmark.setOnClickListener(new View.OnClickListener() {
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