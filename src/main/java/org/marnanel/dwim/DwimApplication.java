package org.marnanel.dwim;

import android.app.Application;
import com.squareup.otto.Bus;
import org.marnanel.dwim.events.NetworkRequestEvent;

public class DwimApplication extends Application {

    private static DwimApplication _app;
    private static Bus _bus = null;

    public DwimApplication() {
	_app = this;
    }

    public static DwimApplication app() {
        return _app;
    }

    public static Bus bus() {
	if (_bus==null) {
	    _bus = new Bus();
	}
	return _bus;
    }

}

