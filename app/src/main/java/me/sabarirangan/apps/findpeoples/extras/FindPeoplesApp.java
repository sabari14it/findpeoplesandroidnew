package me.sabarirangan.apps.findpeoples.extras;

import android.app.Application;
import android.content.ContextWrapper;
//
//import com.nostra13.universalimageloader.core.ImageLoader;

//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pixplicity.easyprefs.library.Prefs;

import io.realm.Realm;

/**
 * Created by Rinik on 01/01/17.
 */

public class FindPeoplesApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
        Realm.init(this);
    }
}
