package com.example.chatappmongodb.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatappmongodb.Api.ApiManager;
import com.example.chatappmongodb.MainActivity;
import com.example.chatappmongodb.Models.ApiResponse;
import com.example.chatappmongodb.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends AppCompatActivity {
    private Toolbar toolbarMyProfileActivity;
    private ImageView ivBackground;
    private CircleImageView civAvatarProfile;
    private TextView tvMyStatus, tvMyUserName, tvMyUserNameSmall, tvMyEmail;
    private String myId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        setControl();
        setEvent();
    }

    private void setControl() {
        toolbarMyProfileActivity = findViewById(R.id.toolbarMyProfileActivity);
        ivBackground = findViewById(R.id.ivBackground);
        civAvatarProfile = findViewById(R.id.civAvatarProfile);
        tvMyStatus = findViewById(R.id.tvMyStatus);
        tvMyUserName = findViewById(R.id.tvMyUserName);
        tvMyUserNameSmall = findViewById(R.id.tvMyUserNameSmall);
        tvMyEmail = findViewById(R.id.tvMyEmail);
        myId = getIntent().getStringExtra("myId");
    }

    private void setEvent() {

        /* Xử lý logic cho Toolbar */
        actionToolbar();
        /* Tải thông tin mặc định lên giao diện */
        loadMyInformation();
    }

    private void actionToolbar() {
        setSupportActionBar(toolbarMyProfileActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Hồ sơ cá nhân");
        toolbarMyProfileActivity.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    private void loadMyInformation() {
        Call<ApiResponse> call = ApiManager.getInstance().getApiService().getProfileById(myId);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.getStatus().equals("fail")) {
                        Toast.makeText(MyProfileActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        String background_url = apiResponse.getData().getBg_image();
                        String profile_url = apiResponse.getData().getProfile();
                        String userName = apiResponse.getData().getName();
                        String userStatus = apiResponse.getData().getStatus();
                        String userEmail = apiResponse.getData().getEmail();

                        Picasso.get().load(background_url).placeholder(R.drawable.default_background).into(ivBackground);
                        Picasso.get().load(profile_url).into(civAvatarProfile);
                        tvMyUserName.setText(userName);
                        tvMyStatus.setText(userStatus);
                        tvMyUserNameSmall.setText(userName);
                        tvMyEmail.setText(userEmail);
                    }
                } else {
                    Toast.makeText(MyProfileActivity.this, "Lỗi khi tải dữ liệu, vui lòng kiểm tra sau", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(MyProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}