package org.marnanel.dwim;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class MainActivity extends Activity {

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    WebView webView = new WebView(this);
    setContentView(webView);

    webView.loadUrl("file:///android_asset/timeline.html");

  }
}
