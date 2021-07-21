package com.example.ikuzo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SendImage extends AppCompatActivity {

    String url, sender, senderName, fPath;
    ImageView imageView;
    Uri imageurl;
    ProgressBar progressBar;
    Button button;
    TextView textView;
    UploadTask uploadTask;
    Intent intent;

    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_image);

        storageReference = firebaseStorage.getInstance().getReference("Images");

        imageView = findViewById(R.id.iv_sendImage);
        button = findViewById(R.id.btn_send_image);
        progressBar = findViewById(R.id.pb_send_image);
        textView = findViewById(R.id.tv_warning);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
            sender = bundle.getString("sender");
            senderName = bundle.getString("senderName");
            fPath = bundle.getString("fPath");
        } else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
        Glide.with(SendImage.this).load(url).into(imageView);
        imageurl = Uri.parse(url);

        databaseReference = FirebaseDatabase.getInstance("https://study-group-chat-room-2021-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child(fPath);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendImage();
                textView.setVisibility(View.VISIBLE);
            }
        });
    }

    private String getFileExt(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void sendImage() {

        if(imageurl != null){
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExt(imageurl));
            uploadTask = reference.putFile(imageurl);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("sender",sender);
                        hashMap.put("message","");
                        hashMap.put("senderName",senderName);
                        hashMap.put("imageUrl",downloadUri.toString());
                        databaseReference.push().setValue(hashMap);

                        progressBar.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.INVISIBLE);
                        finish();
                    }
                }
            });
        } else {
            Toast.makeText(this,"please select some image", Toast.LENGTH_SHORT).show();
        }
    }
}