package com.example.chatappmongodb.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.chatappmongodb.Models.Message;
import com.example.chatappmongodb.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.ChattingViewHolder>{

    ArrayList<Message> listMessage;
    Context context;

    public ChattingAdapter(ArrayList<Message> listMessage, Context context) {
        this.listMessage = listMessage;
        this.context = context;
    }

    @NonNull
    @Override
    public ChattingAdapter.ChattingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_message, parent, false);
        return new ChattingViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChattingAdapter.ChattingViewHolder holder, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("myId", Context.MODE_PRIVATE);
        String myId = sharedPreferences.getString("myId", "");
        Message message = listMessage.get(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm ");
        if (message!=null) {
            if (message.getHas_images().equals("false") && message.getSender_id().equals(myId)) {
                holder.civAvatarFriendChatting.setVisibility(View.GONE);
                holder.tvSmsOfFriend.setVisibility(View.GONE);
                holder.tvTimeFriendSendMessage.setVisibility(View.GONE);
                holder.ivFriendChattingSend.setVisibility(View.GONE);
                holder.tvTimeFriendSendImage.setVisibility(View.GONE);
                holder.tvSmsOfMe.setVisibility(View.VISIBLE);
                holder.tvSmsOfMe.setText(message.getMessage());
                holder.tvTimeISendMessage.setVisibility(View.VISIBLE);
                holder.tvTimeISendMessage.setText(simpleDateFormat.format(message.getCreatedAt()));
                holder.ivImageISend.setVisibility(View.GONE);
                holder.tvTimeISendImage.setVisibility(View.GONE);
            } else if (message.getHas_images().equals("false") && !message.getSender_id().equals(myId)) {
                holder.civAvatarFriendChatting.setVisibility(View.VISIBLE);
                holder.civAvatarFriendChatting.setImageResource(R.drawable.default_avatar);
                holder.tvSmsOfFriend.setVisibility(View.VISIBLE);
                holder.tvSmsOfFriend.setText(message.getMessage());
                holder.tvTimeFriendSendMessage.setVisibility(View.VISIBLE);
                holder.tvTimeFriendSendMessage.setText(simpleDateFormat.format(message.getCreatedAt()));
                holder.ivFriendChattingSend.setVisibility(View.GONE);
                holder.tvTimeFriendSendImage.setVisibility(View.GONE);
                holder.tvSmsOfMe.setVisibility(View.GONE);
                holder.tvTimeISendMessage.setVisibility(View.GONE);
                holder.ivImageISend.setVisibility(View.GONE);
                holder.tvTimeISendImage.setVisibility(View.GONE);
            } else if (!message.getHas_images().equals("false") && message.getSender_id().equals(myId)) {
                holder.civAvatarFriendChatting.setVisibility(View.GONE);
                holder.tvSmsOfFriend.setVisibility(View.GONE);
                holder.tvTimeFriendSendMessage.setVisibility(View.GONE);
                holder.ivFriendChattingSend.setVisibility(View.GONE);
                holder.tvTimeFriendSendImage.setVisibility(View.GONE);
                holder.tvSmsOfMe.setVisibility(View.GONE);
                holder.tvTimeISendMessage.setVisibility(View.GONE);
                holder.ivImageISend.setVisibility(View.VISIBLE);
                Picasso.get().load(message.getHas_images()).into(holder.ivImageISend);
                holder.tvTimeISendImage.setVisibility(View.VISIBLE);
                holder.tvTimeISendImage.setText(simpleDateFormat.format(message.getCreatedAt()));
                holder.ivImageISend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        zoom(message.getHas_images());
                    }
                });
            } else if (!message.getHas_images().equals("false") && !message.getSender_id().equals(myId)) {
                holder.civAvatarFriendChatting.setVisibility(View.VISIBLE);
                holder.civAvatarFriendChatting.setImageResource(R.drawable.default_avatar);
                holder.tvSmsOfFriend.setVisibility(View.GONE);
                holder.tvTimeFriendSendMessage.setVisibility(View.GONE);
                holder.ivFriendChattingSend.setVisibility(View.VISIBLE);
                Picasso.get().load(message.getHas_images()).into(holder.ivFriendChattingSend);
                holder.tvTimeFriendSendImage.setVisibility(View.VISIBLE);
                holder.tvTimeFriendSendImage.setText(simpleDateFormat.format(message.getCreatedAt()));
                holder.tvSmsOfMe.setVisibility(View.GONE);
                holder.tvTimeISendMessage.setVisibility(View.GONE);
                holder.ivImageISend.setVisibility(View.GONE);
                holder.tvTimeISendImage.setVisibility(View.GONE);
                holder.ivFriendChattingSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        zoom(message.getHas_images());
                    }
                });
            }
        }
    }

    private void zoom(String uri) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_photo_view, null);
        dialogBuilder.setView(dialogView);

        // Lấy reference đến ImageView trong dialogView
        SubsamplingScaleImageView photoView = dialogView.findViewById(R.id.photoView);
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                photoView.setImage(ImageSource.bitmap(bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        // Gán ảnh vào PhotoView
        Picasso.get().load(uri).into(target);

        // Tạo AlertDialog
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Xử lý sự kiện khi nhấp ra ngoài ảnh
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Xử lý khi AlertDialog được đóng
            }
        });

        // Hiển thị AlertDialog
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return listMessage.size();
    }

    public class ChattingViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civAvatarFriendChatting;
        TextView tvSmsOfFriend, tvTimeFriendSendMessage, tvTimeFriendSendImage, tvSmsOfMe, tvTimeISendMessage, tvTimeISendImage;
        ImageView ivFriendChattingSend, ivImageISend;
        public ChattingViewHolder(@NonNull View itemView) {
            super(itemView);
            civAvatarFriendChatting = itemView.findViewById(R.id.civAvatarFriendChatting);
            tvSmsOfFriend = itemView.findViewById(R.id.tvSmsOfFriend);
            tvTimeFriendSendMessage = itemView.findViewById(R.id.tvTimeFriendSendMessage);
            tvTimeFriendSendImage = itemView.findViewById(R.id.tvTimeFriendSendImage);
            tvSmsOfMe = itemView.findViewById(R.id.tvSmsOfMe);
            tvTimeISendMessage = itemView.findViewById(R.id.tvTimeISendMessage);
            tvTimeISendImage = itemView.findViewById(R.id.tvTimeISendImage);
            ivFriendChattingSend = itemView.findViewById(R.id.ivFriendChattingSend);
            ivImageISend = itemView.findViewById(R.id.ivImageISend);
        }
    }
}
