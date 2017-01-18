package org.marnanel.dwim;

import android.util.Log;
import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.IOException;
import java.util.Scanner;
import org.marnanel.dwim.ScrapedPage;

public class PageFetcher {

        private static String TAG = "PageFetcher";

        protected PageFetcher() {
        }

        public static ScrapedPage fetchScrapedPage(String address) {

                Log.d(TAG, "fetchScrapedPage, starting, for: "+address);

                try {
                        URLConnection uplink = new URL(address).openConnection();
                        Log.d(TAG, "fetchScrapedPage: connection opened");

                        InputStream is = uplink.getInputStream();
                        Log.d(TAG, "fetchScrapedPage: got the input stream");
                        // Concise but hacky way to read
                        // the entire stream into the string:
                        Scanner scanner = new Scanner(is).useDelimiter("\\A");
                        String content = scanner.hasNext() ? scanner.next() : "";

                        Log.d(TAG, "Received: "+content);

                        // for now...
                        return null;

                } catch (IOException ioe) {


                        Log.d(TAG, "fetchScrapedPage got an IOException: "+ioe.toString());
                        // for now...
                        return null;

                }
        }
}

