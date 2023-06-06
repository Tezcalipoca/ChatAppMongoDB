package com.example.chatappmongodb.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatappmongodb.Api.ApiManager;
import com.example.chatappmongodb.Api.ApiService;
import com.example.chatappmongodb.MainActivity;
import com.example.chatappmongodb.R;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtSignUpEmail, edtSignUpUserName, edtSignUpPassword, edtSignUpConfirmPassword;
    private ImageView ivSignUpPasswordVisible, ivSignUpConfirmPasswordVisible;
    private Spinner spnSignUpCountry;
    private CheckBox cbAgreeWithTermsOfUse;
    private Button btnSignUp;
    private TextView tvClickToSignIn;
    private boolean isEnabled = false;
    private ApiService apiService;
    private String location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setControls();
        setEvents();
    }

    private void setControls() {
        edtSignUpEmail = findViewById(R.id.edtSignUpEmail);
        edtSignUpUserName = findViewById(R.id.edtSignUpUserName);
        edtSignUpPassword = findViewById(R.id.edtSignUpPassword);
        edtSignUpConfirmPassword = findViewById(R.id.edtSignUpConfirmPassword);
        ivSignUpPasswordVisible = findViewById(R.id.ivSignUpPasswordVisible);
        ivSignUpConfirmPasswordVisible = findViewById(R.id.ivSignUpConfirmPasswordVisible);
        edtSignUpConfirmPassword = findViewById(R.id.edtSignUpConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvClickToSignIn = findViewById(R.id.tvClickToSignIn);
    }

    private void setEvents() {

        ivSignUpPasswordVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEnabled) {
                    ivSignUpPasswordVisible.setImageResource(R.drawable.icon_eye);
                    edtSignUpPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isEnabled = false;
                } else {
                    ivSignUpPasswordVisible.setImageResource(R.drawable.icon_eye_off);
                    edtSignUpPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isEnabled = true;
                }
            }
        });

        ivSignUpConfirmPasswordVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEnabled) {
                    ivSignUpConfirmPasswordVisible.setImageResource(R.drawable.icon_eye);
                    edtSignUpConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isEnabled = false;
                } else {
                    ivSignUpConfirmPasswordVisible.setImageResource(R.drawable.icon_eye_off);
                    edtSignUpConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isEnabled = true;
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtSignUpEmail.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng điền Email", Toast.LENGTH_SHORT).show();
                } else if (edtSignUpUserName.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng điền Tên!", Toast.LENGTH_SHORT).show();
                } else if (edtSignUpPassword.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng nhập Mật khẩu", Toast.LENGTH_SHORT).show();
                } else if (edtSignUpConfirmPassword.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng Xác nhận lại mật khẩu", Toast.LENGTH_SHORT).show();
                } else if (!edtSignUpConfirmPassword.getText().toString().equals(edtSignUpPassword.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "Mật khẩu và xác thực không giống nhau, vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                } else {
                    register();
                }
            }
        });

        tvClickToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void register() {
        ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setTitle("Đang tạo tài khoản");
        dialog.setMessage("Chúng tôi đang tạo tài khoản cho bạn");
        dialog.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("email", edtSignUpEmail.getText().toString());
        map.put("name", edtSignUpUserName.getText().toString());
        map.put("password", edtSignUpPassword.getText().toString());
        map.put("passwordConfirm", edtSignUpConfirmPassword.getText().toString());
        Call<Void> call = ApiManager.getInstance().getApiService().createNewUser(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dialog.dismiss();
                if (response.code() == 200) {
                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

}