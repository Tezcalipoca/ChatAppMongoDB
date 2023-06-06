package com.example.chatappmongodb.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatappmongodb.Api.ApiManager;
import com.example.chatappmongodb.MainActivity;
import com.example.chatappmongodb.Models.ApiResponse;
import com.example.chatappmongodb.R;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    EditText edtSignInEmail, edtSignInPassword;
    ImageView ivPasswordVisibility, ivSignInGoogle, ivSignInFacebook;
    CheckBox cbRememberPassword;
    Button btnSignIn;
    TextView tvClickToSignUp;
    private boolean isEnabled = false;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setControl();
        setEvent();
    }

    private void setControl() {
        edtSignInEmail = findViewById(R.id.edtSignInEmail);
        edtSignInPassword = findViewById(R.id.edtSignInPassword);
        ivPasswordVisibility = findViewById(R.id.ivPasswordVisibility);
        ivSignInGoogle = findViewById(R.id.ivSignInGoogle);
        ivSignInFacebook = findViewById(R.id.ivSignInFacebook);
        cbRememberPassword = findViewById(R.id.cbRememberPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvClickToSignUp = findViewById(R.id.tvClickToSignUp);
        sharedPreferences = getSharedPreferences("Password", MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("myId", MODE_PRIVATE);
    }

    private void setEvent() {
        //Đọc mật khẩu được lưu trong sharedPreferences
        readPassword();

        /* Xử lý xem hoặc ẩn mật khẩu */
        ivPasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEnabled) {
                    ivPasswordVisibility.setImageResource(R.drawable.icon_eye);
                    edtSignInPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isEnabled = false;
                } else {
                    ivPasswordVisibility.setImageResource(R.drawable.icon_eye_off);
                    edtSignInPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isEnabled = true;
                }
            }
        });

        /* Xử lý nút đăng nhập */
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Hàm xử lý đăng nhập */
                login();
            }
        });

        /* Xử lý nút chuyển qua đăng ký*/
        tvClickToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void login() {
        ProgressDialog dialog = new ProgressDialog(SignInActivity.this);
        dialog.setTitle("Đăng nhập");
        dialog.setMessage("Đang đăng nhập, vui lòng đợi...");
        dialog.show();
        HashMap<String,String> map = new HashMap<>();
        map.put("email", edtSignInEmail.getText().toString());
        map.put("password", edtSignInPassword.getText().toString());
        Call<ApiResponse> call = ApiManager.getInstance().getApiService().loginApp(map);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    ApiResponse apiResponse = response.body();
                    String status = apiResponse.getStatus();
                    if (status.equals("fail")) {
                        Toast.makeText(SignInActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        if (cbRememberPassword.isChecked()) {
                            writePassword();
                        } else {
                            removeSharedPreferences();
                        }
                        String myId = apiResponse.getData().getUser().get_id();
                        Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        writeMyProfile(myId);
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.putExtra("myId", myId);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(SignInActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    private void writePassword() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Email",edtSignInEmail.getText().toString().trim());
        editor.putString("Password",edtSignInPassword.getText().toString().trim());
        editor.putBoolean("Checked", true);
        editor.commit();
    }

    private void readPassword() {
        edtSignInEmail.setText(sharedPreferences.getString("Email", ""));
        edtSignInPassword.setText(sharedPreferences.getString("Password",""));
        cbRememberPassword.setChecked(sharedPreferences.getBoolean("Checked", false));
    }

    private void removeSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("Email");
        editor.remove("Password");
        editor.remove("Checked");
        editor.commit();
    }

    private void writeMyProfile(String myId){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("myId",myId);
        editor.putString("isMediaManagerInitialized","false");
        editor.apply();
    }
}