package com.example.ikuzo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {

    TextView unit_name;

    FirebaseUser user;
    DatabaseReference reference;

    ImageButton btn_send;
    EditText txt_send;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        intent = getIntent();
        String unitname = intent.getExtras().getString("unitname");
        String subname = intent.getExtras().getString("subname");
        String fPath = "Chats/" + subname + "/" +unitname;


        unit_name = findViewById(R.id.unit_name);
        unit_name.setText(unitname);

        btn_send = findViewById(R.id.btn_send);
        txt_send = findViewById(R.id.txt_send);
        user = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = txt_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(user.getUid(),msg,fPath);
                }else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                txt_send.setText("");
            }
        });



    }
    private void sendMessage(String sender, String message, String fPath){
        reference = FirebaseDatabase.getInstance("https://study-group-chat-room-2021-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("message",message);
        reference.child(fPath).push().setValue(hashMap);
    }
}