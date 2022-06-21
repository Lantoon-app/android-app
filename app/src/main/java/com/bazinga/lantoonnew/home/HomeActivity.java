package com.bazinga.lantoonnew.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bazinga.lantoonnew.R;
import com.bazinga.lantoonnew.SplashActivity;
import com.bazinga.lantoonnew.Tags;
import com.bazinga.lantoonnew.databinding.ActivityHomeBinding;
import com.bazinga.lantoonnew.login.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    public static SessionManager sessionManager;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private BottomNavigationView bottomNavView;
    ActivityHomeBinding binding;

    public static Toolbar toolbar;
    public static LinearLayoutCompat ll_toolbar;
    public static ImageView iv_back;
    public static TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        System.out.println("Lantoon cache " + getCacheDir().getPath() + Tags.FILE_DESTINATION_FOLDER);
        deleteDir(new File(getCacheDir().getPath() + File.separator + "1.zip"));
        deleteDir(new File(getCacheDir().getPath() + Tags.FILE_DESTINATION_FOLDER));
        sessionManager = new SessionManager(this);
        initToolbar();
        initNavigation();
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBack();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    private void onBack() {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_app_exit, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = false;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        TextView tvMessage = popupView.findViewById(R.id.tvMessage);
        Button btnYes = popupView.findViewById(R.id.btnYes);
        Button btnNo = popupView.findViewById(R.id.btnNo);
        ImageButton imgBtnClose = popupView.findViewById(R.id.imgBtnClose);

        tvMessage.setText(getString(R.string.ad_back_button_pressed_msg));

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                finishAffinity();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        imgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                popupWindow.dismiss();
                return true;
            }
        });
       /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.ad_home_button_pressed_msg))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("Alert");
        alert.show();*/
    }

    private void initToolbar() {

        toolbar = findViewById(R.id.toolbar);
        ll_toolbar = findViewById(R.id.ll_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);
        ll_toolbar.setVisibility(View.VISIBLE);
        ImageView iv_nav_menu_open = findViewById(R.id.iv_nav_menu_open);
        ImageView iv_nav_menu_profile_open = findViewById(R.id.iv_nav_menu_profile_open);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        iv_nav_menu_open.setOnClickListener(this::onClick);
        iv_nav_menu_profile_open.setOnClickListener(this::onClick);
    }

    private void initNavigation() {


        bottomNavView = findViewById(R.id.bottom_nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile, R.id.nav_change_password, R.id.nav_new_language,
                R.id.nav_payment, R.id.nav_share, R.id.nav_signout,
                R.id.bottom_lesson, R.id.bottom_target, R.id.bottom_leader, R.id.bottom_setting)
                .setDrawerLayout(binding.drawerLayout)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupWithNavController(binding.navViewEnd, navController);
        NavigationUI.setupWithNavController(bottomNavView, navController);
        binding.llSignout.setOnClickListener(this::onClick);
        binding.llProfileInformation.setOnClickListener(this::onClick);

       /* if (sessionManager.getRegistrationType() != 1)
            binding.navView.getMenu().findItem(R.id.nav_change_password).setVisible(false);
        binding.navView.getMenu().findItem(R.id.nav_change_password).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                bottomNavView.setItemBackground(null);
                binding.navView.setItemBackground(getDrawable(R.drawable.nav_drawer_menu_item_bg));
                return false;
            }
        });*/
        binding.llMyLanguage.setOnClickListener(this::onClick);
        binding.llReferenceLanguage.setOnClickListener(this::onClick);
       /* binding.navView.getMenu().findItem(R.id.nav_payment).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                bottomNavView.setItemBackground(null);
                binding.navView.setItemBackground(getDrawable(R.drawable.nav_drawer_menu_item_bg));
                return false;
            }
        });*/

        /*binding.navView.getMenu().findItem(R.id.nav_share).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                binding.navView.setItemBackground(null);
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Lantoon");
                    String shareMessage = "\nBuild language skills your own way - learn Spanish, French, Italian & more!\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                return false;
            }
        });*/
        bottomNavView.getMenu().findItem(R.id.bottom_lesson).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                binding.navView.setItemBackground(null);
                bottomNavView.setItemBackground(getDrawable(R.drawable.nav_bottom_bg_change_color));
                return false;
            }
        });
        bottomNavView.getMenu().findItem(R.id.bottom_target).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                binding.navView.setItemBackground(null);
                bottomNavView.setItemBackground(getDrawable(R.drawable.nav_bottom_bg_change_color));
                return false;
            }
        });
        bottomNavView.getMenu().findItem(R.id.bottom_leader).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                binding.navView.setItemBackground(null);
                bottomNavView.setItemBackground(getDrawable(R.drawable.nav_bottom_bg_change_color));
                return false;
            }
        });
        bottomNavView.getMenu().findItem(R.id.bottom_setting).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                binding.navView.setItemBackground(null);
                bottomNavView.setItemBackground(getDrawable(R.drawable.nav_bottom_bg_change_color));
                return false;
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("code result", String.valueOf(resultCode));
        Log.d("code req", String.valueOf(requestCode));
        if (requestCode == 2) {
            navController.navigate(R.id.bottom_lesson);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //  Log.d("code result" , String.valueOf(resultCode));
        Log.d("code req", String.valueOf(requestCode));
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        try {
            if (dir != null && dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    System.out.println("Lantoon cache path children " + children[i]);
                    if (!success) {
                        return false;
                    }
                }
                return dir.delete();
            } else if (dir != null && dir.isFile()) {
                System.out.println("Lantoon cache path " + dir.getPath());
                return dir.delete();
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_nav_menu_open:
                binding.drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.iv_nav_menu_profile_open:
                binding.drawerLayout.openDrawer(GravityCompat.END);
                break;
            case R.id.ll_signout:
                bottomNavView.setItemBackground(null);
                //Uncomment the below code to Set the message and title from the strings.xml file
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

                //Setting message manually and performing action on button click
                builder.setMessage(getString(R.string.ad_signout_msg))
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SessionManager sessionManager = new SessionManager(getApplicationContext());
                                sessionManager.logoutUser();
                                //This is for maintaining the behavior of the Navigation view
                                //NavigationUI.onNavDestinationSelected(item, navController);
                                //This is for closing the drawer after acting on it
                                binding.drawerLayout.closeDrawer(GravityCompat.END);
                                deleteDir(new File(getCacheDir().getPath() + File.separator + "1.zip"));
                                deleteDir(new File(getCacheDir().getPath() + Tags.FILE_DESTINATION_FOLDER));
                                Toast.makeText(getApplicationContext(), "Sign out Successfully",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                                binding.drawerLayout.closeDrawer(GravityCompat.START);

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Alert");
                alert.show();
                break;

            case R.id.ll_profile_information:
                binding.drawerLayout.closeDrawer(GravityCompat.END);
                navController.navigate(R.id.nav_profile);
                bottomNavView.setItemBackground(null);
                //binding.navView.setItemBackground(getDrawable(R.drawable.nav_drawer_menu_item_bg));
                break;
            case R.id.ll_my_language:
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                navController.navigate(R.id.nav_new_language);
                bottomNavView.setItemBackground(null);
                //binding.navView.setItemBackground(getDrawable(R.drawable.nav_drawer_menu_item_bg));
                break;
            case R.id.ll_reference_language:
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                navController.navigate(R.id.bottom_setting);
                bottomNavView.setItemBackground(null);
                //binding.navView.setItemBackground(getDrawable(R.drawable.nav_drawer_menu_item_bg));
                break;

        }
    }

}
