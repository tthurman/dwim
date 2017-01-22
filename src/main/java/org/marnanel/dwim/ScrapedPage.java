package org.marnanel.dwim;

import android.util.Log;

import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.IOException;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.safety.Whitelist;

import org.marnanel.dwim.ScrapingException;

public class ScrapedPage {

        public static final String TAG = "ScrapedPage";

        protected Document mDoc;

        public ScrapedPage(String source) throws ScrapingException {

                String html;

                Log.d(TAG, "ScrapedPage starting");

                if (source.startsWith("http")) {
                        Log.d(TAG, "source appears to be a URL:");
                        Log.d(TAG, source);

                        html = fetchPage(source);
                } else {
                        html = source;
                }

                Log.d(TAG, "ScrapedPage: running parse");
                mDoc = Jsoup.parse(html, "UTF-8");
                // XXX what does Jsoup do for mangled input?
                // we should throw ScrapingException

                Log.d(TAG, "ScrapedPage: running scrape");
                scrape();

                Log.d(TAG, "ScrapedPage: init complete");
        }

        private static String fetchPage(String address)
                throws ScrapingException {

                Log.d(TAG, "fetchPage, starting, for: "+address);

                try {
                        URLConnection uplink = new URL(address).openConnection();
                        Log.d(TAG, "connection opened");

                        InputStream is = uplink.getInputStream();
                        Log.d(TAG, "got the input stream");
                        // Concise but hacky way to read
                        // the entire stream into the string:
                        Scanner scanner = new Scanner(is).useDelimiter("\\A");
                        String content = scanner.hasNext() ? scanner.next() : "";

                        Log.d(TAG, "Received.");

                        return content;

                } catch (IOException ioe) {

                        Log.d(TAG, "fetchScrapedPage got an IOException: "+ioe.toString());

                        // XXX consider whether passing the IOE upwards
                        // is a better plan (or wrapping it)

                        throw new ScrapingException(ioe.toString());

                }
        }

        protected void scrape() throws ScrapingException {
                // We do nothing in the superclass.
        }
}
