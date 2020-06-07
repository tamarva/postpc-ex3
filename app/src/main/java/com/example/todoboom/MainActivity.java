package com.example.todoboom;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends Activity {
    public static final int INTENT_CODE = 000_000_007;
    public static final String KEY_TODO = "key_todo";
    public static final String KEY_DEL = "del_key";
    public static final String KEY_UPDATE = "update_key";
    private EditText editText;
    private List<Todo> mTodos;
    private TodoRepository repository;
    private Todo mTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repository = ((Application) getApplication()).getTodoRepo();
        mTodos = new ArrayList<>();

        editText = findViewById(R.id.editText);
        Button submitButton = findViewById(R.id.button);
        RecyclerView recyclerView = findViewById(R.id.todo_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final RecyclerView.Adapter adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
        repository.setActivity(new TodoRepository.FirebaseActivity() {
            @Override
            public void forwardData(List<Todo> list) {
                MainActivity.this.mTodos = list;
                adapter.notifyDataSetChanged();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = editText.getText().toString().trim();
                if (input.length() != 0) {
                    editText.setText("");
                    repository.add(input);
                } else {
                    Toast
                            .makeText(MainActivity.this,
                                    "you can't create TODO item with empty string",
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        if (requestCode == INTENT_CODE){
            if (resultCode == RESULT_OK) {
                if (dataIntent.getBooleanExtra(KEY_DEL, false)) {
                    repository.deleteItem(mTodo);
                } else {
                    Todo myTodo = dataIntent.getParcelableExtra(KEY_UPDATE);
                    if (myTodo != null) {
                        repository.edit(myTodo);
                    }
                }
            }
        }
        mTodo = null;
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private class ViewHolder extends RecyclerView.ViewHolder {
            private TextView listItem;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                listItem = itemView.findViewById(R.id.todo_text);
            }

            public void bind(final Todo todo) {
                String text = todo.isCompleted() ? "~" + todo.getContent() : todo.getContent();
                listItem.setText(text);
                listItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mTodo = todo;
                        Intent intent = new Intent(MainActivity.this,
                                todo.isCompleted() ? TodoMarkedActivity.class : TodoUnmarkedActivity.class);
                        intent.putExtra(KEY_TODO, todo);
                        startActivityForResult(intent, INTENT_CODE);
                    }
                });
            }
        }

        public MyAdapter() {
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View todoView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_one_todo, parent, false);
            return new ViewHolder(todoView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Todo todo = mTodos.get(position);
            holder.bind(todo);
        }

        @Override
        public int getItemCount() {
            return mTodos.size();
        }
    }
}
