package com.example.airquality;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.airquality.API.APIInterface;
import com.google.android.material.navigation.NavigationView;

public class DashBoardActivity_custom extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_CHART = 1;
    private static final int FRAGMENT_MYPROFILE = 2;
    private static final int FRAGMENT_RESETpassword = 3;
    private static final int FRAGMENT_LOGOUT = 4;

    private int mCurrentFragment = FRAGMENT_HOME;
    NavigationView navigationView;

    String token;
    String User;
    String assetid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_custom);

        ////////////////////////////////////////////////////////////
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("mypackage");
        User = bundle.getString("user");
        assetid = bundle.getString("assetid");
        token = bundle.getString("token");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        HomeFragment homeFragment = new HomeFragment();

        replaceFragment(homeFragment);
        getSupportActionBar().setTitle("Home");

        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id =  item.getItemId();
        if (id == R.id.nav_home)
        {
            if (mCurrentFragment != FRAGMENT_HOME) {

                replaceFragment(new HomeFragment());
                mCurrentFragment = FRAGMENT_HOME;
                getSupportActionBar().setTitle("Home");
            }
        }
        else if (id == R.id.nav_char)
        {
            if (mCurrentFragment != FRAGMENT_CHART)
            {
                replaceFragment(new ChartFragment());
                mCurrentFragment = FRAGMENT_CHART;
                getSupportActionBar().setTitle("Chart");
            }
        }
        else if (id == R.id.nav_reset_password)
        {
            if (mCurrentFragment != FRAGMENT_RESETpassword)
            {
                replaceFragment(new ResetPasswordFragment());
                mCurrentFragment = FRAGMENT_RESETpassword;
                getSupportActionBar().setTitle("Reset password");
            }
        }
        else if (id == R.id.nav_my_profile)
        {
            if (mCurrentFragment != FRAGMENT_MYPROFILE)
            {
                replaceFragment(new ProfileFragment());
                mCurrentFragment = FRAGMENT_MYPROFILE;
                getSupportActionBar().setTitle("Account");
            }
        }

        else if (id == R.id.nav_logout)
        {
            if (mCurrentFragment != FRAGMENT_LOGOUT)
            {
                replaceFragment(new LogOutFragment());
                mCurrentFragment = FRAGMENT_LOGOUT;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @NonNull
    @Override
    public OnBackInvokedDispatcher getOnBackInvokedDispatcher() {
        return super.getOnBackInvokedDispatcher();
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
    private void replaceFragment(Fragment fragment)
    {
        Bundle bundleHome = new Bundle();
        bundleHome.putString("user",User);
        bundleHome.putString("assetid", assetid);
        bundleHome.putString("token",token);
        fragment.setArguments(bundleHome);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame,fragment);
        transaction.commit();
    }
}