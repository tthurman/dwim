package org.marnanel.dwim.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.webkit.WebView;
import android.content.Intent;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.ListView;
import android.util.Log;

import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Scanner;
import java.io.InputStream;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import android.content.res.Configuration;

import org.marnanel.dwim.DwimApplication;
import org.marnanel.dwim.R;

public class MainActivity extends AppCompatActivity 
        implements NavigationView.OnNavigationItemSelectedListener {

        private static final String TAG = "MainActivity";

        private NavigationView mDrawer;
        private DrawerLayout mDrawerLayout;
        private ActionBarDrawerToggle mDrawerToggle;
        private Toolbar mToolbar;
        private int mSelectedId;

        @Override protected void onCreate(Bundle icicle) {

                super.onCreate(icicle);
                Log.d(TAG, "start creating.");

                setContentView(R.layout.activity_main);

                Log.d(TAG, "Get toolbar");
                mToolbar= (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(mToolbar);

                Log.d(TAG, "Get drawer");
                mDrawer = (NavigationView) findViewById(R.id.main_drawer);
                mDrawer.setNavigationItemSelectedListener(this);

                mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

                Log.d(TAG, "Create drawer toggle");
                mDrawerToggle = new ActionBarDrawerToggle(this,
                                mDrawerLayout,
                                mToolbar,
                                R.string.drawer_open,
                                R.string.drawer_close);

                Log.d(TAG, "Set drawer listener");
                mDrawerLayout.setDrawerListener(mDrawerToggle);
                mDrawerToggle.syncState();

                Log.d(TAG, "Created.");
        }

        private void itemSelection(int mSelectedId) {

                switch(mSelectedId) {

                        case R.id.navigation_item_1:
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                break;

                        case R.id.navigation_item_2:
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                break;

                        case R.id.navigation_sub_item_1:
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                break;

                        case R.id.navigation_sub_item_2:
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                break;

                }

        }

        @Override public void onConfigurationChanged(Configuration newConfig) {
                super.onConfigurationChanged(newConfig);
                mDrawerToggle.onConfigurationChanged(newConfig);
        }

        @Override public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mSelectedId = menuItem.getItemId();
                itemSelection(mSelectedId);
                return true;
        }

        @Override public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
                super.onSaveInstanceState(outState, outPersistentState);
                // save selected item so it will remain the same even after orientation change
                outState.putInt("SELECTED_ID",mSelectedId);
        }


}

