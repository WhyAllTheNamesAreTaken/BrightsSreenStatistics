package com.blackheart.brightscreenstatistics;

import android.app.Application;
import android.content.Context;
import org.litepal.LitePal;

/**
 * Created by l4656_000 on 2015/11/14.
 */
public class MyApplication extends Application {
    private static Context instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        LitePal.initialize(instance);

    }

    public static Context getInstance(){
        return instance;
    }

}
