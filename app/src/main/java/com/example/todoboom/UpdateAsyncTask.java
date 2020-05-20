package com.example.todoboom;

import android.os.AsyncTask;

public class UpdateAsyncTask extends AsyncTask<Todo, Void, Void> {

    private MainActivity.TodoDao mTodoDao;


    UpdateAsyncTask(MainActivity.TodoDao todoDao){
        this.mTodoDao = todoDao;
    }

    @Override
    protected Void doInBackground(Todo... todos) {
        mTodoDao.updateTodo(todos);
        return null;
    }
}
