package com.example.chatappmongodb.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappmongodb.Adapters.ConversationAdapter;
import com.example.chatappmongodb.Adapters.UserAdapter;
import com.example.chatappmongodb.Api.ApiManager;
import com.example.chatappmongodb.Models.ApiResponseMessage;
import com.example.chatappmongodb.Models.Message;
import com.example.chatappmongodb.R;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Chat extends Fragment {
    SearchView searchViewConversation;
    RecyclerView rvListConversation;
    ConversationAdapter conversationAdapter;
    ArrayList<Message> listConversation;
    String myId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__chat, container, false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setControl(View view) {
        searchViewConversation = view.findViewById(R.id.searchViewConversation);
        rvListConversation = view.findViewById(R.id.rvListConversation);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvListConversation.setLayoutManager(layoutManager);
        myId = getArguments().getString("myId");
    }
    private void setEvent(){
        loadListConversation();
    }
    private void updateListConversation(ArrayList<Message> updatedList) {
        listConversation = updatedList;
        conversationAdapter = new ConversationAdapter(listConversation, getContext());
        rvListConversation.setAdapter(conversationAdapter);
        conversationAdapter.notifyDataSetChanged();
    }

    private void loadListConversation() {
        HashMap<String, String> map = new HashMap<>();
        map.put("myId", myId);
        Call<ApiResponseMessage> call = ApiManager.getInstance().getApiService().getAllConversations(map);
        call.enqueue(new Callback<ApiResponseMessage>() {
            @Override
            public void onResponse(Call<ApiResponseMessage> call, Response<ApiResponseMessage> response) {
                if (response.isSuccessful()) {
                    ArrayList<Message> updatedList = response.body().getListMessage();
                    updateListConversation(updatedList);
                }
            }

            @Override
            public void onFailure(Call<ApiResponseMessage> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}