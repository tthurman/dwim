package org.marnanel.dwim;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.content.Intent;

import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Scanner;
import java.io.InputStream;
import android.util.Log;

public class MainActivity extends Activity {

  private static final String TAG = "dwimMainActivity";

  public void onCreate(Bundle savedInstanceState) {

    String DW_URL = "http://marnanel.dreamwidth.org/?style=lynx";
    URL url;
    InputStream stream;
    WebView webView = new WebView(this);
    String pageText;

    super.onCreate(savedInstanceState);

    Log.d(TAG, "Created.");

    try {
       url = new URL(DW_URL);
       Log.d(TAG, "URL created successfully");
    } catch (java.net.MalformedURLException murle) {
       url = null;
       Log.d(TAG, "Problem creating URL", murle);
       return;
    }

    setContentView(webView);
    webView.loadData("loading...",
	"text/plain",
	null);

    try {
       InputStream inputStream;
       Scanner scanner;
       HttpURLConnection connection = (HttpURLConnection) url.openConnection();
       
       inputStream = connection.getInputStream();
       scanner = new java.util.Scanner(inputStream).useDelimiter("\\A");
       pageText = scanner.hasNext()? scanner.next(): "";

       Log.d(TAG, pageText);

       connection.disconnect();
    } catch (java.io.IOException jie) {
       Log.d(TAG, "Problem fetching page", jie);
       pageText = "it went wrong";
    }

    webView.loadData(pageText,
	"text/plain",
	null);

  }

}

