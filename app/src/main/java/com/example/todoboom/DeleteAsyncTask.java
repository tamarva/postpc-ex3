package com.example.todoboom;

import android.os.AsyncTask;
import android.util.Log;

public class DeleteAsyncTask extends AsyncTask<Todo, Void, Void> {

    private MainActivity.TodoDao mTodoDao;
    private static final String TAG = "DeleteAsyncTask";

    DeleteAsyncTask(MainActivity.TodoDao todoDao){
        this.mTodoDao = todoDao;
    }

    @Override
    protected Void doInBackground(Todo... todos) {
        Log.d(TAG, "doInBackground: thread" + Thread.currentThread().getName());
        mTodoDao.deleteTodo(todos);
        return null;
    }
}
