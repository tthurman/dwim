package org.marnanel.dwim;

import android.app.Application;

public class DwimApplication extends Application {

    private static DwimApplication _app;

    public DwimApplication() {
	_app = this;
    }

    public static DwimApplication app() {
        return _app;
    }

}

