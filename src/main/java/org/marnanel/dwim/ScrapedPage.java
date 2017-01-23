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

/**
 * An HTML page, along with the information gleaned from it.
 *
 * This is the superclass, and does nothing useful with
 * the HTML it finds. The subclasses parse the HTML
 * as needed, for a variety of uses.
*/
public class ScrapedPage {

        public static final String TAG = "ScrapedPage";

        /**
         * Parsed contents of the page. May be null,
         * if the page wasn't supplied to (or by) the constructor.
        */
        protected Document mDoc = null;

        /**
         * Constructor. In the superclass this leaves mDoc null;
         * you will have to use the fetch methods if you want to
         * populate the object.
        */
        public ScrapedPage() throws ScrapingException {
                // nothing
        }

        /**
         * A constructor that can populate mDoc.
         *
         * The parameter has a number of uses:
         *   - If it is an empty string, then mDoc
         *     is left null, as with the simple constructor.
         *   - If it begins "http", it is assumed to be
         *     a URL; the HTML at that address is loaded
         *     and parsed.
         *   - If it begins "/", it is assumed to be
         *     a path on the Dreamwidth website.
         *     This is very hacky and will be replaced
         *     before we release. (No, really.)
         *   - Otherwise, the string is assumed to
         *     contain HTML. It gets parsed.
         * 
         * @fixme talk about error conditions here
         *
         * @params  source  The source string; see above.
         */
        public ScrapedPage(String source) throws ScrapingException {

                Log.d(TAG, "starting");

                if (source=="") {

                        // do nothing, and
                        // expect them to sort it out
                        // for themselves later

                } else if (source.startsWith("http")) {
                        Log.d(TAG, "source appears to be a URL");

                        parseHtmlFromUrl(source);

                } else if (source.startsWith("/")) {
                        Log.d(TAG, "source appears to be a path");

                        parseHtmlFromPath(source);
                 } else {
                        parseHtml(source);
                }
                Log.d(TAG, "init complete");
        }

        public void parseHtml(String html)
                throws ScrapingException {
                Log.d(TAG, "parsing...");

                mDoc = Jsoup.parse(html, "UTF-8");
                // XXX what does Jsoup do for mangled input?
                // we should throw ScrapingException

                Log.d(TAG, "parsing done. Running scrape.");
                scrape();
        }

        public void parseHtmlFromUrl(String url)
                throws ScrapingException {
                Log.d(TAG, "fetching HTML from "+url);
                String html = fetchPage(url);
                parseHtml(html);
        }

        public void parseHtmlFromPath(String path)
                throws ScrapingException {
                Log.d(TAG, "fetching HTML from path "+path);
                String url = "https://www.dreamwidth.org" +
                        path +
                        "?usescheme=lynx";

                String html = fetchPage(url);
                parseHtml(html);
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
