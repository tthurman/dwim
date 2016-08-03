package org.marnanel.dwim.activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.content.Intent;

import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Scanner;
import java.io.InputStream;
import android.util.Log;

import org.marnanel.dwim.DwimApplication;
import org.marnanel.dwim.events.UserTimelineRequestEvent;

public class TimelineActivity extends Activity {

  private static final String TAG = "TimelineActivity";

  protected WebView _webView = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    Log.d(TAG, "Created.");

    DwimApplication.bus().register(this);

    _webView = new WebView(this);
    setContentView(_webView);

    _webView.loadData("Hello world.",
	    "text/plain",
	    null);

    Log.d(TAG, "Requesting page...");
    DwimApplication.bus().post(new UserTimelineRequestEvent("marnanel"));
    Log.d(TAG, "Requested page.");
  }

  @Override
  protected void onDestroy() {
      DwimApplication.bus().unregister(this);
      super.onDestroy();
  }

}

