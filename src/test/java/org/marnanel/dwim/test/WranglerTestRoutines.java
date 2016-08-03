package org.marnanel.dwim.test;

import org.marnanel.dwim.Wrangler;
import org.junit.Test;
import static org.junit.Assert.*;
import android.util.Log;
import android.os.Bundle;

public class WranglerTestRoutines {

        static final String TAG = "WranglerTestRoutines";

        public static void wranglerTest(String sourceText, String expectText) {

                Bundle result = Wrangler.parse(sourceText);
                Log.w(TAG, result.toString());
                assert(false);
        }
}
