package com.example.ikuzo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ikuzo.Adapter.MessageAdapter;
import com.example.ikuzo.models.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    TextView unit_name;

    FirebaseUser user;
    DatabaseReference reference;

    ImageButton btn_send;
    EditText txt_send;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

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
        reference = FirebaseDatabase.getInstance("https://study-group-chat-room-2021-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(fPath);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                readMessages(user.getUid(),fPath);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

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

    private void readMessages(String myid, String fPath){
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance("https://study-group-chat-room-2021-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(fPath);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    mChat.add(chat);
                }
                messageAdapter = new MessageAdapter(MessageActivity.this,mChat);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}