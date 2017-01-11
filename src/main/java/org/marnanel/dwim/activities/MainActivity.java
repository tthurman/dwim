package org.marnanel.dwim.activities;

import android.app.Activity;
import android.os.Bundle;
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

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

import org.marnanel.dwim.DwimApplication;
import org.marnanel.dwim.R;

public class MainActivity extends Activity {

        private static final String TAG = "MainActivity";

        @Override protected void onCreate(Bundle icicle) {

                super.onCreate(icicle);
                Log.d(TAG, "start creating.");

                setContentView(R.layout.activity_main);

                AccountHeader headerResult = new AccountHeaderBuilder()
                        .withActivity(this)
                        .build();
                Log.d(TAG, "Added account header to drawer.");

                new DrawerBuilder()
                        .withActivity(this)
                        .withAccountHeader(headerResult)
                        .build();
                Log.d(TAG, "Added drawer.");

                Log.d(TAG, "Created.");
        }

}

