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

import com.example.chatappmongodb.Adapters.UserAdapter;
import com.example.chatappmongodb.Api.ApiManager;
import com.example.chatappmongodb.Models.ApiResponse;
import com.example.chatappmongodb.Models.User;
import com.example.chatappmongodb.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Contact extends Fragment {
    private SearchView searchViewContact;
    private RecyclerView rvListUser;
    private UserAdapter userAdapter;
    private ArrayList<User> listUsers = new ArrayList<>();
    private ArrayList<User> listUserFilter = new ArrayList<>();
    private String myId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment__contact, container, false);
        setControl(mView);
        setEvent();
        return mView;
    }

    private void setControl(View mView) {
        searchViewContact = mView.findViewById(R.id.searchViewContact);
        rvListUser = mView.findViewById(R.id.rvListUser);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvListUser.setLayoutManager(layoutManager);
        myId = getArguments().getString("myId");
    }

    private void setEvent() {
        /* Xử lý Logic cho Search View */
        searchViewContact.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                userAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userAdapter.getFilter().filter(newText);
                return false;
            }
        });
        loadAllUser();
    }

    private void loadAllUser() {
        Call<ApiResponse> call = ApiManager.getInstance().getApiService().getlistUser();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    listUsers = response.body().getListUser();
                    listUserFilter = filterUser(listUsers, myId);
                    userAdapter = new UserAdapter(listUserFilter, getContext());
                    rvListUser.setAdapter(userAdapter);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<User> filterUser(ArrayList<User> listUsers, String myId) {
        ArrayList<User> filteredUser = new ArrayList<>();
        for (User user : listUsers) {
            if (!user.get_id().equals(myId)) {
                filteredUser.add(user);
            }

        }
        return filteredUser;
    }
}