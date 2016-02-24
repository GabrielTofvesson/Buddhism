package com.gabriel.buddhism;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionItemTarget;
import com.github.amlcurran.showcaseview.targets.PointTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private int introCounter;
    private ShowcaseView overlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        introCounter=0;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(overlay==null) drawer.closeDrawer(GravityCompat.START);
                else overlay.hide();
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        new Thread(new Runnable() {@Override public void run() {
                try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
                new Handler(Looper.getMainLooper()).post(new Runnable() {@Override public void run() {intro();}});}}).start();
        findViewById(R.id.main).setVisibility(View.GONE);
    }
    public void intro(){
        if(introCounter==0){
            ++introCounter;
            new ShowcaseView.Builder(this)
                    .replaceEndButton((Button) getLayoutInflater().inflate(R.layout.got_it, (RelativeLayout) findViewById(R.id.main_parent), false))
                    .setContentTitle("Welcome")
                    .setContentText(getResources().getText(R.string.welcome1))
                    // .replaceEndButton((Button) getLayoutInflater().inflate(R.layout.bye_button, (RelativeLayout) findViewById(R.id.main_parent), false))
                    .setShowcaseEventListener(new OnShowcaseEventListener(){
                        @Override
                        public void onShowcaseViewHide(ShowcaseView showcaseView) {

                        }

                        @Override
                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                            new Thread(new Runnable() {@Override public void run() {
                                try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
                                new Handler(Looper.getMainLooper()).post(new Runnable() {@Override public void run() {intro();}
                                });
                            }}).start();
                        }

                        @Override
                        public void onShowcaseViewShow(ShowcaseView showcaseView) {

                        }

                        @Override
                        public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

                        }
                    })
                    .build();
        }else if(introCounter==1){
            ++introCounter;
            overlay = new ShowcaseView.Builder(this).replaceEndButton(R.layout.got_it)
                    .setTarget(new PointTarget(50, 60))
                    .replaceEndButton((Button) getLayoutInflater().inflate(R.layout.got_it, (RelativeLayout) findViewById(R.id.main_parent), false))
                    .setContentTitle("Instructions")
                    .setContentText(getResources().getText(R.string.welcome2) + "\n" + getResources().getText(R.string.signoff))
                    .setShowcaseEventListener(new OnShowcaseEventListener() {
                        @Override
                        public void onShowcaseViewHide(ShowcaseView showcaseView) {

                        }

                        @Override
                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            intro();
                                        }
                                    });
                                }
                            }).start();
                        }

                        @Override
                        public void onShowcaseViewShow(ShowcaseView showcaseView) {

                        }

                        @Override
                        public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

                        }
                    })
                    .build();
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}