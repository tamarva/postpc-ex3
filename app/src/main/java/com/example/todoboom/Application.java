package com.example.todoboom;

import android.util.Log;

public class Application extends android.app.Application {
    private static final String TAG = Application.class.getSimpleName();
    private TodoRepository todoRepo;

    @Override
    public void onCreate() {
        super.onCreate();
        todoRepo = new TodoRepository();
        int numTodos = todoRepo.getItemsCount();
        Log.d(TAG, "Num todos: " + numTodos);
    }

    public TodoRepository getTodoRepo() {
        return todoRepo;
    }
}


