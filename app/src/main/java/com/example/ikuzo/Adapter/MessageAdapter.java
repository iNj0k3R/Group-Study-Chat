package com.example.ikuzo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ikuzo.R;
import com.example.ikuzo.models.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    public static final int IMG_TYPE_LEFT = 3;
    public static final int IMG_TYPE_RIGHT = 4;

    private final Context mContext;
    private List<Chat> mChat;

    FirebaseUser user;

    public MessageAdapter(Context mContext, List<Chat> mChat){
        this.mChat = mChat;
        this.mContext = mContext;
    }

    @NonNull
    @NotNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
//        if (viewType == MSG_TYPE_RIGHT){
//            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
//            return new MessageAdapter.ViewHolder(view);
//        } else {
//            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
//            return new MessageAdapter.ViewHolder(view);
//        }
        switch (viewType){
            case MSG_TYPE_LEFT:
                view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
                break;
            case MSG_TYPE_RIGHT:
                view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
                break;
            case IMG_TYPE_LEFT:
                view = LayoutInflater.from(mContext).inflate(R.layout.chat_image_left, parent, false);
                break;
            case IMG_TYPE_RIGHT:
                view = LayoutInflater.from(mContext).inflate(R.layout.chat_image_right, parent, false);
                break;
        }
        return new MessageAdapter.ViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = mChat.get(position);

        holder.sender_name.setText(chat.getSenderName());
        if(holder.viewType == MSG_TYPE_LEFT || holder.viewType ==MSG_TYPE_RIGHT) {

            holder.show_message.setText(chat.getMessage());
        } else {
            Glide.with(mContext).load(chat.getImageUrl()).into(holder.show_image);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView show_image;
        public TextView sender_name;
        int viewType;

        public ViewHolder(View itemView, int viewType){
            super(itemView);
            this.viewType = viewType;

                if(viewType == MSG_TYPE_LEFT || viewType == MSG_TYPE_RIGHT) {
                    sender_name = itemView.findViewById(R.id.sender_name);
                    show_message = itemView.findViewById(R.id.show_message);
                }
                else {
                    sender_name = itemView.findViewById(R.id.sender_name);
                    show_image = itemView.findViewById(R.id.show_image);
                }

        }
    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getImageUrl() == null) {
            if (mChat.get(position).getSender().equals(user.getUid())) {
                return MSG_TYPE_RIGHT;
            } else {
                return MSG_TYPE_LEFT;
            }
        }
        else {
            if (mChat.get(position).getSender().equals(user.getUid())) {
                return IMG_TYPE_RIGHT;
            } else {
                return IMG_TYPE_LEFT;
            }
        }
    }
}
