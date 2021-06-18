package com.example.ikuzo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class UnitsActivity extends AppCompatActivity {

    TextView subject_name;
    String[] unitArray = {"UNIT 1","UNIT 2","UNIT 3","UNIT 4", "UNIT 5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_units);

        Intent intent = getIntent();
        String subname = intent.getExtras().getString("subname");

        subject_name = findViewById(R.id.subject_name);

        subject_name.setText(subname);

        ArrayAdapter unitAdapter = new ArrayAdapter<>(this,
                R.layout.activity_listview, unitArray);
        ListView listView = findViewById(R.id.unit_listview);
        listView.setAdapter(unitAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(UnitsActivity.this, MessageActivity.class);
                intent.putExtra("subname", subname);
                intent.putExtra("unitname", selectedItem);
                startActivity(intent);
            }
        });
    }
}