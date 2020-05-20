package com.example.todoboom;

import android.os.AsyncTask;

public class InsertAsyncTask extends AsyncTask<Todo, Void, Void> {

    private MainActivity.TodoDao mTodoDao;

    InsertAsyncTask(MainActivity.TodoDao todoDao){
        this.mTodoDao = todoDao;
    }

    @Override
    protected Void doInBackground(Todo... todos) {
        mTodoDao.insertTodo(todos);
        return null;
    }
}
