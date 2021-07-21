package com.example.ikuzo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
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

    String unitname;
    String subname;
    String fPath;

    ImageButton btn_send;
    ImageButton btn_cam;
    EditText txt_send;


    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;

    Intent intent;

    Uri uri;

    private static final int PICK_IMAGE = 1;

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
        unitname = intent.getExtras().getString("unitname");
        subname = intent.getExtras().getString("subname");
        fPath = "Chats/" + subname + "/" +unitname;


        unit_name = findViewById(R.id.unit_name);
        unit_name.setText(unitname);

        btn_send = findViewById(R.id.btn_send);
        txt_send = findViewById(R.id.txt_send);
        btn_cam = findViewById(R.id.btn_cam);
        user = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = txt_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(user.getUid(),msg,user.getDisplayName(),fPath);
                }else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                txt_send.setText("");
            }
        });

        btn_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE);

            }
        });


        reference = FirebaseDatabase.getInstance("https://study-group-chat-room-2021-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

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
    private void sendMessage(String sender, String message, String senderName, String fPath){
        reference = FirebaseDatabase.getInstance("https://study-group-chat-room-2021-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("message",message);
        hashMap.put("senderName",senderName);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE || resultCode == RESULT_OK ||
                    data != null || data.getData() != null){
            uri = data.getData();

            String url = uri.toString();
            Intent intent = new Intent(MessageActivity.this, SendImage.class);
            intent.putExtra("url", url);
            intent.putExtra("sender", user.getUid());
            intent.putExtra("senderName", user.getDisplayName());
            intent.putExtra("fPath", fPath);
            startActivity(intent);
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }
}
