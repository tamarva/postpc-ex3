package com.example.todoboom;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TodoRepository {


    private MainActivity.TodoDatabase mTodoDatabase;


    TodoRepository(Context context){
        mTodoDatabase = MainActivity.TodoDatabase.getInstance(context);
    }

    void insertTodoTask(Todo todo){
        new InsertAsyncTask(mTodoDatabase.getTodoDao()).execute(todo);

    }

    void deleteTodo(Todo todo){
        new DeleteAsyncTask(mTodoDatabase.getTodoDao()).execute(todo);
    }

    void updateNote(Todo todo){
        new UpdateAsyncTask(mTodoDatabase.getTodoDao()).execute(todo);

    }

    LiveData<Integer> getCountRow() {
        return mTodoDatabase.getTodoDao().getCount();
    }

    LiveData<List<Todo>> retrieveTodos(){
        return mTodoDatabase.getTodoDao().getTodoNotes();
        //returns a livedata list of all todos inside the database
    }

}
