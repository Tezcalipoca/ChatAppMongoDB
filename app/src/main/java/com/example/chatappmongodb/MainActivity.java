package com.example.chatappmongodb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatappmongodb.Api.ApiManager;
import com.example.chatappmongodb.Fragments.Fragment_Chat;
import com.example.chatappmongodb.Fragments.Fragment_Contact;
import com.example.chatappmongodb.Login.SignInActivity;
import com.example.chatappmongodb.Models.ApiResponse;
import com.example.chatappmongodb.View.MyProfileActivity;
import com.example.chatappmongodb.View.UpdateMyProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final int FRAGMENT_CHAT = 1;
    private static final int FRAGMENT_CONTACT = 2;
    private static final int FRAGMENT_CALL = 3;
    private static final int FRAGMENT_PROFILE = 4;
    public static final int MY_REQUEST_CODE = 10;
    private int currentFragment = FRAGMENT_CHAT;
    private BottomNavigationView bottomNavigation;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbarMain;
    public String myId;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        setEvent();

    }

    private void setControl() {

        toolbarMain = findViewById(R.id.toolbarMain);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        sharedPreferences = getSharedPreferences("myId", MODE_PRIVATE);
        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("myId", Context.MODE_PRIVATE);
        myId = getIntent().getStringExtra("myId");
    }

    private void setEvent() {

        /* Đặt Fragment mặc định là ChatFragment */
        replaceFragment(new Fragment_Chat());
        /* Đặt title cho toolbar */
        setTitleToolBar();
        /* Tải thông tin cho Header Navigation */
        loadHeaderInformation();
        /* Xử lý logic cho toolbar */
        setActionToolbar();
        /* Xử lý logic cho Bottom Navigation */
        setActionBottomNavigation();
        /* Xử lý logic cho Drawer*/
        setActionDrawer();
        /* Xử lý logic cho Navigation Drawer */
        setActionNavigationDrawer();
    }


    private void setActionToolbar() {
        setSupportActionBar(toolbarMain);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbarMain.setNavigationIcon(R.drawable.icon_menu_navigation);
    }

    private void loadHeaderInformation() {
        NavigationView navigationView = findViewById(R.id.navigationView);
        View headerNavigation = navigationView.getHeaderView(0);
        CircleImageView nav_header_userPhoto = headerNavigation.findViewById(R.id.nav_header_userPhoto);
        TextView nav_header_userName =  headerNavigation.findViewById(R.id.nav_header_userName);
        TextView nav_header_userEmail = headerNavigation.findViewById(R.id.nav_header_userEmail);
        ImageView nav_header_background = headerNavigation.findViewById(R.id.nav_header_background);
        Call<ApiResponse> call = ApiManager.getInstance().getApiService().getProfileById(myId);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.getStatus().equals("fail")) {
                        Toast.makeText(MainActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        nav_header_userName.setText(apiResponse.getData().getName());
                        nav_header_userEmail.setText(apiResponse.getData().getEmail());
                        Picasso.get().load(apiResponse.getData().getProfile()).placeholder(R.drawable.default_avatar).into(nav_header_userPhoto);
                        Picasso.get().load(apiResponse.getData().getBg_image()).placeholder(R.drawable.default_background).into(nav_header_background);
                    }
                } else {
                    Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        headerNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyProfileActivity.class);
                intent.putExtra("myId",myId);
                startActivity(intent);
            }
        });
    }

    private void setActionBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottomNavigationChat:
                        openChatFragment();
                        navigationView.setCheckedItem(R.id.navigationDrawerChat);
                        break;
                    case R.id.bottomNavigationContact:
                        openContactFragment();
                        navigationView.setCheckedItem(R.id.navigationDrawerContact);
                        break;
                    case R.id.bottomNavigationProfile:
                        PopupMenu popupMenu = new PopupMenu(MainActivity.this, bottomNavigation);
                        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_profile, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.popupProfile:
                                        Intent intent01 = new Intent(MainActivity.this, MyProfileActivity.class);
                                        intent01.putExtra("myId", myId);
                                        startActivity(intent01);
                                        break;
                                    case R.id.popupProfileUpdate:
                                        Intent intent02 = new Intent(MainActivity.this, UpdateMyProfileActivity.class);
                                        intent02.putExtra("myId", myId);
                                        startActivity(intent02);
                                        break;
                                    case R.id.popupProfileLogout:
                                        openDialogConfirmLogout(Gravity.CENTER);
                                        break;
                                }
                                return false;
                            }
                        });
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.setForceShowIcon(true);
                        popupMenu.show();
                        break;
                }
                setTitleToolBar();
                return true;
            }
        });
    }


    private void setActionDrawer() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void setActionNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.navigationDrawerChat);
        bottomNavigation.getMenu().findItem(R.id.bottomNavigationChat).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navigationDrawerChat) {
            openChatFragment();
            bottomNavigation.getMenu().findItem(R.id.bottomNavigationChat).setChecked(true);
        }  else if (id == R.id.navigationDrawerContact) {
            openContactFragment();
            bottomNavigation.getMenu().findItem(R.id.bottomNavigationContact).setChecked(true);
        }
        else if (id == R.id.navigationDrawerLogout) {
            openDialogConfirmLogout(Gravity.CENTER);
        }
        setTitleToolBar();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }
    private void openDialogConfirmLogout(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_logout);
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
            Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
            Button btnCancelConfirm = dialog.findViewById(R.id.btnCancelConfirm);

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(MainActivity.this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                    removeSharedPreferences();
                    dialog.dismiss();
                }
            });
            btnCancelConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Các hàm liên quan đến chuyển Fragment*/
    /* Thay thế fragment này bằng một fragment khác */
    private void replaceFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("myId", myId);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_view, fragment);
        transaction.commit();
    }
    /* Các hàm chuyển Fragment*/
    private void openChatFragment() {
        if (currentFragment != FRAGMENT_CHAT) {
            replaceFragment(new Fragment_Chat());
            currentFragment = FRAGMENT_CHAT;
        }
    }


    private void openContactFragment() {
        if (currentFragment != FRAGMENT_CONTACT) {
            replaceFragment(new Fragment_Contact());
            currentFragment = FRAGMENT_CONTACT;
        }
    }

    /* Xét lại title cho mỗi Fragment */
    private void setTitleToolBar() {
        String title = "";
        switch (currentFragment) {
            case FRAGMENT_CHAT:
                title = getString(R.string.chat);
                break;
            case FRAGMENT_CONTACT:
                title = getString(R.string.contact);
                break;
            case FRAGMENT_PROFILE:
                title = getString(R.string.profile);
                break;
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
    private void removeSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("myId");
        editor.commit();
    }

}