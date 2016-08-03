package org.marnanel.dwim;

import android.os.IBinder;
import android.content.Intent;
import android.app.Service;

import org.marnanel.dwim.DwimAuthenticator;

/**
 * Here's where we handle requests for our authenticator
 * from other apps. In practice, that means the
 * device's AccountManager.
 */
public class DwimAuthenticatorService extends Service {
        
        @Override public IBinder onBind(Intent intent) {

                DwimAuthenticator authenticator = new DwimAuthenticator(this);

                return authenticator.getIBinder();

        }
}

