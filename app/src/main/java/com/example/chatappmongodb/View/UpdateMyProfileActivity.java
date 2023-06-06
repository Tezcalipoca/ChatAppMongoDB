package com.example.chatappmongodb.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.ListenerService;
import com.example.chatappmongodb.Api.ApiManager;
import com.example.chatappmongodb.Models.ApiResponse;
import com.example.chatappmongodb.R;
import com.example.chatappmongodb.RealPathUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateMyProfileActivity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar toolbarUpdateProfileActivity;
    private ImageButton ibtnUpdateBackground, ibtnUpdateAvatar, ibtnUpdateName, ibtnUpdateStatus;
    private ImageView ivUpdateBackground;
    private CircleImageView civUpdateAvatarProfile;
    private TextView tvUserNameUpdate, tvStatusUpdate, tvUpdateEmail;
    private EditText edtUpdateName, edtUpdateMyStatus;
    private Button btnSaveName, btnSaveStatus;
    private LinearLayout linearLayoutUpdateMyName, linearLayoutUpdateMySatus;
    private OutputStream outputStream;
    private static final int MY_REQUEST_CODE = 10;
    private static final String TAG = "Upload image";
    private static final int REQUEST_UPDATE_BACKGROUND = 1;
    private static final int REQUEST_UPDATE_AVATAR = 2;
    private Uri imagePath;
    private String myId, myName, myEmail, myProfile, myBackground, myStatus;
    private ProgressDialog dialog;
    private SharedPreferences sharedPreferences;
//    private boolean isMediaManagerInitialized;
    private boolean isMediaManagerInitialized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_my_profile);
        setControl();
        setEvent();
    }

    private void setControl() {
        toolbarUpdateProfileActivity = findViewById(R.id.toolbarUpdateProfileActivity);
        ibtnUpdateBackground = findViewById(R.id.ibtnUpdateBackground);
        ibtnUpdateAvatar = findViewById(R.id.ibtnUpdateAvatar);
        ibtnUpdateAvatar = findViewById(R.id.ibtnUpdateAvatar);
        ibtnUpdateName = findViewById(R.id.ibtnUpdateName);
        ibtnUpdateStatus = findViewById(R.id.ibtnUpdateStatus);
        ivUpdateBackground = findViewById(R.id.ivUpdateBackground);
        civUpdateAvatarProfile = findViewById(R.id.civUpdateAvatarProfile);
        tvUserNameUpdate = findViewById(R.id.tvUserNameUpdate);
        tvStatusUpdate = findViewById(R.id.tvStatusUpdate);
        tvUpdateEmail = findViewById(R.id.tvUpdateEmail);
        edtUpdateName = findViewById(R.id.edtUpdateName);
        edtUpdateMyStatus = findViewById(R.id.edtUpdateMyStatus);
        btnSaveName = findViewById(R.id.btnSaveName);
        btnSaveStatus = findViewById(R.id.btnSaveStatus);
        linearLayoutUpdateMyName = findViewById(R.id.linearLayoutUpdateMyName);
        linearLayoutUpdateMySatus = findViewById(R.id.linearLayoutUpdateMySatus);
        sharedPreferences = UpdateMyProfileActivity.this.getSharedPreferences("isMediaManagerInitialized", Context.MODE_PRIVATE);
        myId = getIntent().getStringExtra("myId");
        dialog = new ProgressDialog(UpdateMyProfileActivity.this);
        dialog.setTitle("Cập nhật ảnh");
        dialog.setMessage("Đang cập nhật, vui lòng đợi...");
    }

    private void setEvent() {
        /* Xử lý logic cho Toolbar */
        setActionToolbar(toolbarUpdateProfileActivity);
        /* Update ảnh bìa */
        ibtnUpdateBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery(REQUEST_UPDATE_BACKGROUND);
            }
        });
        /* Update ảnh đại diện */
        ibtnUpdateAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery(REQUEST_UPDATE_AVATAR);
            }
        });
        /* Tải thông tin mặc định lên giao diện */
        loadDefaultInformation(myId, myName, myEmail, myProfile, myBackground, myStatus);
        /* Xử lý cho nút cập nhật Tên người dùng*/
        ibtnUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateName();
            }
        });
        /* Xử lý cho nút cập nhật Trạng thái*/
        ibtnUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatus();
            }
        });
    }

    private void setActionToolbar(Toolbar toolbarUpdateProfileActivity) {
        setSupportActionBar(toolbarUpdateProfileActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Cài đặt tài khoản");
        toolbarUpdateProfileActivity.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    private void openGallery(int requestUpdateAvatar) {
        ImagePicker.Companion.with(UpdateMyProfileActivity.this)
                .crop()                         //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start(requestUpdateAvatar);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            dialog.show();
            imagePath = data.getData();
            String strRealPath = RealPathUtil.getRealPath(this, imagePath);
            callApiUploadImage(strRealPath, requestCode );
        }
    }

    private void callApiUploadImage(String strRealPath, int requestCode) {
        Map<String, String> params = new HashMap<String, String>();
        if (requestCode == REQUEST_UPDATE_BACKGROUND) {
            params.put("folder","images/userBackground");
        } else if (requestCode == REQUEST_UPDATE_AVATAR)
            params.put("folder","images/userProfile");
        MediaManager.get().upload(strRealPath).options(params).callback(new ListenerService() {
            @Nullable
            @Override
            public IBinder onBind(Intent intent) {
                return null;
            }

            @Override
            public void onStart(String requestId) {
                Log.d(TAG, "onStart: "+ "Starting ...");
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                Log.d(TAG, "onStart: "+ "Uploading ...");
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                String url = Objects.requireNonNull(resultData.get("url")).toString();
                HashMap<String, String> params = new HashMap();
                if (requestCode == REQUEST_UPDATE_BACKGROUND) {
                    params.put("id",myId);
                    params.put("background_url",url);
                    Call<ApiResponse> call = ApiManager.getInstance().getApiService().updateBackgroundById(params);
                    call.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.isSuccessful()) {
                                ApiResponse apiResponse = response.body();
                                if (apiResponse.getStatus().equals("fail")) {
                                    Toast.makeText(UpdateMyProfileActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    dialog.dismiss();
                                    Picasso.get().load(url).placeholder(R.drawable.default_background).into(ivUpdateBackground);
                                    Toast.makeText(UpdateMyProfileActivity.this, "Cập nhật ảnh bìa thành công", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(UpdateMyProfileActivity.this, "Lỗi khi tải dữ liệu, vui lòng kiểm tra sau" + response.message(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            Toast.makeText(UpdateMyProfileActivity.this, "Cập nhật thất bại: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (requestCode == REQUEST_UPDATE_AVATAR) {
                    params.put("id",myId);
                    params.put("avatar_url",url);
                    Call<ApiResponse> call = ApiManager.getInstance().getApiService().updateAvatarById(params);
                    call.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.isSuccessful()) {
                                ApiResponse apiResponse = response.body();
                                if (apiResponse.getStatus().equals("fail")) {
                                    Toast.makeText(UpdateMyProfileActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    dialog.dismiss();
                                    Picasso.get().load(url).placeholder(R.drawable.default_avatar).into(civUpdateAvatarProfile);
                                    Toast.makeText(UpdateMyProfileActivity.this, "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(UpdateMyProfileActivity.this, "Lỗi khi tải dữ liệu, vui lòng kiểm tra sau", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            Toast.makeText(UpdateMyProfileActivity.this, "Cập nhật thất bại: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                Log.e(TAG, "onError: "+ error.getDescription());
                dialog.dismiss();
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                Log.e(TAG, "onError: "+ error.getDescription());
                dialog.dismiss();
            }
        }).dispatch();
    }

    private void loadDefaultInformation(String myId, String myName, String myEmail, String myProfile, String myBackground, String myStatus) {

        Call<ApiResponse> call = ApiManager.getInstance().getApiService().getProfileById(myId);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.getStatus().equals("fail")) {
                        Toast.makeText(UpdateMyProfileActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        String background_url = apiResponse.getData().getBg_image();
                        String profile_url = apiResponse.getData().getProfile();
                        String userName = apiResponse.getData().getName();
                        String userStatus = apiResponse.getData().getStatus();
                        String userEmail = apiResponse.getData().getEmail();

                        Picasso.get().load(background_url).placeholder(R.drawable.default_background).into(ivUpdateBackground);
                        Picasso.get().load(profile_url).placeholder(R.drawable.default_background).into(civUpdateAvatarProfile);
                        tvUserNameUpdate.setText(userName);
                        tvStatusUpdate.setText(userStatus);
                        tvUpdateEmail.setText(userEmail);
                        edtUpdateName.setText(userName);
                        edtUpdateMyStatus.setText(userStatus);
                    }
                } else {
                    Toast.makeText(UpdateMyProfileActivity.this, "Lỗi khi tải dữ liệu, vui lòng kiểm tra sau", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(UpdateMyProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateName() {
        tvUserNameUpdate.setVisibility(View.GONE);
        linearLayoutUpdateMyName.setVisibility(View.VISIBLE);
        btnSaveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvUserNameUpdate.setVisibility(View.VISIBLE);
                linearLayoutUpdateMyName.setVisibility(View.GONE);
                tvUserNameUpdate.setText(edtUpdateName.getText().toString().trim());
                saveName(myId);
            }
        });
    }

    private void saveName(String myId) {
        ProgressDialog dialog = new ProgressDialog(UpdateMyProfileActivity.this);
        dialog.setTitle("Lưu Tên người dùng");
        dialog.setMessage("Đang lưu, vui lòng đợi...");
        dialog.show();
        HashMap<String,String> map = new HashMap<>();
        map.put("id", myId);
        map.put("name", edtUpdateName.getText().toString());
        Call<ApiResponse> call = ApiManager.getInstance().getApiService().updateNameById(map);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.getStatus().equals("fail")) {
                        Toast.makeText(UpdateMyProfileActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UpdateMyProfileActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(UpdateMyProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void updateStatus() {
        tvStatusUpdate.setVisibility(View.GONE);
        linearLayoutUpdateMySatus.setVisibility(View.VISIBLE);

        btnSaveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvStatusUpdate.setVisibility(View.VISIBLE);
                linearLayoutUpdateMySatus.setVisibility(View.GONE);
                tvStatusUpdate.setText(edtUpdateMyStatus.getText().toString().trim());
                saveStatus(myId);
            }
        });
    }

    private void saveStatus(String myId) {
        ProgressDialog dialog = new ProgressDialog(UpdateMyProfileActivity.this);
        dialog.setTitle("Lưu Trạng thái");
        dialog.setMessage("Đang lưu, vui lòng đợi...");
        dialog.show();
        HashMap<String,String> map = new HashMap<>();
        map.put("id", myId);
        map.put("status", edtUpdateMyStatus.getText().toString());
        Call<ApiResponse> call = ApiManager.getInstance().getApiService().updateStatusById(map);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.getStatus().equals("fail")) {
                        Toast.makeText(UpdateMyProfileActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UpdateMyProfileActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(UpdateMyProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}