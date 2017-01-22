package org.marnanel.dwim;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.safety.Whitelist;

import org.marnanel.dwim.ScrapedPage;
import org.marnanel.dwim.ScrapingException;

public class ScrapedLoginPage
        extends ScrapedPage {

        public static final String TAG = "ScrapedLoginPage";

        public ScrapedLoginPage(String source) throws ScrapingException {
                super(source);
        }

        protected void scrape() throws ScrapingException {
                // We do nothing in the superclass.
                if (mDoc.select(".lj_login_form").isEmpty()) {
                        throw new ScrapingException("Not a login form");
                }

                for (Element e: mDoc.select("input")) {
                        Log.d(TAG, "---- form pair:");
                        Log.d(TAG, e.attr("name"));
                        Log.d(TAG, e.attr("value"));
                }
        }

}
