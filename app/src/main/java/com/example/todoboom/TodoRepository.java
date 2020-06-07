package com.example.todoboom;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TodoRepository {
    public interface FirebaseActivity {
        void forwardData(List<Todo> list);
    }

    private List<Todo> todosList = new ArrayList<>();
    public FirebaseFirestore database;
    private FirebaseActivity activity;

    public TodoRepository() {
        database = FirebaseFirestore.getInstance();
        getFromServer();
    }

    private CollectionReference getFromServer() {
        CollectionReference collectionReference = database.collection("TODO_COLLECTION");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e == null && queryDocumentSnapshots == null) {
                    todosList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Todo todo = document.toObject(Todo.class);
                        todosList.add(todo);
                    }
                }
            }
        });

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (activity != null) {
                    activity.forwardData(todosList);
                }
            }
        });
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (activity != null) {
                    activity.forwardData(todosList);
                }
            }
        });
        return collectionReference;
    }

    public void add(String text) {
        DocumentReference doc = database.collection("TODO_COLLECTION").document();
        String id = doc.getId();
        Todo todoItem = new Todo(id, text);
        todosList.add(todoItem);
        database.collection("TODO_COLLECTION").add(todoItem.myData());
        if (activity != null) {
            activity.forwardData(todosList);
        }
    }

    public void deleteItem(Todo item) {
        todosList.remove(find(item));
        database.collection("TODO_COLLECTION").document(item.getId()).delete();
        if (activity != null) {
            activity.forwardData(todosList);
        }
    }

    public void edit(Todo item) {
        todosList.set(find(item), item);
        database.collection("TODO_COLLECTION").document(item.getId()).set(item.myData());
        if (activity != null) {
            activity.forwardData(todosList);
        }
    }

    public int find(Todo item) {
        for (int i = 0; i < todosList.size(); i++) {
            if (todosList.get(i).getId().equals(item.getId())) {
                return i;
            }
        }
        return -1;
    }

    public int getItemsCount() {
        return todosList.size();
    }

    public void setActivity(FirebaseActivity activity) {
        this.activity = activity;
    }
}
