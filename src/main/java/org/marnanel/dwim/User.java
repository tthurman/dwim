package org.marnanel.dwim;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;
import java.util.ArrayList;

import android.util.Log;

/**
 * This will eventually represent a logged-in user.
 * At present, it's just a stub.
*/
public class User implements CookieStore {

        private String TAG = "User";

        public void add(URI uri, HttpCookie cookie) {
                Log.d(TAG, "added: "+cookie);
        }

        public List<HttpCookie> get(URI uri) {
                return new ArrayList<HttpCookie>();
        }

        public List<HttpCookie> getCookies() {
                return new ArrayList<HttpCookie>();
        }

        public List<URI> getURIs() {
                return new ArrayList<URI>();
        }

        public boolean remove(URI uri, HttpCookie cookie) {
                return false;
        }

        public boolean removeAll() {
                return false;
        }
}
