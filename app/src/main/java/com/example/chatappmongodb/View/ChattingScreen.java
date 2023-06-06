package com.example.chatappmongodb.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.ListenerService;
import com.example.chatappmongodb.Adapters.ChattingAdapter;
import com.example.chatappmongodb.Api.ApiManager;
import com.example.chatappmongodb.Login.SignInActivity;
import com.example.chatappmongodb.MainActivity;
import com.example.chatappmongodb.Models.ApiResponse;
import com.example.chatappmongodb.Models.ApiResponseDelete;
import com.example.chatappmongodb.Models.ApiResponseMessage;
import com.example.chatappmongodb.Models.Message;
import com.example.chatappmongodb.Models.User;
import com.example.chatappmongodb.R;
import com.example.chatappmongodb.RealPathUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChattingScreen extends AppCompatActivity {
    private ImageButton ibtnBack, ibtnSendImage, ibtnSendTextMessage, ibtnDeleteConversation, ibtnWidgets;
    private CircleImageView civAvatarFriend;
    private static final String TAG = "Send image";
    private TextView tvFriendName, tvFriendStatusActivity;
    private RecyclerView rvChatting;
    private EditText edtEnterMessage;
    private ChattingAdapter chattingAdapter;
    private ArrayList<Message> listMessage = new ArrayList<>();
    private String friendId, myId;
    private static final int MY_REQUEST_CODE = 10;
    private boolean isMediaManagerInitialized = false;
    private  Uri uri;
    private static MediaManager mediaManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_screen);
        setControl();
        setEvent();
    }

    private void setControl() {
        ibtnBack = findViewById(R.id.ibtnBack);
        ibtnSendImage = findViewById(R.id.ibtnSendImage);
        ibtnSendTextMessage = findViewById(R.id.ibtnSendTextMessage);
        ibtnDeleteConversation = findViewById(R.id.ibtnDeleteConversation);
        ibtnWidgets = findViewById(R.id.ibtnWidgets);
        civAvatarFriend = findViewById(R.id.civAvatarFriend);
        tvFriendName = findViewById(R.id.tvFriendName);
        tvFriendStatusActivity = findViewById(R.id.tvFriendStatusActivity);
        rvChatting = findViewById(R.id.rvChatting);
        edtEnterMessage = findViewById(R.id.edtEnterMessage);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ChattingScreen.this);
        rvChatting.setLayoutManager(layoutManager);

        friendId = getIntent().getStringExtra("friendId");
        myId = getIntent().getStringExtra("myId");
    }

    private void setEvent() {
        /* Config Cloudinary*/
//        MediaManager mediaManager = MediaManagerWrapper.getInstance(ChattingScreen.this).getMediaManager();
        /* Xử lý logic nút back */
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        /* Lấy thông tin của bạn bè nhắn tin */
        getFriendProfile(friendId);
        /* Lấy dữ liệu tin nhắn */
        getDataMessage(myId, friendId);
        /* Xử lý nút gửi tin nhắn */
        ibtnSendTextMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTextMessage();
            }
        });
        /* Xử lý nút gửi hình ảnh*/
        ibtnSendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        /* Xử lý widgets gửi hình ảnh*/
        ibtnWidgets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(ChattingScreen.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_widgets_chattingscreen, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.popupSendLocation:
                                Toast.makeText(ChattingScreen.this,"Chức năng gửi định vị" , Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.popupSendFile:
                                Toast.makeText(ChattingScreen.this, "Chức năng gửi đính kèm tệp", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.popupSendAudio:
                                Toast.makeText(ChattingScreen.this, "Chức năng gửi Audio", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.popupSendImageFromCamera:
                                Toast.makeText(ChattingScreen.this, "Chức năng gửi ảnh từ Camera", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.setGravity(Gravity.CENTER);
                popupMenu.setForceShowIcon(true);
                popupMenu.show();
            }
        });

        /* Xóa tin nhắn */
        ibtnDeleteConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogConfirmDelete(Gravity.CENTER);
            }
        });
    }


    private void configCloudinary() {
        Map<String, String> config = new HashMap();
        config.put("cloud_name", "dvcsktxvh");
        config.put("api_key", "717455286888243");
        config.put("api_secret", "kHcRvqkr0HqrUV2uBRkGICTndys");
        mediaManager.init(ChattingScreen.this,config);
    }


    private void getFriendProfile(String friendId) {
        Call<ApiResponse> call = ApiManager.getInstance().getApiService().getProfileById(friendId);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.getStatus().equals("fail")) {
                        Toast.makeText(ChattingScreen.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        tvFriendName.setText(apiResponse.getData().getName());
                        Picasso.get().load(apiResponse.getData().getProfile()).placeholder(R.drawable.default_avatar).into(civAvatarFriend);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(ChattingScreen.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getDataMessage(String myId, String friendId) {

        HashMap<String, String> map = new HashMap<>();
        map.put("receiver_id", friendId);
        map.put("sender_id", myId);
        Call<ApiResponseMessage> call = ApiManager.getInstance().getApiService().getMessage(map);
        call.enqueue(new Callback<ApiResponseMessage>() {
            @Override
            public void onResponse(Call<ApiResponseMessage> call, Response<ApiResponseMessage> response) {
                if (response.isSuccessful()) {
                    listMessage = response.body().getListMessage();
                    chattingAdapter = new ChattingAdapter(listMessage,ChattingScreen.this);
                    rvChatting.setAdapter(chattingAdapter);
                    chattingAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ChattingScreen.this, "Không có tin nhắn nào gần đây", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponseMessage> call, Throwable t) {
                Toast.makeText(ChattingScreen.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendTextMessage() {
        HashMap<String, String> map = new HashMap<>();
        map.put("message", edtEnterMessage.getText().toString().trim());
        map.put("sender_id", myId);
        map.put("receiver_id", friendId);
        Call<ApiResponseMessage> call = ApiManager.getInstance().getApiService().sendTextMessage(map);
        call.enqueue(new Callback<ApiResponseMessage>() {
            @Override
            public void onResponse(Call<ApiResponseMessage> call, Response<ApiResponseMessage> response) {
                if (response.isSuccessful()){
                    edtEnterMessage.setText(null);
                    getDataMessage(myId,friendId);
                } else {
                    Toast.makeText(ChattingScreen.this, "Có lỗi xáy ra", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponseMessage> call, Throwable t) {
                Toast.makeText(ChattingScreen.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        ImagePicker.Companion.with(ChattingScreen.this)
                .crop()                         //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .galleryOnly()
                .start(MY_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data.getData()!=null) {
                uri = data.getData();
                String strRealPath = RealPathUtil.getRealPath(this, uri);
                sendImageMessage(strRealPath);
            }
        }
    }

    private void sendImageMessage(String path) {
        /* Config Cloudinary*/
        configCloudinary();
        Map<String, String> params = new HashMap<String, String>();
        params.put("folder","images/imageMessage");
        MediaManager.get().upload(path).options(params).callback(new ListenerService() {
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
                HashMap<String, String> map = new HashMap<>();
                map.put("sender_id", myId);
                map.put("receiver_id", friendId);
                map.put("has_images", url);
                Call<ApiResponseMessage> call = ApiManager.getInstance().getApiService().sendImageMessage(map);
                call.enqueue(new Callback<ApiResponseMessage>() {
                    @Override
                    public void onResponse(Call<ApiResponseMessage> call, Response<ApiResponseMessage> response) {
                        if (response.isSuccessful()){
                            getDataMessage(myId,friendId);
                        } else {
                            Toast.makeText(ChattingScreen.this, "Có lỗi xáy ra: "+response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponseMessage> call, Throwable t) {
                        Toast.makeText(ChattingScreen.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                Log.e(TAG, "onError: "+ error.getDescription());
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                Log.e(TAG, "onError: "+ error.getDescription());
            }
        }).dispatch();
    }

    private void openDialogConfirmDelete(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_delete_conversation);
        Window window = (Window) dialog.getWindow();
        if (window == null) {
            return;
        } else {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            window.setAttributes(windowAttributes);

            if (Gravity.CENTER == gravity) {
                dialog.setCancelable(true);
            } else {
                dialog.setCancelable(false);
            }
            Button btnDelete = dialog.findViewById(R.id.btnDelete);
            Button btnCancelDelete = dialog.findViewById(R.id.btnCancelDelete);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Call<ApiResponseDelete> call = ApiManager.getInstance().getApiService().deleteMessages(myId, friendId);
                    call.enqueue(new Callback<ApiResponseDelete>() {
                        @Override
                        public void onResponse(Call<ApiResponseDelete> call, Response<ApiResponseDelete> response) {
                            if (response.isSuccessful()) {
                                ApiResponseDelete apiResponseDelete = response.body();
                                if (apiResponseDelete.getStatus().equals("success")) {
                                    Toast.makeText(ChattingScreen.this, apiResponseDelete.getMessage(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ChattingScreen.this,MainActivity.class);
                                    intent.putExtra("myId", myId);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(ChattingScreen.this, apiResponseDelete.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponseDelete> call, Throwable t) {
                            Toast.makeText(ChattingScreen.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    dialog.dismiss();
                }
            });
            btnCancelDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }
}