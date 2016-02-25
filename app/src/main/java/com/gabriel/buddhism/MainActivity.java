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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.PointTarget;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private int introCounter;
    private ShowcaseView overlay;
    private static final String TAG_INFO_STAGE="info_stage";
    private static final String TAG_NAVIGATION_STAGE="nav_stage";
    private int viewStage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewStage=(savedInstanceState!=null) ? savedInstanceState.getInt(TAG_NAVIGATION_STAGE) : 0;
        System.out.println("Got stage with value: "+viewStage);
        introCounter=(savedInstanceState!=null) ? savedInstanceState.getInt(TAG_INFO_STAGE) : 0;
        System.out.println("Got counter with value: "+introCounter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (introCounter<1) drawer.closeDrawer(GravityCompat.START);
                else if(overlay!=null) overlay.hide();
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(introCounter<2)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {if(introCounter==0) Thread.sleep(1000);
                    } catch (InterruptedException e) {e.printStackTrace();}
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {intro();
                        }
                    });}}).start();
        else {
            if(viewStage<2) {
                LayoutInflater l = getLayoutInflater();
                View v = l.inflate(R.layout.dynamic_welcome, (RelativeLayout) findViewById(R.id.main_parent));
                v.findViewById(R.id.arrow).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.arrow_anim));
                viewStage = 1;
            }else if (viewStage == 2) loadCorrespondingView(R.id.nav_question1);
            else if (viewStage == 3) loadCorrespondingView(R.id.nav_question2);
            else loadCorrespondingView(R.id.nav_question3);
        }
    }
    public void intro(){
        if(introCounter==0){
            new ShowcaseView.Builder(this)
                    .replaceEndButton((Button) getLayoutInflater().inflate(R.layout.got_it, (RelativeLayout) findViewById(R.id.main_parent), false))
                    .setContentTitle(getResources().getText(R.string.welcome))
                    .setContentText(getResources().getText(R.string.welcome1))
                    // .replaceEndButton((Button) getLayoutInflater().inflate(R.layout.bye_button, (RelativeLayout) findViewById(R.id.main_parent), false))
                    .setShowcaseEventListener(new OnShowcaseEventListener(){
                        @Override
                        public void onShowcaseViewHide(ShowcaseView showcaseView) {}
                        @Override
                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                            ++introCounter;
                            new Thread(new Runnable() {@Override public void run() {
                                try {Thread.sleep(750);} catch (InterruptedException e) {e.printStackTrace();}
                                new Handler(Looper.getMainLooper()).post(new Runnable() {@Override public void run() {intro();}
                                });
                            }}).start();
                        }
                        @Override
                        public void onShowcaseViewShow(ShowcaseView showcaseView) {}
                        @Override
                        public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {}
                    })
                    .build();
        }else if(introCounter==1){
            overlay = new ShowcaseView.Builder(this).replaceEndButton(R.layout.got_it)
                    .setTarget(new PointTarget(50, 60))
                    .replaceEndButton((Button) getLayoutInflater().inflate(R.layout.got_it, (RelativeLayout) findViewById(R.id.main_parent), false))
                    .setContentTitle(getResources().getText(R.string.introduction))
                    .setContentText(getResources().getText(R.string.welcome2) + "\n" + getResources().getText(R.string.signoff))
                    .setShowcaseEventListener(new OnShowcaseEventListener() {
                        @Override
                        public void onShowcaseViewHide(ShowcaseView showcaseView) {
                        }

                        @Override
                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                            ++introCounter;
                            toolbar.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    intro();
                                }
                            }, 250);
                        }

                        @Override
                        public void onShowcaseViewShow(ShowcaseView showcaseView) {
                        }

                        @Override
                        public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {
                        }
                    })
                    .build();
        }else if(introCounter==2){
            LayoutInflater l = getLayoutInflater();
            View v = l.inflate(R.layout.dynamic_welcome, (RelativeLayout) findViewById(R.id.main_parent));
            v.findViewById(R.id.arrow).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.arrow_anim));
            viewStage = 1;
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

    @Override
    public void onSaveInstanceState(Bundle b){
        b.putInt(TAG_INFO_STAGE, introCounter);
        System.out.println("Stored counter: " + introCounter);
        b.putInt(TAG_NAVIGATION_STAGE, viewStage);
        System.out.println("Stored stage: "+viewStage);
        super.onSaveInstanceState(b);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if(viewStage==0) return false;

        // Handle navigation view item clicks here.
        loadCorrespondingView(item.getItemId());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void loadCorrespondingView(int id){
        ((RelativeLayout) findViewById(R.id.main_parent)).removeAllViewsInLayout();
        RelativeLayout v = (RelativeLayout) getLayoutInflater().inflate(R.layout.dynamic_answer, ((RelativeLayout) findViewById(R.id.main_parent)));
        if (id == R.id.nav_question1) {
            viewStage=2;
            ((TextView) v.findViewById(R.id.question)).setText(getResources().getText(R.string.question1));
            ((TextView) v.findViewById(R.id.answer)).setText(getResources().getText(R.string.answer1));
        } else if (id == R.id.nav_question2) {
            viewStage=3;
            ((TextView) v.findViewById(R.id.question)).setText(getResources().getText(R.string.question2));
            ((TextView) v.findViewById(R.id.answer)).setText(getResources().getText(R.string.answer2));
        } else if (id == R.id.nav_question3) {
            viewStage=4;
            ((TextView) v.findViewById(R.id.question)).setText(getResources().getText(R.string.question3));
            ((TextView) v.findViewById(R.id.answer)).setText(getResources().getText(R.string.answer3));
        }
    }
}