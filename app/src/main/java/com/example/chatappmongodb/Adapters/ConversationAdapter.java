package com.example.chatappmongodb.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappmongodb.Api.ApiManager;
import com.example.chatappmongodb.Models.ApiResponse;
import com.example.chatappmongodb.Models.Message;
import com.example.chatappmongodb.R;
import com.example.chatappmongodb.View.ChattingScreen;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {
    ArrayList<Message> listConversations;
    Context context;

    public ConversationAdapter(ArrayList<Message> listConversations, Context context) {
        this.listConversations = listConversations;
        this.context = context;
    }

    @NonNull
    @Override
    public ConversationAdapter.ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_conversation, parent, false);
        return new ConversationViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationAdapter.ConversationViewHolder holder, int position) {
        Message message = listConversations.get(position);
        SharedPreferences sharedPreferences = context.getSharedPreferences("myId", Context.MODE_PRIVATE);
        String myId = sharedPreferences.getString("myId", "");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm ");

        if (message != null) {
            if (message.getHas_images().equals("false") && (message.getSender_id().equals(myId))) {
                message.setContacts_id(message.getReceiver_id());
            } else if (message.getHas_images().equals("false") && message.getReceiver_id().equals(myId)) {
                message.setContacts_id(message.getSender_id());
            } else if (!message.getHas_images().equals("false") && message.getSender_id().equals(myId)) {
                message.setContacts_id(message.getReceiver_id());
            } else if (!message.getHas_images().equals("false") && message.getReceiver_id().equals(myId)) {
                message.setContacts_id(message.getSender_id());
            }
        }

        if (message != null) {
            if (message.getHas_images().equals("false") && (message.getSender_id().equals(myId))) {
                message.setContacts_id(message.getReceiver_id());
                holder.tvLastMessageItemListConversation.setText(message.getMessage());
                getNameAndProfileContact(message.getReceiver_id(), new OnNameLoadedListener() {
                    @Override
                    public void onNameLoaded(String name, String profile) {
                        holder.tvUserNameItemListConversation.setText(name);
                        Picasso.get().load(profile).into(holder.civAvatarItemListConversation);
                    }
                });
            } else if (message.getHas_images().equals("false") && message.getReceiver_id().equals(myId)) {
                message.setContacts_id(message.getSender_id());
                holder.tvLastMessageItemListConversation.setText(message.getMessage());
                getNameAndProfileContact(message.getSender_id(), new OnNameLoadedListener() {
                    @Override
                    public void onNameLoaded(String name, String profile) {
                        holder.tvUserNameItemListConversation.setText(name);
                        Picasso.get().load(profile).into(holder.civAvatarItemListConversation);
                    }
                });
            } else if (!message.getHas_images().equals("false") && message.getSender_id().equals(myId)) {
                message.setContacts_id(message.getReceiver_id());
                holder.tvLastMessageItemListConversation.setText("Bạn đã gửi một ảnh");
                getNameAndProfileContact(message.getReceiver_id(), new OnNameLoadedListener() {
                    @Override
                    public void onNameLoaded(String name, String profile) {
                        holder.tvUserNameItemListConversation.setText(name);
                        Picasso.get().load(profile).into(holder.civAvatarItemListConversation);
                    }
                });
            } else if (!message.getHas_images().equals("false") && message.getReceiver_id().equals(myId)) {
                message.setContacts_id(message.getSender_id());
                holder.tvLastMessageItemListConversation.setText("Đã gửi một ảnh");
                getNameAndProfileContact(message.getSender_id(), new OnNameLoadedListener() {
                    @Override
                    public void onNameLoaded(String name, String profile) {
                        holder.tvUserNameItemListConversation.setText(name);
                        Picasso.get().load(profile).into(holder.civAvatarItemListConversation);
                    }
                });
            }
        }
//        holder.civAvatarItemListConversation.setImageResource(R.drawable.default_avatar);
        holder.tvTimeLastMessage.setText(simpleDateFormat.format(message.getCreatedAt()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("myId", Context.MODE_PRIVATE);
                String myId = sharedPreferences.getString("myId", "");
                Intent intent = new Intent(holder.itemView.getContext(), ChattingScreen.class);
                intent.putExtra("friendId", message.getContacts_id());
                intent.putExtra("myId", myId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listConversations.size();
    }

    public class ConversationViewHolder extends RecyclerView.ViewHolder {

        CircleImageView civAvatarItemListConversation;
        TextView tvUserNameItemListConversation, tvLastMessageItemListConversation, tvTimeLastMessage;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            civAvatarItemListConversation = itemView.findViewById(R.id.civAvatarItemListConversation);
            tvUserNameItemListConversation = itemView.findViewById(R.id.tvUserNameItemListConversation);
            tvLastMessageItemListConversation = itemView.findViewById(R.id.tvLastMessageItemListConversation);
            tvTimeLastMessage = itemView.findViewById(R.id.tvTimeLastMessage);
        }
    }

    private void getNameAndProfileContact(String id, OnNameLoadedListener listener) {
        Call<ApiResponse> call = ApiManager.getInstance().getApiService().getProfileById(id);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.getStatus().equals("fail")) {
                        Toast.makeText(ConversationAdapter.this.context, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        String name = apiResponse.getData().getName();
                        String profile = apiResponse.getData().getProfile();
                        listener.onNameLoaded(name, profile);
                    }
                } else {
                    Toast.makeText(ConversationAdapter.this.context, "Lỗi khi tải dữ liệu, vui lòng kiểm tra sau", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(ConversationAdapter.this.context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public interface OnNameLoadedListener {
        void onNameLoaded(String name, String profile);
    }
}
