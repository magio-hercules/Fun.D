package study.easycalendar;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class App extends Application {

    private static App applicationInstance;

    public static App getApplicationInstance() {
        return applicationInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationInstance = this;
        Stetho.initializeWithDefaults(this);
        AndroidThreeTen.init(this);
    }
}
