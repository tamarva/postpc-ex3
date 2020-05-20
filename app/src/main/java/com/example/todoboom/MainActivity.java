package com.example.todoboom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Update;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Todo mInitialTodo;
    private String text;
    private TodoAdapter adapter;
    private Todo mExistedTodo;
    private TodoRepository mTodoRepository;
    private boolean mIsNewTodo;
    private ArrayList<Todo> mTodos = new ArrayList<>();

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.button);
        adapter = new TodoAdapter(mTodos);
        final RecyclerView recyclerView = findViewById(R.id.todo_recycle);
        mTodoRepository = new TodoRepository(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        retrieveTodos();
        getTodoCount();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text = editText.getText().toString();

                if (!text.equals("")) {
                    mInitialTodo = new Todo(text, false);
                    mIsNewTodo = true;
                    saveChanges(); // saving to db
                    editText.setText("");
                    retrieveTodos();
                    return;
                }

                Toast.makeText(getApplicationContext(),
                        "you can't create TODO item with empty string",
                        Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setItemClickListener(new onItemClickListener() {
            @Override
            public void onItemClick(Todo todo, TodoAdapter.TodoHolder todoHolder) {
                if (!todo.getIsDone()) {
                    todo.setIsDone(true);
                    todoHolder.textView.setTextColor(Color.parseColor("#5CC615"));
                    Toast.makeText(getApplicationContext(), "TODO " + todo.getContent() +
                            " is now DONE. BOOM!", Toast.LENGTH_SHORT).show();
                    mExistedTodo = todo;
                    saveUpdatedTodo();

                }
            }
        });

        adapter.setItemLongClickListener(new onItemLongClickListener() {
            @Override
            public boolean onItemLongClick(Todo todo, TodoAdapter.TodoHolder todoHolder) {
                Dialog dialog = new Dialog(todo, mTodos, adapter, mTodoRepository);
                dialog.show(getSupportFragmentManager(), "dialog");
                return true;
            }
        });
    }


    private void saveChanges(){
        if(mIsNewTodo)
        {
            saveNewTodo(); // insert new node to table
            mIsNewTodo = false;
        }
//        else{
//            saveUpdatedTodo();
//        }
    }

    private void saveNewTodo()
    {
        mTodoRepository.insertTodoTask(mInitialTodo);
    }

    private void saveUpdatedTodo()
    {
        mTodoRepository.updateNote(mExistedTodo);
    }

    private void getTodoCount(){
        mTodoRepository.getCountRow().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d(TAG, "get count of todo list: " + integer);

            }
        });
    }

    private void retrieveTodos(){
        mTodoRepository.retrieveTodos().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
                // called when a change occurs to the livedata and also in the first time we attach the observer
                if (mTodos.size() > 0) // because if there are already todos im gonna requery them
                {
                    mTodos.clear();
                }

                if (todos != null) {
                    mTodos.addAll(todos);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    //-----------------ADAPTER---------------//

    class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoHolder> {
        private ArrayList<Todo> mTodos;
        private onItemClickListener itemClickListener;
        private onItemLongClickListener itemLongClickListener;

        public TodoAdapter(ArrayList<Todo> todos){
            this.mTodos = todos;
        }


        void setItemClickListener(onItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }


        void setItemLongClickListener(onItemLongClickListener itemLongClickListener){
            this.itemLongClickListener = itemLongClickListener;
        }


        @NonNull
        @Override
        public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            LayoutInflater todoInflater = LayoutInflater.from(parent.getContext());
            View view = todoInflater.inflate(R.layout.item_one_todo, parent, false);
            return new TodoHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull final TodoHolder holder, int position) {
            final Todo todo = mTodos.get(position);
            holder.textView.setText(todo.getContent());

            if (todo.getIsDone())
            {
                holder.textView.setTextColor(Color.parseColor("#5CC615"));

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(todo, holder);
                    }
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (itemLongClickListener != null){
                        itemLongClickListener.onItemLongClick(todo, holder);
                    }
                    return true;
                }
            });
        }


        @Override
        public int getItemCount()
        {
            return mTodos.size();
        }


        private class TodoHolder extends RecyclerView.ViewHolder
        {
            TextView textView;


            TodoHolder(View view)
            {
                super(view);
                textView = view.findViewById(R.id.todo_text);
            }
        }
    }


    public interface onItemClickListener
    {
        void onItemClick(Todo todo, TodoAdapter.TodoHolder todoHolder);
    }


    public interface onItemLongClickListener{
        boolean onItemLongClick(Todo todo, TodoAdapter.TodoHolder todoHolder);
    }


    //------------------DAO------------------//
    @Dao
    public interface TodoDao {

        @Insert
        void insertTodo(Todo... todoNote);

        @Delete
        void deleteTodo(Todo... todoNote);

        @Query("SELECT * FROM todoNotes")
        LiveData<List<Todo>> getTodoNotes();

        @Query("SELECT COUNT(*) FROM todoNotes")
        LiveData<Integer> getCount();

        @Update
        void updateTodo(Todo... todoNote);
    }


    //------------------DATABASE------------------//

    @Database(entities = {Todo.class}, version =1)
    abstract static class TodoDatabase extends RoomDatabase{ // TODO CHECK IF ITS OK BEING STATIC OR TAKE OFF

        static  final String DATABASE_NAME = "todo_db";

        private static TodoDatabase instance;

        static TodoDatabase getInstance(final Context context){
            if (instance == null)
            {
                instance = Room.databaseBuilder(context.getApplicationContext(), TodoDatabase.class,
                        DATABASE_NAME).build();
            }
            return instance;
        }

        public abstract TodoDao getTodoDao(); // reference to dao
    }


    //------------------REPOSITORY------------------//

    public class TodoRepository{
        private TodoDatabase mTodoDatabase;


        public TodoRepository(Context context){
            mTodoDatabase = TodoDatabase.getInstance(context);
        }

        public void insertTodoTask(Todo todo){
            new InsertAsyncTask(mTodoDatabase.getTodoDao()).execute(todo);

        }

        public void deleteTodo(Todo todo){
            new DeleteAsyncTask(mTodoDatabase.getTodoDao()).execute(todo);
        }

        public void updateNote(Todo todo){
            new UpdateAsyncTask(mTodoDatabase.getTodoDao()).execute(todo);

        }

        public LiveData<Integer> getCountRow() {
            return mTodoDatabase.getTodoDao().getCount();
        }

        public LiveData<List<Todo>> retrieveTodos(){
            return mTodoDatabase.getTodoDao().getTodoNotes();
            //returns a livedata list of all todos inside the database
        }
    }


    public class InsertAsyncTask extends AsyncTask<Todo, Void, Void> {

        private TodoDao mTodoDao;

        public InsertAsyncTask(TodoDao todoDao){
            this.mTodoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            mTodoDao.insertTodo(todos);
            return null;
        }
    }


    public class DeleteAsyncTask extends AsyncTask<Todo, Void, Void> {

        private TodoDao mTodoDao;
        private static final String TAG = "DeleteAsyncTask";

        public DeleteAsyncTask(TodoDao todoDao){
            this.mTodoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            Log.d(TAG, "doInBackground: thread" + Thread.currentThread().getName());
            mTodoDao.deleteTodo(todos);
            return null;
        }
    }

    public class UpdateAsyncTask extends AsyncTask<Todo, Void, Void> {

        private TodoDao mTodoDao;


        public UpdateAsyncTask(TodoDao todoDao){
            this.mTodoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            mTodoDao.updateTodo(todos);
            return null;
        }
    }
}





