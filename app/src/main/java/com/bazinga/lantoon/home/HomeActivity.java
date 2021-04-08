package com.bazinga.lantoon.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.MainActivity;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.login.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class HomeActivity extends AppCompatActivity {


    public static SessionManager sessionManager;
    private static final float END_SCALE = 0.85f;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavView;
    private CoordinatorLayout contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        /*Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));*/
        sessionManager = new SessionManager(this);

        initToolbar();
        initNavigation();
        //showBottomNavigation(false);
    }

    private void initToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    private void initNavigation() {

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavView = findViewById(R.id.bottom_nav_view);
        contentView = findViewById(R.id.content_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile, R.id.nav_change_password, R.id.nav_new_language,
                R.id.nav_payment, R.id.nav_share, R.id.nav_signout,
                R.id.bottom_lesson, R.id.bottom_target, R.id.bottom_leader, R.id.bottom_setting)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavView, navController);
        navigationView.getMenu().findItem(R.id.nav_signout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
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
                                NavigationUI.onNavDestinationSelected(item, navController);
                                //This is for closing the drawer after acting on it
                                drawer.closeDrawer(GravityCompat.START);
                                Toast.makeText(getApplicationContext(), "Sign out Successfully",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                                //This is for maintaining the behavior of the Navigation view
                                NavigationUI.onNavDestinationSelected(item, navController);
                                //This is for closing the drawer after acting on it
                                drawer.closeDrawer(GravityCompat.START);

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Alert");
                alert.show();


                return true;
            }
        });
        //animateNavigationDrawer();
        setHeader();
    }

    private void setHeader() {
        View view = navigationView.getHeaderView(0);
        ImageView ivNavHeaderUserImage = view.findViewById(R.id.ivNavHeaderUserImage);
        TextView tvNavHeaderUsername = view.findViewById(R.id.tvNavHeaderUsername);
        TextView tvNavHeaderUserId = view.findViewById(R.id.tvNavHeaderUserId);
        tvNavHeaderUsername.setText(sessionManager.getUserDetails().getUname());
        tvNavHeaderUserId.setText(sessionManager.getUserDetails().getUid());
        if (!sessionManager.getUserDetails().getPhoto().equals("")) {
            byte[] decodedString = Base64.decode(sessionManager.getUserDetails().getPhoto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            RoundedBitmapDrawable dr =
                    RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), decodedByte);
            dr.setGravity(Gravity.CENTER);
            dr.setCircular(true);
            ivNavHeaderUserImage.setBackground(null);
            ivNavHeaderUserImage.setImageDrawable(dr);

        }
    }


    private void animateNavigationDrawer() {
//        drawerLayout.setScrimColor(getResources().getColor(R.color.text_brown));
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
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

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


}
