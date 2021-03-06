package com.project.pp.parentparadise;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.project.pp.parentparadise.amber.FavoriteFragment;
import com.project.pp.parentparadise.amber.ShareFragment;
import com.project.pp.parentparadise.freya.CommunityFragment;
import com.project.pp.parentparadise.lin.ActFragment;
import com.project.pp.parentparadise.lin.HomeFragment;
import com.project.pp.parentparadise.utl.SessionManager;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private static int itemSelected;
    private static String appTitle;
    private static SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpActionBar();
        initDrawer();
        initContent();

    }

    //幾乎不會用這個生命週期方法，因為同步化規定要寫在這邊
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // home icon will keep still without calling syncState()
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int id = getIntent().getIntExtra("id", 0);
        Fragment fragment;
        switch (id){
            case 1:
                fragment = new ShareFragment();
                switchFragment(fragment);;
                appTitle = "分享";
                setTitle(appTitle);
                break;

            case 2:
                fragment = new FavoriteFragment();
                switchFragment(fragment);
                appTitle = "我的收藏";
                setTitle(appTitle);
                break;
        }
        if (id == 2) {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        itemSelected = 0;
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //可以放按鈕(<-)
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //ActionBarDrawerToggle監聽器與同步化按鈕(其實也可不加監聽器，為了同步化按鈕才加的)
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.text_Open, R.string.text_Close);

        //檢查手機有沒有新功能setDrawerListener屬於舊寫法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
        } else {
            drawerLayout.setDrawerListener(actionBarDrawerToggle);
        }

        NavigationView view_start = (NavigationView) findViewById(R.id.navigation_start);
        view_start.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                Fragment fragment;
                itemSelected = menuItem.getItemId();
                switch (itemSelected) {
                    case R.id.item_Home:
                        initContent();
                        fragment = new HomeFragment();
                        switchFragment(fragment);
                        appTitle = "首頁";
                        setTitle(appTitle);
                        break;
                    case R.id.item_activities:
                        fragment = new ActFragment();
                        switchFragment(fragment);
                        appTitle = "親子活動";
                        setTitle(appTitle);
                        break;
                    case R.id.item_Share:
                        fragment = new ShareFragment();
                        switchFragment(fragment);
                        appTitle = "分享";
                        setTitle(appTitle);
                        break;
                    case R.id.item_Community:
                        fragment = new CommunityFragment();
                        switchFragment(fragment);
                        appTitle = "社群";
                        setTitle(appTitle);
                        break;
                    case R.id.item_Favorite:
                        fragment = new FavoriteFragment();
                        switchFragment(fragment);
                        appTitle = "我的收藏";
                        setTitle(appTitle);
                        break;
		            case R.id.item_Logout:
                        session = null;
                        session = new SessionManager(getApplicationContext());
                        session.setLogin(false);
                        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        initContent();
                        break;
                }
                return true;
            }
        });
    }

    private void initContent() {
        if(itemSelected != 0){
            NavigationView view_start = (NavigationView) findViewById(R.id.navigation_start);
            view_start.setCheckedItem(itemSelected);
            setTitle(appTitle);
        }else {
            Fragment fragment = new HomeFragment();
            switchFragment(fragment);
            appTitle = "首頁";
            setTitle(appTitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchFragment(Fragment fragment) {
        //想要異動fragment就需要FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //FragmentTransaction異動的method
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        //commit切換，一定要做
        fragmentTransaction.commit();
    }
}

