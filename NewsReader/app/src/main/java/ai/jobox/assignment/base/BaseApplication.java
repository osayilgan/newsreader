package ai.jobox.assignment.base;

import android.app.Application;

public class BaseApplication extends Application {

    /** Instance of Application Class */
    private static BaseApplication instance;

    public BaseApplication() {
        instance = this;
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
