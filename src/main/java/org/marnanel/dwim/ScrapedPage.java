package org.marnanel.dwim;

// Under construction. This used to be Wrangler, a static parser class.
// It's turning into ScrapedPage, which gets instantiated with some
// HTML and can parse it as needed.

import java.util.ArrayList;
import java.util.HashMap;
import android.util.Log;
import android.os.Bundle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.safety.Whitelist;

public class ScrapedPage {

        public static final String TAG = "ScrapedPage";

        public static final String PAGE_TYPE = "pagetype";
        public static final String PAGE_TYPE_TIMELINE = "timeline";
        public static final String PAGE_TYPE_LOGIN = "login";
        public static final String PAGE_TYPE_UNKNOWN = "unknown";

        protected mDoc;

        public ScrapedPage(String html,
                String expectedType) {

                Bundle result;
                mDoc = Jsoup.parse(html, "UTF-8");

                // XXX rewrite this, now that we're not using static methods

                // This isn't elegant but I've messed
                // around with trying to build a list
                // of inner classes within a static method
                // for far too long. Why did Java take
                // so long to get method references?

                result = wrangleTimeline(doc);
                if (result!=null) { return result; }

                result = wrangleIcons(doc);
                if (result!=null) { return result; }

                result = wrangleTaglist(doc);
                if (result!=null) { return result; }

                result = wrangleLogin(doc);
                if (result!=null) { return result; }

                return wrangleUnknown(doc);
        }

        protected static Bundle wrangleTimeline(Document doc) {

                Elements sourceEntries = doc.select(".entry-wrapper");

                if (sourceEntries.isEmpty()) {

                        return null;
                }
                ArrayList<Bundle> resultEntries = new ArrayList<Bundle>();
                
                for (Element sourceEntry: sourceEntries) {

                        Bundle resultEntry = new Bundle();

                        resultEntry.putString("sec", sourceEntry.className());
                        for (String sourceEntryClass: sourceEntry.classNames()) {
                                if (sourceEntryClass.startsWith("has-") ||
                                    sourceEntryClass.startsWith("entry-")) {
                                        continue;
                                }
                        
                                int lastHyphen = sourceEntryClass.lastIndexOf('-');

                                if (lastHyphen==-1) {
                                        continue;
                                }

                                String field = sourceEntryClass.substring(0,lastHyphen);
                                String value = sourceEntryClass.substring(lastHyphen+1);

                                resultEntry.putString(field, value);
                        }

                       Element content = sourceEntry.select(".entry-content").first();
                        if (content==null) {
                                resultEntry.putString("content", "");
                        } else {
                                Whitelist whitelist = Whitelist.relaxed();
                                String cleanedHTML =
                                        Jsoup.clean(content.html(),
                                                Whitelist.relaxed());
                                resultEntry.putString("content", cleanedHTML);
                        }

                        // TODO: images
                        // Not really our problem atm, but when we handle
                        // offline access, we'll need to preload.

                        Element title = sourceEntry.select(".entry-title a").first();
                        if (title==null) {
                                // XXX tbh an entry without a permalink is
                                // probably not very useful; maybe we should
                                // discard it.
                                resultEntry.putString("title", "");
                                resultEntry.putString("permalink", "");
                        } else {
                                resultEntry.putString("title", title.attr("title"));
                                resultEntry.putString("permalink", title.attr("href"));
                        }


                        // TODO: tags

                        // TODO: userpic

                        resultEntries.add(resultEntry);
                }

                Bundle result = new Bundle();
                result.putString(PAGE_TYPE, PAGE_TYPE_TIMELINE);
                result.putParcelableArrayList("entries", resultEntries);

                return result;
        }

        protected static Bundle wrangleTaglist(Document doc) {
                return null;
        }

        protected static Bundle wrangleIcons(Document doc) {
                return null;
        }

        protected static Bundle wrangleLogin(Document doc) {
                if (doc.select(".lj_login_form").isEmpty()) {
                        return null;
                }

                Bundle result = new Bundle();

                for (Element e: doc.select("input")) {
                        result.putString(
                                e.attr("name"),
                                e.attr("value"));
                }

                // Add system fields last, so the parsed HTML
                // can't override them.
                result.putString(PAGE_TYPE, PAGE_TYPE_LOGIN);

                return result;
        }

        protected static Bundle wrangleUnknown(Document doc) {
                Bundle result = new Bundle();
                result.putString(PAGE_TYPE, PAGE_TYPE_UNKNOWN);

                return result;
        }

        public static Bundle parse(String html) {
        }
}
