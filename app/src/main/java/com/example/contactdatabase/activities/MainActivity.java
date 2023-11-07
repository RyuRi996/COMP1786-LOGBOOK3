package com.example.contactdatabase.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.contactdatabase.R;
import com.example.contactdatabase.database.AppDatabase;
import com.example.contactdatabase.models.Detail;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ContactAdapter.OnClickListener {
    List<Detail> details;
    private AppDatabase appDatabase;
    private ContactAdapter adapter;
    RecyclerView recyclerView;
    FloatingActionButton addBtn,deleteAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addBtn = findViewById(R.id.AddButton);
        deleteAll = findViewById(R.id.deleteAll);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user_detail.db")
                .allowMainThreadQueries().build();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        details = appDatabase.detailDAO().getAllDetails();
        adapter = new ContactAdapter(details, this);
        recyclerView.setAdapter(adapter);

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAll();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onDeleteClick(Detail detail) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.baseline_delete_24)
                .setTitle(R.string.delete_1)
                .setMessage("Are you sure to delete contact " + detail.name + "?")
                .setPositiveButton(R.string.delete, (dialog, which)->{
                    appDatabase.detailDAO().deleteDetail(detail);
                    details.remove(detail);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Delete complete", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void deleteAll(){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.baseline_delete_24)
                .setTitle(R.string.delete_all)
                .setMessage("Are you sure to delete all contact?")
                .setPositiveButton(R.string.delete, (dialog, which)->{
                    appDatabase.detailDAO().deleteAll();
                    details.removeAll(details);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Delete complete", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

}