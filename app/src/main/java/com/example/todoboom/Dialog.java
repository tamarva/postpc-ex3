package com.example.todoboom;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;

public class Dialog extends AppCompatDialogFragment {

    private Todo mTodo;
    private ArrayList<Todo> mTodos;
    private MainActivity.TodoAdapter mAdapter;
    private TodoRepository mTodoRepository;

    public Dialog(Todo todo, ArrayList<Todo> todos, MainActivity.TodoAdapter adapter, TodoRepository repo)
    {
        this.mTodo = todo;
        this.mTodos = todos;
        this.mAdapter = adapter;
        this.mTodoRepository = repo;

    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert!").setMessage("Are you sure to delete?")
                .setPositiveButton("YES!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTodos.remove(mTodo);
                        mAdapter.notifyDataSetChanged();
                        mTodoRepository.deleteTodo(mTodo);
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}




