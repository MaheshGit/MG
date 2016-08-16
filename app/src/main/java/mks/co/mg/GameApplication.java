package mks.co.mg;

import android.app.Application;
import android.content.Context;

/**
 * Created by Mahesh on 13/8/16.
 */
public class GameApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
